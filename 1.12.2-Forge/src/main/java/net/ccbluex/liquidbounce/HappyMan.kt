package net.ccbluex.liquidbounce

import net.ccbluex.liquidbounce.LiquidBounce.invest
import net.ccbluex.liquidbounce.LiquidBounce.startClient
import net.ccbluex.liquidbounce.management.CombatManager
import net.ccbluex.liquidbounce.rename.verify.base.UserData
import net.ccbluex.liquidbounce.utils.AutoLMsg
import net.ccbluex.liquidbounce.utils.UUIDUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import top.fl0wowp4rty.MethodParameter
import top.fl0wowp4rty.Native
import java.net.HttpURLConnection
import java.net.URL
import java.util.HashMap

@Native
object HappyMan {
    @MethodParameter
    @JvmStatic
    fun start() {
        startClient()
    }

    @MethodParameter
    @JvmStatic
    fun fuck1(thiss: GuiScreen, i: Int, j: Int, k: Int) {
        thiss.eventButton = k
        thiss.lastMouseEvent = Minecraft.getSystemTime()
        if (invest != null) invest!!.mouseClicked(i, j, thiss.eventButton)
    }

    @MethodParameter
    @JvmStatic
    fun fuck2(thiss: GuiScreen, i: Int, j: Int, k: Int) {
        thiss.eventButton = -1
        if (invest != null) invest!!.mouseReleased(i, j, k)
    }

    @MethodParameter
    @JvmStatic
    fun fuck3(k: Int): Boolean {
        return k == 0 && AutoLMsg.getSwearing() && LiquidBounce.useIP
    }

    @MethodParameter
    fun fuck4() {
        if (!LiquidBounce.useIP || LiquidBounce.data == null || LiquidBounce.data!!.uuid.length >= 20 || !AutoLMsg.getSwearing()) Minecraft.getMinecraft().shutdown()
    }
}