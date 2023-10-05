
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 0.0, y: Double = 0.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {
    /**
     * Example notification for CustomHUD designer
     */

    private val exampleNotification = Notification("Notification", "This is example", NotifyType.INFO)
    /**
     * Draw element
     */
    override fun drawElement(): Border? {

        val notifications = mutableListOf<Notification>()
        for((index, notify) in LiquidBounce.hud.notifications.withIndex()){
            GL11.glPushMatrix()

            if(notify.drawNotification(index,this.renderX.toFloat(), this.renderY.toFloat(),scale, this)){
                notifications.add(notify)
            }

            GL11.glPopMatrix()
        }
        for(notify in notifications){
            LiquidBounce.hud.notifications.remove(notify)
        }

        if (classProvider.isGuiHudDesigner(mc.currentScreen)) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()

            return Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(),0F,0F)
        }

        return null
    }
    fun drawBoarderBlur() {}

}


class Notification(val title: String, val content: String, val type: NotifyType, val time: Int=1000, val animeTime: Int=350) {
    private var s: String? = null
    var n2: Int = Fonts.tenacitybold35.getStringWidth(content)
    var textLength = Math.max(n2 as Int, 0 as Int)
    val width=this.textLength.toFloat() + 80.0f
    val height=30
    var fadeState = FadeState.IN
    var nowY=-height
    var x = 0F
    var displayTime=System.currentTimeMillis()
    var animeXTime=System.currentTimeMillis()
    var animeYTime=System.currentTimeMillis()



    /**
     * Draw notification
     */
    fun drawNotification(index: Int, blurRadius: Float, y: Float, scale: Float,notifications: Notifications):Boolean {
        val renderX:Double = notifications.renderX;
        val renderY:Double = notifications.renderY;
        val realY=-(index+1) * (height + 2)

        val nowTime=System.currentTimeMillis()

        var transY=nowY.toDouble()
        //Y-Axis Animation
        if(nowY!=realY){
            var pct=(nowTime-animeYTime)/animeTime.toDouble()
            if(pct>1){
                nowY=realY
                pct=1.0
            }else{
                pct= EaseUtils.easeOutQuart(pct)
            }
            GL11.glTranslated(0.0,(realY-nowY)*pct,0.0)
        }else{
            animeYTime=nowTime

        }
        GL11.glTranslated(1.0,nowY.toDouble(),0.0)

        //X-Axis Animation
        var pct=(nowTime-animeXTime)/animeTime.toDouble()
        when(fadeState){
            FadeState.IN -> {
                if(pct>1){
                    fadeState= FadeState.STAY
                    animeXTime=nowTime
                    pct=1.0
                }
                pct= EaseUtils.easeOutQuart(pct)
                transY+=(realY-nowY)*pct
            }

            FadeState.STAY -> {
                pct=1.0
                if((nowTime-animeXTime)>time){
                    fadeState= FadeState.OUT
                    animeXTime=nowTime
                }
            }

            FadeState.OUT -> {
                if(pct>1){
                    fadeState= FadeState.END
                    animeXTime=nowTime
                    pct=2.0
                }
                pct=1- EaseUtils.easeInQuart(pct)
            }

            FadeState.END -> {
                return true
            }
        }

        GL11.glTranslated(width-(width*pct),0.0,0.0)
        GL11.glTranslatef(-width.toFloat(),0F,0F)
        //动态显示
        // RenderUtils.drawRect(-22F, height - 2F, max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)), -22F), height.toFloat(), type.renderColor)
        //RoundedUtil.drawRound(-22F,0F,max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)), 0F),28F,CustomColor.ra.get(), Color(255,255,255))
        //绘制模糊
        //BlurBuffer.blurRoundArea(44F,-2F,width,28F,0)
        //绘制通知
        if (type == NotifyType.SUCCESS)
            s = "SUCCESS";
        else if (type == NotifyType.ERROR)
            s = "ERROR";
        else if (type == NotifyType.WARNING)
            s = "WARNING";
        else if ( type == NotifyType.INFO)
            s = "INFO";
        //分段绘制
        if (s == "INFO") {
            RoundedUtil.drawRound(14F,-2F,width - 15F,28F,CustomColor.ra.get(), Color(0 ,0, 0,150))
            Fonts.notificationIcon80.drawString("C",23F,5F,Color(224,194,30,255).rgb)
            Fonts.tenacitybold35.drawString(title,62f,3f,Color(255,255,255,240).rgb)
            Fonts.font30.drawString(content + " (" + BigDecimal(((time - time * ((nowTime - displayTime) / (animeTime * 2F + time))) / 1000).toDouble()).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "s)", 48f,16f, Color(255,255,255,255).rgb)
        }
        if (s == "WARNING") {
            RoundedUtil.drawRound(14F,-2F,width - 15F,28F,CustomColor.ra.get(), Color(0 ,0, 0,150))
            Fonts.notificationIcon80.drawString("D",23F,5F,Color(224,194,30,255).rgb)
            Fonts.tenacitybold35.drawString(title,62f,3f,Color(255,255,255,240).rgb)
            Fonts.font30.drawString(content + " (" + BigDecimal(((time - time * ((nowTime - displayTime) / (animeTime * 2F + time))) / 1000).toDouble()).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "s)", 48f,16f, Color(255,255,255,255).rgb)
        }
        if (s == "SUCCESS") {
            RoundedUtil.drawRound(14F,-2F,width - 15F,28F,CustomColor.ra.get(), Color(0 ,0, 0,150))
            Fonts.notificationIcon80.drawString("A",23F,5F,Color(0 ,215, 248,200).rgb)
            Fonts.tenacitybold35.drawString(title,62f,3f,Color(255,255,255,240).rgb)
            Fonts.font30.drawString(content + " (" + BigDecimal(((time - time * ((nowTime - displayTime) / (animeTime * 2F + time))) / 1000).toDouble()).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "s)", 48f,16f, Color(255,255,255,255).rgb)
        }
        if (s == "ERROR") {
            RoundedUtil.drawRound(14F,-2F,width - 15F,28F,CustomColor.ra.get(),Color(0 ,0, 0,150))
            Fonts.notificationIcon80.drawString("B",23F,5F,Color(206,33,33,240).rgb)
            Fonts.tenacitybold35.drawString(title,62F,3f,Color(255,255,255,240).rgb)
            Fonts.font30.drawString(content + " (" + BigDecimal(((time - time * ((nowTime - displayTime) / (animeTime * 2F + time))) / 1000).toDouble()).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "s)", 48f,16f, Color(255,255,255,255).rgb)
        }

        return false
    }

}

enum class NotifyType(var renderColor: Color) {
    SUCCESS(Color(0 ,157, 255,240)),
    ERROR(Color(255,0,0,200)),
    WARNING(Color(0xF5FD00)),
    INFO(Color(0xF5FD00));
}


enum class FadeState { IN, STAY, OUT, END }
