package net.ccbluex.liquidbounce.rename.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.network.play.server.SPacketConfirmTransaction

@ModuleInfo(name = "Timer", description = "Changes the speed of the entire game.", category = ModuleCategory.WORLD)
class Timer : Module() {

    private val speedValue = FloatValue("Speed", 2F, 0.1F, 10F)
    private val spoofValue = BoolValue("Spoof", false)
    private val grimACValue = BoolValue("GrimAC", false)
    private val onMoveValue = BoolValue("OnMove", true)
    private var nowPackets = arrayListOf<Packet<INetHandlerPlayServer>>()

    private val timer = MSTimer()

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()

        if (packet is SPacketConfirmTransaction) {
            if (grimACValue.get())
                event.cancelEvent()
        }
        if (packet is CPacketUseEntity) {
            if (grimACValue.get())
                event.cancelEvent()
        }
    }

    override fun onEnable() {
        timer.reset()
    }

    override fun onDisable() {
        if (mc.thePlayer == null)
            return

        mc.timer.timerSpeed = 1F
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (spoofValue.get()) {
            if (timer.hasTimePassed(2000L)) {
                mc.timer.timerSpeed = 2.0f
                if (timer.hasTimePassed(3500L)) {
                    toggle()
                    mc.timer.timerSpeed = 1f
                    LiquidBounce.hud.notifications.add(Notification("Timer Info", "Packet value reaches the upper limit, disabled!", NotifyType.INFO))
                }
            } else {
                mc.timer.timerSpeed = 0.05f
            }
            return
        }
        if(MovementUtils.isMoving || !onMoveValue.get()) {
            mc.timer.timerSpeed = speedValue.get()
            return
        }

        mc.timer.timerSpeed = 1F
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        if (event.worldClient != null)
            return

        state = false
    }
}
