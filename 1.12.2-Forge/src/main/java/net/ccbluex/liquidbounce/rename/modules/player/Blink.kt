package net.ccbluex.liquidbounce.rename.modules.player

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.model.ModelPlayer
import net.minecraft.client.renderer.entity.RenderLivingBase
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.Packet
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.login.client.CPacketEncryptionResponse
import net.minecraft.network.login.client.CPacketLoginStart
import net.minecraft.network.play.client.CPacketAnimation
import net.minecraft.network.play.client.CPacketChatMessage
import net.minecraft.network.play.client.CPacketConfirmTeleport
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketCustomPayload
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketPlayerTryUseItem
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.network.status.client.CPacketPing
import net.minecraft.network.status.client.CPacketServerQuery
import net.minecraftforge.event.ForgeEventFactory
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "Blink", description = "Suspends all movement packets.", category = ModuleCategory.PLAYER, autoDisableMode = Module.AutoDisableMode.WORLDCHANGE)
class Blink : Module() {
    private val packets = LinkedBlockingQueue<Packet<*>>()
    private var fakePlayer: EntityOtherPlayerMP? = null
    private var disableLogger = false
    private val positions = LinkedList<DoubleArray>()

    private val pulseValue = BoolValue("Pulse", false)
    private val c0FValue = BoolValue("C0FCancel", false)
    private val pulseDelayValue = IntegerValue("PulseDelay", 1000, 500, 5000)

    private val pulseTimer = MSTimer()

    override fun onEnable() {
        if (mc.thePlayer == null) return
        if (!pulseValue.get()) {
            fakePlayer = EntityOtherPlayerMP(mc2.world, mc.thePlayer!!.gameProfile)
            fakePlayer!!.copyLocationAndAnglesFrom(mc2.player)
            fakePlayer!!.rotationYawHead = mc.thePlayer!!.rotationYawHead
            mc2.world.addEntityToWorld(-114514, fakePlayer!!)
        }
        synchronized(positions) {
            positions.add(
                doubleArrayOf(
                    mc.thePlayer!!.posX,
                    mc.thePlayer!!.entityBoundingBox.minY + mc.thePlayer!!.eyeHeight / 2,
                    mc.thePlayer!!.posZ
                )
            )
            positions.add(
                doubleArrayOf(
                    mc.thePlayer!!.posX,
                    mc.thePlayer!!.entityBoundingBox.minY,
                    mc.thePlayer!!.posZ
                )
            )
        }
        pulseTimer.reset()
    }

    override fun onDisable() {
        if (mc.thePlayer == null) return
        blink()
        if (fakePlayer != null) {
            mc.theWorld!!.removeEntityFromWorld(fakePlayer!!.entityId)
            fakePlayer = null
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet: Packet<*> = event.packet.unwrap()
        if (mc.thePlayer == null || disableLogger) return
        if (packet is CPacketPlayer)
            event.cancelEvent()
        if (packet is CPacketPlayer.Position || packet is CPacketPlayer.PositionRotation ||
            packet is CPacketPlayerTryUseItem || packet is CPacketPlayerTryUseItemOnBlock ||
            packet is CPacketAnimation ||
            packet is CPacketEntityAction || packet is CPacketUseEntity || (c0FValue.get() && (packet !is CPacketCustomPayload && packet !is CPacketChatMessage && packet !is C00Handshake && packet !is CPacketServerQuery && packet !is CPacketLoginStart && packet !is CPacketPing && packet !is CPacketEncryptionResponse))
        ) {
            event.cancelEvent()
            packets.add(packet)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        synchronized(positions) {
            positions.add(
                doubleArrayOf(
                    mc.thePlayer!!.posX,
                    mc.thePlayer!!.entityBoundingBox.minY,
                    mc.thePlayer!!.posZ
                )
            )
        }
        if (pulseValue.get() && pulseTimer.hasTimePassed(pulseDelayValue.get().toLong())) {
            blink()
            pulseTimer.reset()
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        val breadcrumbs: Breadcrumbs = LiquidBounce.moduleManager.getModule(Breadcrumbs::class.java) as Breadcrumbs
        val color = if (breadcrumbs.colorRainbow.get()) rainbow() else Color(
            breadcrumbs.colorRedValue.get(),
            breadcrumbs.colorGreenValue.get(),
            breadcrumbs.colorBlueValue.get()
        )
        synchronized(positions) {
            GL11.glPushMatrix()
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            mc.entityRenderer.disableLightmap()
            GL11.glBegin(GL11.GL_LINE_STRIP)
            RenderUtils.glColor(color)
            val renderPosX = mc.renderManager.viewerPosX
            val renderPosY = mc.renderManager.viewerPosY
            val renderPosZ = mc.renderManager.viewerPosZ
            for (pos in positions) GL11.glVertex3d(
                pos[0] - renderPosX,
                pos[1] - renderPosY,
                pos[2] - renderPosZ
            )
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
            GL11.glEnd()
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glPopMatrix()
        }
    }

    override val tag: String
        get() = packets.size.toString()

    private fun blink() {
        try {
            disableLogger = true
            while (!packets.isEmpty()) {
                mc2.connection!!.networkManager.sendPacket(packets.take())
            }
            disableLogger = false
        } catch (e: Exception) {
            e.printStackTrace()
            disableLogger = false
        }
        synchronized(positions) { positions.clear() }
    }
}