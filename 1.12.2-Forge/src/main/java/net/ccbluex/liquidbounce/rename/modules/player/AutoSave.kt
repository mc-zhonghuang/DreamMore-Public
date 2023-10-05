/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.rename.modules.player


import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.block.BlockUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.block.BlockAir


@ModuleInfo(name = "AutoSave", description = "pureland.", category = ModuleCategory.PLAYER)
class AutoSave : Module() {
    private val maxFallDistValue = FloatValue("MaxFallDistance", 5F, 5F, 20F)
    private val voidOnlyValue = BoolValue("OnlyVoid", true)

    private var blink = false
    private var canBlink = false
    private var canSpoof = false
    private var tried = false
    private var flagged = false

    private var posX = 0.0
    private var posY = 0.0
    private var posZ = 0.0

    override fun onEnable() {
        blink = false
        canBlink = false
        canSpoof = false
        tried = false
        flagged = false
    }

    @EventTarget
    
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer!!.onGround) {
            tried = false
            flagged = false
        }
        if (mc!!.thePlayer!!.onGround && BlockUtils.getBlock(WBlockPos(mc.thePlayer!!.posX, mc.thePlayer!!.posY - 1.0, mc.thePlayer!!.posZ)) !is BlockAir) {
            posX = mc.thePlayer!!.prevPosX
            posY = mc.thePlayer!!.prevPosY
            posZ = mc.thePlayer!!.prevPosZ
        }
        if (!voidOnlyValue.get() || checkVoid()) {
            if (mc.thePlayer!!.fallDistance > maxFallDistValue.get() && !tried) {
                //保存切换烈焰棒之前手的位置
                val temp = mc.thePlayer!!.inventory.currentItem
                //快速切换烈焰棒并右键
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(findSlot()))
                mc.netHandler.addToSendQueue(classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND))
                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange(temp))
                mc.thePlayer!!.inventory.currentItem = temp
                mc.playerController.updateController()
                tried = true
            }
        }
    }

    //抄袭antivoid
    private fun checkVoid(): Boolean {
        var i = (-(mc.thePlayer!!.posY-1.4857625)).toInt()
        var dangerous = true
        while (i <= 0) {
            dangerous = mc.theWorld!!.getCollisionBoxes(mc.thePlayer!!.entityBoundingBox.offset(mc.thePlayer!!.motionX * 0.5, i.toDouble(), mc.thePlayer!!.motionZ * 0.5)).isEmpty()
            i++
            if (!dangerous) break
        }
        return dangerous
    }

    private fun findSlot(): Int {
        //寻找烈焰棒
        for (slot in 0..8) {
            if (mc.thePlayer!!.inventory.getStackInSlot(slot)!!.unlocalizedName.toLowerCase().contains("blaze")) {
                return slot
            }
        }
        return 0
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
    }
}