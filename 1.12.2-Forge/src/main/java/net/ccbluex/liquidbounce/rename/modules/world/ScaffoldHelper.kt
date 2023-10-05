package net.ccbluex.liquidbounce.rename.modules.hyt

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.rename.modules.world.Scaffold
import net.ccbluex.liquidbounce.rename.modules.world.Timer

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "ScaffoldHelper", description = "ScaffoldHelper", category = ModuleCategory.HYT, autoDisableMode = Module.AutoDisableMode.WORLDCHANGE)
class ScaffoldHelper : Module() {
    override fun onDisable() {
        LiquidBounce.moduleManager.getModule(Scaffold::class.java).state = false
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        var scaffoldmodule = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
        if (!mc.thePlayer!!.sneaking){
            scaffoldmodule.state = !mc.thePlayer!!.onGround
        }
    }
}