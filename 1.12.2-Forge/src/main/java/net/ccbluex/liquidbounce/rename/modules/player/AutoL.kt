package net.ccbluex.liquidbounce.rename.modules.player

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.AutoLMsg
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import java.util.*

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "AutoL", description = "AutoL. ", category = ModuleCategory.PLAYER)
class AutoL : Module() {
    val modeValue = ListValue("Mode", arrayOf("Chinese", "English","zhuboMessage","yurluMessage","YuJiangJun","Ikun", "L","None","Text"), "None")
    val lobbyValue = TextValue("Text", "More 2022/Genuine edition")
    private val prefix = BoolValue("@",true)
    private val delay = IntegerValue("Delay",100,0,2000)
    var index = 0
    var R = Random()
    var abuse = arrayOf("More 2022/Genuine edition")
    var englishabuse = arrayOf("You are loser")
    private var target: IEntity? = null
    var kill = 0
    val msTimer = MSTimer()
    fun AutoL() {
        state = true
    }
    @EventTarget
    fun onAttack(event: AttackEvent) {
        target = event.targetEntity
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (target != null) {
            if (target!!.isDead) {
                if (msTimer.hasTimePassed(delay.get().toLong())) {
                    index ++
                    when (modeValue.get()) {
                        "Chinese" -> {
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "")+":"+ abuse[R.nextInt(abuse.size)]
                            )
                            kill += 1
                            target = null
                        }
                        "YuJiangJun" -> {
                            if (index > AutoLMsg.yuJiangJun.size) index = 0
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "") + " " + AutoLMsg.yuJiangJun[index])
                            kill += 1
                            target = null
                        }
                        "zhuboMessage" -> {
                            if (index > AutoLMsg.zhuboMessage.size) index = 0
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "") + " " + AutoLMsg.zhuboMessage[index])
                            kill += 1
                            target = null
                        }
                        "yurluMessage" -> {
                            if (index > AutoLMsg.yurluMessage.size) index = 0
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "") + " " + AutoLMsg.yurluMessage[index])
                            kill += 1
                            target = null
                        }
                        "Ikun" -> {
                            if (index > AutoLMsg.ikun.size) index = 0
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "") + " " + AutoLMsg.ikun[index])
                            kill += 1
                            target = null
                        }
                        "English" -> {
                            kill += 1
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "") + "  " + englishabuse[R.nextInt(
                                englishabuse.size
                            )]
                            )
                            target = null
                        }
                        "L" -> {
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "")+" L " + target!!.name)
                            kill += 1
                            target = null
                        }
                        "None" -> {
                            kill += 1
                            target = null
                        }
                        "Text" -> {
                            mc.thePlayer!!.sendChatMessage((if (prefix.get()) "@" else "")+lobbyValue.get()+" [" + target!!.name+"]")
                            kill += 1
                            target = null
                        }
                    }
                    msTimer.reset()
                }
            }
        }
    }
    override val tag: String
        get() = "Kills%$kill"
}