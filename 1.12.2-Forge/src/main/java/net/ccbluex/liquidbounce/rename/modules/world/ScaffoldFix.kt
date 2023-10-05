package net.ccbluex.liquidbounce.rename.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "HytScaffoldFix", description = "KillFix", category = ModuleCategory.HYT)
class ScaffoldFix : Module() {
    val modeValue = ListValue("Mode", arrayOf("Fix", "none"), "Fix")
    var a = 1
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val scaffoldModule = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        when(modeValue.get().toLowerCase()){
            "Fix" -> {
                if (a == 40){
                    ClientUtils.displayChatMessage("ScaffoldFix")
                    ClientUtils.displayChatMessage("idan fuck you")
                    a = 0;
                }
                if(mc.thePlayer!!.onGround) {
                    scaffoldModule.state = false
                    mc.thePlayer!!.jump();
                } else {
                    scaffoldModule.state = true
                }
            }
            "none" -> {
                ClientUtils.displayChatMessage("idan chou sha bi")
            }
       }
    }
}