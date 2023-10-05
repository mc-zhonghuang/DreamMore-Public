package net.ccbluex.liquidbounce.management

import com.allatori.annotations.Rename
import net.ccbluex.liquidbounce.HappyMan
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.LiquidBounce.data
import net.ccbluex.liquidbounce.LiquidBounce.useIP
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.UUIDUtils
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils.doPost
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.client.Minecraft
import top.fl0wowp4rty.Heavy
import top.fl0wowp4rty.Native

import java.util.HashMap

/**
@author ChengFeng
@since 2022/11/26
 */
@Rename
@Heavy
@Native
class CombatManager: MinecraftInstance(), Listenable {
    private val lastAttackTimer = MSTimer()
    var inCombat = false
    var kills = 0
    var target : IEntityLivingBase? = null
    val attackTimer = MSTimer()
    private val attackedList = ArrayList<IEntityLivingBase>()
    override fun handleEvents(): Boolean {
        return true
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is IEntityLivingBase) {
            target = event.targetEntity
            attackedList.add(event.targetEntity)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {

        if (target != null && (mc.thePlayer!!.getDistanceToEntityBox(target!!) > 7 || target!!.health <= 0 || target!!.isDead)) {
            target = null
        }
        inCombat = false
        if (!lastAttackTimer.hasTimePassed(1000)) {
            inCombat = true
            return
        }

        if (target != null) {
            if (mc.thePlayer!!.getDistanceToEntity(target!!) > 7 || !inCombat || target!!.isDead) {
                target = null
            } else {
                inCombat = true
            }
        }
        attackedList.map { it }.forEach {
            if (it.isDead || it.health <= 0) {
                attackedList.remove(it)
                kills += 1
                LiquidBounce.eventManager.callEvent(EntityKilledEvent(it))
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        HappyMan.fuck4()
    }
}