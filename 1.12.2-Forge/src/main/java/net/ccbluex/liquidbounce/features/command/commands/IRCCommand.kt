package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.rename.verify.MoreIRC

class IRCCommand : Command("irc") {
    private fun toString(args: Array<String>): String {
        var textLatest = ""
        for (text in args) {
            textLatest += "$text "
        }
        textLatest.removeSuffix(" ")
        return textLatest
    }

    override fun execute(args: Array<String>) {
        if (args.size < 2) {
            chat(".irc <login/leave/chat>")
            return
        }
        when  (args[1].toLowerCase()) {
            "chat" -> MoreIRC.say(toString(args.drop(2).toTypedArray()))
        }
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return if (args.size == 1) arrayListOf("login", "leave").map { it }.filter { it.startsWith(args[0], ignoreCase = true) }.toList()
        else emptyList()
    }
}