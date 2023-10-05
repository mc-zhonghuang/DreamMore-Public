package net.ccbluex.liquidbounce.rename.modules.hyt

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import java.util.regex.Pattern

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "HytNoHurt", description = "How dare are you", category = ModuleCategory.HYT)
class HytNoHurt : Module() {
    private val debug = BoolValue("Debug", true)
    private val mode = ListValue("GetNameMode", arrayOf("4V4/1V1", "32/64", "16V16"), "4V4/1V1")

    override fun onDisable() {
        clearAll()
        super.onDisable()
    }
    override val tag: String
        get() = "Normal"
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (classProvider.isSPacketChat(packet)) {
            when (mode.get().toLowerCase()) {
                "4v4/1v1", "32/64" -> {
                    val matcher =
                            Pattern.compile("杀死了 (.*?)\\(").matcher(packet.asSPacketChat().chatComponent.unformattedText)
                    val matcher2 = Pattern.compile("起床战争>> (.*?) (\\((((.*?)死了!)))")
                            .matcher(packet.asSPacketChat().chatComponent.unformattedText)
                    if (matcher.find()) {
                        val name = matcher.group(1).trim()
                        if (name != "") {
                            LiquidBounce.fileManager.friendsConfig.addFriend(name)
                            if (debug.get()) {
                                ClientUtils.displayChatMessage("§8[§c§lMore§8]§c§d添加无敌人：" + name)
                            }
                            Thread {
                                try {
                                    Thread.sleep(5000)
                                    LiquidBounce.fileManager.friendsConfig.removeFriend(name)
                                    if (debug.get()) {
                                        ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d删除无敌人：" + name)
                                    }
                                } catch (ex: InterruptedException) {
                                    ex.printStackTrace()
                                }
                            }.start()
                        }
                    }
                    if (matcher2.find()) {
                        val name = matcher2.group(1).trim()
                        if (name != "") {
                            LiquidBounce.fileManager.friendsConfig.addFriend(name)
                            if (debug.get()) {
                                ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d添加无敌人：" + name)
                            }
                            Thread {
                                try {
                                    Thread.sleep(5000)
                                    LiquidBounce.fileManager.friendsConfig.removeFriend(name)
                                    if (debug.get()) {
                                        ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d删除无敌人：" + name)
                                    }
                                } catch (ex: InterruptedException) {
                                    ex.printStackTrace()
                                }
                            }.start()
                        }
                    }
                }

                "16v16" -> {
                    val matcher =
                            Pattern.compile("击败了 (.*?)!").matcher(packet.asSPacketChat().chatComponent.unformattedText)
                    val matcher2 =
                            Pattern.compile("玩家 (.*?)死了！").matcher(packet.asSPacketChat().chatComponent.unformattedText)
                    if (matcher.find()) {
                        val name = matcher.group(1).trim()
                        if (name != "") {
                            LiquidBounce.fileManager.friendsConfig.addFriend(name)
                            if (debug.get()) {
                                ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d添加无敌人：" + name)
                            }
                            Thread {
                                try {
                                    Thread.sleep(10000)
                                    LiquidBounce.fileManager.friendsConfig.removeFriend(name)
                                    if (debug.get()) {
                                        ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d删除无敌人：" + name)
                                    }
                                } catch (ex: InterruptedException) {
                                    ex.printStackTrace()
                                }
                            }.start()
                        }
                    }
                    if (matcher2.find()) {
                        val name = matcher2.group(1).trim()
                        if (name != "") {
                            LiquidBounce.fileManager.friendsConfig.addFriend(name)
                            if (debug.get()) {
                                ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d添加无敌人：" + name)
                            }
                            Thread {
                                try {
                                    Thread.sleep(10000)
                                    LiquidBounce.fileManager.friendsConfig.removeFriend(name)
                                    if (debug.get()) {
                                        ClientUtils.displayChatMessage("§8[§c§lMore提醒您§8]§c§d删除无敌人：" + name)
                                    }
                                } catch (ex: InterruptedException) {
                                    ex.printStackTrace()
                                }
                            }.start()
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent?) {
        clearAll()
    }

    private fun clearAll() {
        LiquidBounce.fileManager.friendsConfig.clearFriends()
    }
}