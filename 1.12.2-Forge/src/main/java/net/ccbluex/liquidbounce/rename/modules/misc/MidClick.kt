package net.ccbluex.liquidbounce.rename.modules.misc

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import org.lwjgl.input.Mouse

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "MidClick", description = "Allows you to add a player as a friend by right clicking him.", category = ModuleCategory.MISC)
class MidClick : Module() {
    private var wasDown = false

    @EventTarget
    fun onRender(event: Render2DEvent?) {
        if (mc.currentScreen != null)
            return

        if (!wasDown && Mouse.isButtonDown(2)) {
            val entity = mc.objectMouseOver!!.entityHit

            if (classProvider.isEntityPlayer(entity)) {
                val playerName = stripColor(entity!!.name)
                val friendsConfig = LiquidBounce.fileManager.friendsConfig

                if (!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName)
                    LiquidBounce.fileManager.saveConfig(friendsConfig)
                    ClientUtils.displayChatMessage("§a§l$playerName§c was added to your friends.")
                } else {
                    friendsConfig.removeFriend(playerName)
                    LiquidBounce.fileManager.saveConfig(friendsConfig)
                    ClientUtils.displayChatMessage("§a§l$playerName§c was removed from your friends.")
                }

            } else
                ClientUtils.displayChatMessage("§c§lError: §aYou need to select a player.")
        }
        wasDown = Mouse.isButtonDown(2)
    }
}