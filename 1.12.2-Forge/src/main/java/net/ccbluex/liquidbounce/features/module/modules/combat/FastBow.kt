/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.api.enums.EnumFacingType
import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "FastBow", description = "Turns your bow into a machine gun.", category = ModuleCategory.COMBAT)
class FastBow : Module() {

    private val packetsValue = IntegerValue("Packets", 20, 3, 20)
    private val delay = IntegerValue("Delay", 160, 160, 500)
    private val onlyGround = BoolValue("OnlyGround",true)


    val timer = MSTimer()
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (!thePlayer.isUsingItem)
            return

        val currentItem = thePlayer.inventory.getCurrentItemInHand()

        if (currentItem != null && classProvider.isItemBow(currentItem.item)) {
            // TODO Find out what this is suppose to do
            //mc.netHandler.addToSendQueue(UseItemPacket(currentItem, WEnumHand.MAIN_HAND))
            //mc2.connection!!.sendPacket(CPacketPlayerTryUseItem(EnumHand.MAIN_HAND))
            mc.netHandler.addToSendQueue(classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND))
            //mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(WBlockPos.ORIGIN, 255, currentItem, 0F, 0F, 0F))

            val yaw = if (RotationUtils.targetRotation != null)
                RotationUtils.targetRotation.yaw
            else
                thePlayer.rotationYaw

            val pitch = if (RotationUtils.targetRotation != null)
                RotationUtils.targetRotation.pitch
            else
                thePlayer.rotationPitch

            for (i in 0 until packetsValue.get())
                //mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerLook(yaw, pitch, onlyGround.get()))
            if(timer.hasTimePassed(delay.get().toLong())) {
                mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
                timer.reset()
            }
            thePlayer.itemInUseCount = currentItem.maxItemUseDuration - 1
        }
    }
}