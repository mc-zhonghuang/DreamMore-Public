package net.ccbluex.liquidbounce.ui.client

import io.netty.buffer.Unpooled
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.rename.modules.misc.HYTParty
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.CPacketCustomPayload
import java.awt.Color

class ListSwitcher(val map: HashMap<String, String>): GuiScreen() {
    var pressed = false
    var id = ""

    override fun drawScreen(mouseX: Int, mouseY: Int, ticks: Float) {
        RenderUtils.drawRect(0F, 0F, width.toFloat(), height.toFloat(), Color(0, 0, 0, 50).rgb)
        val newWidth = width / 2 - 50
        val newHeight = height / 2 - (map.size * 10)
        RenderUtils.drawRect(newWidth.toDouble(), newHeight.toDouble(), newWidth + 100.0, newHeight + map.size * 20.0, Color(0, 0, 0, 200).rgb)
        var i = 0
        for (it in map) {
            Fonts.font30.drawString(it.key, newWidth, newHeight+i, Color(255, 255, 255, 200).rgb)
            if (mouseX in newWidth .. newWidth + 100 && mouseY in newHeight + i .. newHeight + i + 20 && pressed) {
                if (it.key.contains("输入")) {
                    id = it.value
                    LiquidBounce.commandManager.switcher = this
                    ClientUtils.info("请输入.khparty <输入的内容>来进行内容输入")
                    mc.displayGuiScreen(null)
                    return
                } else {
                    HYTParty.sendDebugPacket(listOf(it.value, "null", "button"))
                    val data = Unpooled.wrappedBuffer(HYTParty.encode(HYTParty.jsonCloseGUI))
                    Minecraft.getMinecraft().connection?.sendPacket(CPacketCustomPayload("VexView", PacketBuffer(data)))
                    mc.displayGuiScreen(null)
                }
                return
            }
            i+=20
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, ticks: Int) {
        pressed = true

        super.mouseClicked(mouseX, mouseY, ticks)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, ticks: Int) {
        pressed = false

        super.mouseReleased(mouseX, mouseY, ticks)
    }
}