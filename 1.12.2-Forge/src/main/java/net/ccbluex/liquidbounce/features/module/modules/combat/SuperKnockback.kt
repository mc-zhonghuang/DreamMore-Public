/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketEntityAction


@ModuleInfo(name = "StrongKnockback", category = ModuleCategory.COMBAT, description = "fdp")
class SuperKnockback : Module() {
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val modeValue = ListValue("Mode", arrayOf("ExtraPacket", "WTap", "Packet", "HuaYuTing"), "ExtraPacket")
    private val WtapDelay = IntegerValue("WTapDelay", 6, 1, 10)
    private val onlyMoveValue = BoolValue("OnlyMove", false)
    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val delayValue = IntegerValue("Delay", 0, 0, 500)

    private var ticks = 0

    val timer = MSTimer()

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            if (event.targetEntity.hurtTime > hurtTimeValue.get() || !timer.hasTimePassed(delayValue.get().toLong()) ||
                (!MovementUtils.isMoving && onlyMoveValue.get()) || (!mc.thePlayer!!.onGround && onlyGroundValue.get())) {
                return
            }
            when (modeValue.get().toLowerCase()) {
                "extrapacket" -> {
                    if (mc2.player.isSprinting) {
                        mc2.player.isSprinting = true
                    }
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING))
                    mc.thePlayer!!.serverSprintState = true
                }

                "huayuting" -> {
                    if (mc2.player.isSprinting)
                        mc2.connection!!
                            .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                    mc2.connection!!
                        .sendPacket(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                    mc.thePlayer!!.sprinting = true
                    mc.thePlayer!!.serverSprintState = true
                }

                "wtap" -> {
                    ticks = 0
                }
                "packet" -> {
                    if (mc2.player.isSprinting) {
                        mc2.player.isSprinting = true
                    }
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING))
                    mc.thePlayer!!.serverSprintState = true
                }
            }
            timer.reset()
        }
    }

    @EventTarget
    fun onUpdate() {
        ticks ++
        if (modeValue.equals("WTap")) {
            if (ticks <= WtapDelay.get()) {
                mc.gameSettings.keyBindForward.pressed = false
            } else if (ticks == WtapDelay.get() + 1) {
                mc.gameSettings.keyBindForward.pressed = true
            }
        }
    }

    @EventTarget
    fun onPreMotion() {
        if (modeValue.equals("WTap")) {
            if (ticks <= WtapDelay.get()) {
                mc.gameSettings.keyBindForward.pressed = false
            } else if (ticks == WtapDelay.get() + 1) {
                mc.gameSettings.keyBindForward.pressed = true
            }
        }
    }

    override val tag: String
        get() = modeValue.get()
}
