package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.module.Module
import java.lang.IllegalArgumentException

class AutoDisableCommand : Command("autodisable", "ad") {
    override fun execute(args: Array<String>) {
        if (args.size < 2) {
            chat(".autodisable <Module> <WorldChange/Dead/LagBack/None>")
            return
        }
        if (args.size == 2) {
            val module = LiquidBounce.moduleManager.getModule(args[1])

            if (module == null) {
                chat("Module not found.")
                return
            }

            module.autoDisableMode = Module.AutoDisableMode.NONE
            chat("The auto disable mode of this module is:" + module.autoDisableMode.name)
        } else {
            val module = LiquidBounce.moduleManager.getModule(args[1])

            if (module == null) {
                chat("Module not found.")
                return
            }

            val mode: Module.AutoDisableMode
            try {
                mode = Module.AutoDisableMode.valueOf(args[2].toUpperCase())
            } catch (e: IllegalArgumentException) {
                chat("The auto disable mode not found.")
                return
            }
            module.autoDisableMode = mode
            chat("The auto disable mode of this module is set to:" + mode.name)
        }
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        val moduleName = args[0]

        return when (args.size) {
            1 -> LiquidBounce.moduleManager.modules
                .map { it.name }
                .filter { it.startsWith(moduleName, true) }
                .toList()
            2 -> Module.AutoDisableMode.values()
                .map { it.name }
                .filter { it.startsWith(args[1], true) }
                .toList()
            else -> emptyList()
        }
    }
}