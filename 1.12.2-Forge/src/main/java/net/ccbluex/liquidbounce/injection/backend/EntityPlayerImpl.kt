
package net.ccbluex.liquidbounce.injection.backend

import com.mojang.authlib.GameProfile
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase
import net.ccbluex.liquidbounce.api.minecraft.util.IFoodStats
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemSword
import net.minecraft.util.EnumHand

open class EntityPlayerImpl<T : EntityPlayer>(wrapped: T) : EntityLivingBaseImpl<T>(wrapped), IEntityPlayer {
    override val gameProfile: GameProfile
        get() = wrapped.gameProfile
    override val fishEntity: IEntity?
        get() = wrapped.fishEntity?.wrap()
    override val foodStats: IFoodStats
        get() = wrapped.foodStats.wrap()
    override val prevChasingPosY: Double
        get() = wrapped.prevChasingPosY
    override var sleepTimer: Int
        get() = wrapped.sleepTimer
        set(value) {
            wrapped.sleepTimer = value
        }
    override var sleeping: Boolean
        get() = wrapped.sleeping
        set(value) {
            wrapped.sleeping = value
        }
    override val isPlayerSleeping: Boolean
        get() = wrapped.isPlayerSleeping
    override var speedInAir: Float
        get() = wrapped.speedInAir
        set(value) {
            wrapped.speedInAir = value
        }
    override var cameraYaw: Float
        get() = wrapped.cameraYaw
        set(value) {
            wrapped.cameraYaw = value
        }
    override val isBlocking: Boolean
        get() = (LiquidBounce.moduleManager.getModule(KillAura::class.java).state && !(LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura).autoBlockValue.equals("Off", true) && (LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura).target != null) || (LiquidBounce.moduleManager.getModule(KillAura2::class.java).state && !(LiquidBounce.moduleManager.getModule(KillAura2::class.java) as KillAura2).autoBlockValue.equals("Off", true) && (LiquidBounce.moduleManager.getModule(KillAura2::class.java) as KillAura2).target != null) || (wrapped.getHeldItem(EnumHand.MAIN_HAND).item is ItemSword && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isPressed)

    override var itemInUseCount: Int
        get() = wrapped.itemInUseCount
        set(value) {
            wrapped.activeItemStackUseCount = value
        }
    override val itemInUse: IItemStack?
        get() = wrapped.activeItemStack?.wrap()
    override val capabilities: IPlayerCapabilities
        get() = wrapped.capabilities.wrap()
    override val heldItem: IItemStack?
        get() = wrapped.heldItemMainhand?.wrap()
    override val isUsingItem: Boolean
        get() = wrapped.isHandActive
    override val inventoryContainer: IContainer
        get() = wrapped.inventoryContainer.wrap()
    override val inventory: IInventoryPlayer
        get() = wrapped.inventory.wrap()
    override val openContainer: IContainer?
        get() = wrapped.openContainer?.wrap()
    override val itemInUseDuration: Int
        get() = wrapped.itemInUseMaxCount
    override val displayNameString: String
        get() = wrapped.displayNameString
    override val spectator: Boolean
        get() = wrapped.isSpectator

    override fun stopUsingItem() = wrapped.stopActiveHand()

    override fun onCriticalHit(entity: IEntity) = wrapped.onCriticalHit(entity.unwrap())

    override fun onEnchantmentCritical(entity: IEntityLivingBase) = wrapped.onEnchantmentCritical(entity.unwrap())

    override fun attackTargetEntityWithCurrentItem(entity: IEntity) = wrapped.attackTargetEntityWithCurrentItem(entity.unwrap())

    override fun fall(distance: Float, damageMultiplier: Float) = wrapped.fall(distance, damageMultiplier)

    override fun triggerAchievement(stat: IStatBase) = wrapped.addStat(stat.unwrap())

    override fun jump() = wrapped.jump()
    override fun getCooledAttackStrength(adjustTicks: Float): Float = wrapped.getCooledAttackStrength(adjustTicks)
    override fun resetCooldown() = wrapped.resetCooldown()

}

inline fun IEntityPlayer.unwrap(): EntityPlayer = (this as EntityPlayerImpl<*>).wrapped
inline fun EntityPlayer.wrap(): IEntityPlayer = EntityPlayerImpl(this)
