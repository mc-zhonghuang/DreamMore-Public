/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "HytAutoVelocity", description = "AutoVelocity", category = ModuleCategory.HYT)
class KbHelper : Module() {
    @EventTarget
    fun UpdateEvent(event: UpdateEvent) {
        var velocity_module: Velocity? = null

        velocity_module!!.state = mc.thePlayer!!.onGround
    }

    override fun onEnable() {
        var velocity_module: Velocity? = null
        velocity_module = LiquidBounce.moduleManager.getModule(Velocity::class.java) as Velocity
    }
}