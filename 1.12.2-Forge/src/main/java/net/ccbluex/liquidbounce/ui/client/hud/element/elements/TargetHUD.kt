/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.rename.modules.misc.blur.BlurBuffer
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.injection.backend.ExtractedFunctionsImpl
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ColorUtils
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * A target hud
 */
@ElementInfo(name = "TargetHud")
class TargetHUD : Element() {

    private val modeValue = ListValue("Style", arrayOf("LiquidWing", "LiquidBounce", "Astro", "WTF", "Nameless","Vape","Tenacity"), "LiquidBounce")

    private val blur =BoolValue("Blur",true)
    private val cfont = BoolValue("CFont", false)
    private val outline = BoolValue("Outline", false)

    private val decimalFormat = DecimalFormat("##0.00", DecimalFormatSymbols(Locale.ENGLISH))
    private val fadeSpeed = FloatValue("HP-FadeSpeed", 2F, 1F, 9F)

    private val colorRedValue = IntegerValue("R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("G", 111, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)
    private val colorRed2Value = IntegerValue("R2", 0, 0, 255)
    private val colorGreen2Value = IntegerValue("G2", 111, 0, 255)
    private val colorBlue2Value = IntegerValue("B2", 255, 0, 255)

    private var easingHealth: Float = 0F

    var fontRenderer = Fonts.font40

    var mainTarget: IEntityLivingBase? = null
    var animProgress = 0F

    fun calculateCompensation(target: Float, current: Float, delta: Long, speed: Int): Float {
        var current = current
        var delta = delta
        val diff = current - target
        if (delta < 1L) {
            delta = 1L
        }
        val xD: Double
        if (diff > speed.toFloat()) {
            xD =
                if ((speed.toLong() * delta / 16L).toDouble() < 0.25) 0.5 else (speed.toLong() * delta / 16L).toDouble()
            current = (current.toDouble() - xD).toFloat()
            if (current < target) {
                current = target
            }
        } else if (diff < (-speed).toFloat()) {
            xD =
                if ((speed.toLong() * delta / 16L).toDouble() < 0.25) 0.5 else (speed.toLong() * delta / 16L).toDouble()
            current = (current.toDouble() + xD).toFloat()
            if (current > target) {
                current = target
            }
        } else {
            current = target
        }
        return current
    }

    override fun drawElement(): Border {
        if (this.cfont.get())
            fontRenderer = Fonts.font35
        else
            fontRenderer = mc.fontRendererObj

        val actualTarget =
            (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target ?: if (classProvider.isGuiHudDesigner(
                    mc.currentScreen
                )
            ) mc.thePlayer
            else null

        var width = 80F
        if (this.modeValue.get().toLowerCase().equals("liquidwing")) {
            width = 150F
        } else if (this.modeValue.get().toLowerCase().equals("astro")) {
            width = 90F
        } else if (this.modeValue.get().toLowerCase().equals("wtf")) {
            width = 100F
        } else if (this.modeValue.get().toLowerCase().equals("nameless")) {
            width = 100F
        } else if (this.modeValue.get().toLowerCase().equals("vape")) {
            width = 90F
        } else if (this.modeValue.get().toLowerCase().equals("Tenacity")) {
            width = 150F
        } else {
            width = 128F
        }
        var height = 34F
        if (this.modeValue.get().toLowerCase().equals("liquidwing")) {
            height = 40F
        } else if (this.modeValue.get().toLowerCase().equals("astro")) {
            height = 40F
        } else if (this.modeValue.get().toLowerCase().equals("wtf")) {
            height = 44F
        } else if (this.modeValue.get().toLowerCase().equals("nameless")) {
            height = 36F
        } else if (this.modeValue.get().toLowerCase().equals("vape")) {
            height = 36F
        } else if (this.modeValue.get().toLowerCase().equals("Tenacity")) {
            height = 36F
        } else {
            height = 36F
        }

        // animProgress += ((1 - animProgress) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime * if (actualTarget != null) -1F else 1F

        val num = if (actualTarget != null) 0F else 1F;
        animProgress += ((num - animProgress) / 2.0F.pow(10.0F - 5.50f)) * RenderUtils.deltaTime

        animProgress = animProgress.coerceIn(0F, 1F)

        if (actualTarget != null)
            mainTarget = actualTarget
        else if (animProgress >= 1F) {
            mainTarget = null
        }

        if (mainTarget == null) {
            easingHealth = 0F
            return Border(0F, 0F, width, height)
        }

        if (this.modeValue.get().toLowerCase().equals("liquidwing")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(80)
                .toFloat()
        } else if (this.modeValue.get().toLowerCase().equals("astro")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(90)
                .toFloat()
        } else if (this.modeValue.get().toLowerCase().equals("wtf")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(100)
                .toFloat()
        } else if (this.modeValue.get().toLowerCase().equals("nameless")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(100)
                .toFloat()
        } else if (this.modeValue.get().toLowerCase().equals("vape")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(100)
                .toFloat()
        } else if (this.modeValue.get().toLowerCase().equals("Tenacity")) {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(100)
                .toFloat()
        } else {
            width = (38 + (mainTarget!!.name?.let(mc.fontRendererObj::getStringWidth) ?: 0))
                .coerceAtLeast(100)
                .toFloat()
        }
        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.blurArea(-renderX.toFloat(), -renderY.toFloat(), width, height)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }

        val calcScaleX = animProgress
        val calcScaleY = animProgress
        val calcTranslateX = width / 2F * calcScaleX
        val calcTranslateY = height / 2F * calcScaleY

        GL11.glPushMatrix()
        GL11.glTranslatef(calcTranslateX, calcTranslateY, 0F)
        GL11.glScalef(1F - calcScaleX, 1F - calcScaleY, 1F - calcScaleX)
        if (this.modeValue.get().toLowerCase().equals("astro")) {
            this.astro(mainTarget!!, width, height)
        } else if (this.modeValue.get().toLowerCase().equals("wtf")) {
            this.wtf(mainTarget!!, width, height)
        } else if (this.modeValue.get().toLowerCase().equals("nameless")) {
            this.nameless(mainTarget!!, width, height)
        } else if (this.modeValue.get().toLowerCase().equals("vape")) {
            this.vape(mainTarget!!, width, height)
        } else if (this.modeValue.get().toLowerCase().equals("Tenacity")) {
            this.Tenacity(mainTarget!!, width, height)
        } else {
            this.liquidbounce(mainTarget!!, width, height)
        }

        GL11.glPopMatrix()

        GlStateManager.resetColor()
        return Border(0F, 0F, width, height)
    }
    private fun Tenacity(target: IEntityLivingBase, width: Float, height: Float) {
        if (target != null) {
            //
            val hudMod = LiquidBounce.moduleManager.getModule(
                HUD::class.java
            ) as HUD
            fun getClientColor(): Color {
                return Color(hudMod.r.get(), hudMod.g.get(), hudMod.b.get())
            }

            fun getAlternateClientColor(): Color {
                return Color(hudMod.r2.get(), hudMod.g2.get(), hudMod.b2.get())
            }
            //画主体
            BlurBuffer.CustomBlurRoundArea(0f,0f,width,height,4f,8f)
            //画玩家信息
            var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
            if (classProvider.isEntityPlayer(target)) {
                playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
            }
            if (playerInfo != null) {

                // Draw head
                val locationSkin = playerInfo.locationSkin

                val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) { Minecraft.getMinecraft().timer.renderPartialTicks } else { 0f }
                // 受伤的红色效果
                val hurtPercent = renderHurtTime / 10.0F
                GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)

                val scale = if (hurtPercent == 0f) { 1f } else if (hurtPercent < 0.5f) {
                    1 - (0.2f * hurtPercent * 2)
                } else {
                    0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
                }
                val size = 30

                GL11.glPushMatrix()
                // 受伤的缩放效果
                GL11.glScalef(scale, scale, scale)
                GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

                mc.textureManager.bindTexture(locationSkin)
                RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8,size, size,
                    64F, 64F)

                GL11.glPopMatrix()
            }

        }
    }
    private fun liquidbounce(target: IEntityLivingBase, width: Float, height: Float) {
        if (target != null) {
            if (easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }
            // Draw rect box
            RenderUtils.rectangle(0.0, 0.0, width.toDouble(), height.toDouble(), Color(0,0,0,70).rgb)

            var c = Color(this.colorRedValue.get(), this.colorGreenValue.get(), this.colorBlueValue.get())
            var c2 = Color(this.colorRed2Value.get(), this.colorGreen2Value.get(), this.colorBlue2Value.get())
            // Health bar
            //  RenderUtils.drawGradientSideways(0.0, height.toDouble() - 2.0, ((easingHealth / target.maxHealth) * width).toDouble(),
            //    height.toDouble(), c.darker().rgb, c.rgb)
            val startPos = 0.0
            val healthBar = width.toDouble()
            - startPos * 2.0
            RenderUtils.drawGradientSideways(startPos, height.toDouble() - 2.0, startPos + ((easingHealth / target.maxHealth) * (healthBar)).toDouble(),
                height.toDouble(), c2.rgb, c.rgb)

            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

            target.name?.let { fontRenderer.drawStringWithShadow(it, 36, 7, 0xffffff) }

            fontRenderer.drawStringWithShadow("Distance: ${decimalFormat.format(mc.thePlayer!!.getDistanceToEntityBox(target))}" , 36, 19, 0xffffff)

            // Draw info

            //绘制头像
            //如果target是玩家，将会绘制其头像。
            var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
            if (classProvider.isEntityPlayer(target)) {
                playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
            }
            if (playerInfo != null) {

                // Draw head
                val locationSkin = playerInfo.locationSkin

                val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) { Minecraft.getMinecraft().timer.renderPartialTicks } else { 0f }
                // 受伤的红色效果
                val hurtPercent = renderHurtTime / 10.0F
                GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)

                val scale = if (hurtPercent == 0f) { 1f } else if (hurtPercent < 0.5f) {
                    1 - (0.2f * hurtPercent * 2)
                } else {
                    0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
                }
                val size = 30

                GL11.glPushMatrix()
                // 受伤的缩放效果
                GL11.glScalef(scale, scale, scale)
                GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

                mc.textureManager.bindTexture(locationSkin)
                RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8,size, size,
                    64F, 64F)

                GL11.glPopMatrix()
            }
        }
    }

    private fun vape(target: IEntityLivingBase, width: Float, height: Float) {
        val font = Fonts.font35
        val color = ColorUtils.healthColor(target.health,target.maxHealth)
        val darkColor = ColorUtils.darker(color,0.6F)
        val healthPosition = 43F + ((target.health / target.maxHealth * 10000).roundToInt() / 168)
        RenderUtils.drawRect(0F,0F, 140F, 40F, Color(30,30,30, 230).rgb)
        RenderUtils.drawOutLineRect(2.0, 2.0, 40.0,38.0,0.5, Color(45,45,45,230).rgb, Color(86,86,86).rgb)
        RenderUtils.drawOutLineRect(43.0, 12.0, 103.0, 18.0, 0.5, Color(45,45,45,230).rgb, Color(86,86,86).rgb)
        font.drawStringWithShadow(target.name!!, 43, 2, Color.WHITE.rgb)
        ExtractedFunctionsImpl.enableStandardItemLighting()
        for(i in 0..4) {
            if(target.getEquipmentInSlot(i) != null)
                mc.renderItem.renderItemAndEffectIntoGUI(target.getEquipmentInSlot(i)!!, 43 + i * 20, 22)
            RenderUtils.drawOutLineRect(43.0 + i * 20.0, 22.0, 59.0 + i * 20.0, 38.0,0.5, Color(45,45,45,230).rgb, Color(86,86,86).rgb)
        }
        ExtractedFunctionsImpl.disableStandardItemLighting()
        GL11.glPushMatrix()
        GL11.glScalef(0.7F, 0.7F, 0.7F)
        font.drawStringWithShadow("${target.health.toInt()}hp", 150, 18, Color.WHITE.rgb)
        GL11.glPopMatrix()
        RenderUtils.drawEntityOnScreen(20, 35, 15, target)
        RenderUtils.drawRect(healthPosition, 13F, 43F + ((easingHealth / target.maxHealth * 10000).roundToInt() / 168), 17F, darkColor)
        easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime
        RenderUtils.drawRect(44F, 13F, healthPosition, 17F, color)
    }

    private fun astro(target: IEntityLivingBase, width: Float, height: Float) {
        if (target != null) {
            if (easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }

            var c = Color(this.colorRedValue.get(), this.colorGreenValue.get(), this.colorBlueValue.get())
            var c2 = Color(this.colorRed2Value.get(), this.colorGreen2Value.get(), this.colorBlue2Value.get())

            // Draw rect box

            //ColorUtils.reAlpha(c.rgb, 0.4F)
            RenderUtils.rectangle(0.0, 0.0, width.toDouble(), height.toDouble(), Color(0,0,0,120).rgb)

            RenderUtils.rectangle(2.0, height - 6.0, 2.0 + ((width - 4)).toDouble(),
                height - 2.00, Color(25,25,25,70).rgb)

            RenderUtils.drawGradientSideways(2.0, height - 6.0, 2.0 + ((easingHealth / target.maxHealth) * (width - 4)).toDouble(),
                height - 2.0, c2.rgb, c.rgb)

            // mc.fontRendererObj.drawCenteredString(((target.health * 10).toInt() / 10.0F).toString(), 34.0F + (width - 36) / 2.0F, 14.0F, ColorUtils.getHealthColor(easingHealth, target.maxHealth), true)
            // Health bar

            //RenderUtils.drawGradientSideways(0.0, 34.0, ((easingHealth / target.maxHealth) * width).toDouble(),
            //   36.0, ColorUtils.rainbow().darker().rgb, ColorUtils.rainbow().rgb)

            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

            target.name?.let { fontRenderer.drawStringWithShadow(it, 34, 5, 0xffffff) }

            // Draw info

            //绘制头像
            //如果target是玩家，将会绘制其头像。
            if (classProvider.isEntityPlayer(target)) {
                val playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
                if (playerInfo != null) {

                    // Draw head
                    val locationSkin = playerInfo.locationSkin
                    drawHead(locationSkin, 30, 30)
                }
                //如果target不是玩家，将会绘制你自己的头像。
            } else {
                val playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
                if (playerInfo != null) {

                    // Draw head
                    val locationSkin = playerInfo.locationSkin
                    drawHead(locationSkin, 30, 30)
                }
            }
        }
    }

    private fun wtf(target: IEntityLivingBase, width: Float, height: Float) {
        if (target != null) {
            if (easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }

            var c = Color(this.colorRedValue.get(), this.colorGreenValue.get(), this.colorBlueValue.get())
            var c2 = Color(this.colorRed2Value.get(), this.colorGreen2Value.get(), this.colorBlue2Value.get())

            // Draw rect box

            //ColorUtils.reAlpha(c.rgb, 0.4F)
            RenderUtils.rectangleBordered(0.0, 0.0, width.toDouble(), height.toDouble(), 1.0, Color(95,95,95,255).rgb, Color(0,0,0,255).rgb)
            RenderUtils.rectangleBordered(1.5, 1.5, width.toDouble() - 1.5, height.toDouble() - 1.5, 1.0, Color(45,45,45,255).rgb, Color(65,65,65,255).rgb)
            //RenderUtils.rectangleBordered(0.0, 0.0, width.toDouble(), height.toDouble(), 0.5, Color(0,0,0,60).rgb, Color(0,0,0,90).rgb)

            val startPos = 6.0
            val barWidth = width.toDouble() - startPos * 2.0
            RenderUtils.rectangle(startPos - 0.5, 15.5, (startPos + (barWidth))+0.5,
                26.5, Color(25,25,25,255).rgb)

            RenderUtils.drawGradientSideways(startPos, 16.0, startPos + ((easingHealth / target.maxHealth) * barWidth),
                26.0, c2.rgb, c.rgb)

            Fonts.font35.drawCenteredString(((target.health * 10).toInt() / 10.0F).toString() + " HP", width / 2F, 19.0F, -1, true)
            // Health bar

            //RenderUtils.drawGradientSideways(0.0, 34.0, ((easingHealth / target.maxHealth) * width).toDouble(),
            //   36.0, ColorUtils.rainbow().darker().rgb, ColorUtils.rainbow().rgb)

            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

            target.name?.let { fontRenderer.drawStringWithShadow(it, startPos.toInt() + 2, 6, 0xffffff) }

            fontRenderer.drawStringWithShadow("Distance: " + mc.thePlayer!!.getDistanceToEntity(target).toInt() + "m", startPos.toInt() + 2, 30, 0xffffff)
            // Draw info
        }
    }

    private fun nameless(target: IEntityLivingBase, width: Float, height: Float) {
        if (target != null) {
            if (easingHealth < 0 || easingHealth > target.maxHealth ||
                abs(easingHealth - target.health) < 0.01) {
                easingHealth = target.health
            }

            // Draw rect box
            RenderUtils.drawRoundedRect(0.0f, 0.0f, width, height, 2.4F, Color(0,0,0,100).rgb)

            var c = Color(this.colorRedValue.get(), this.colorGreenValue.get(), this.colorBlueValue.get())
            var c2 = Color(this.colorRed2Value.get(), this.colorGreen2Value.get(), this.colorBlue2Value.get())
            // Health bar
            //  RenderUtils.drawGradientSideways(0.0, height.toDouble() - 2.0, ((easingHealth / target.maxHealth) * width).toDouble(),
            //    height.toDouble(), c.darker().rgb, c.rgb)
            val startPos = 2.0
            val barWidth = width.toDouble() - startPos * 2.0

            //  RenderUtils.rectangle(startPos, height.toDouble() - 3.0, startPos + ((target.totalArmorValue / 20) * (barWidth)),
            //    height.toDouble() - 2.0, Color(55, 105, 255).rgb)


            RenderUtils.drawGradientSideways(startPos, height.toDouble() - 3.0, startPos + ((easingHealth / target.maxHealth) * (barWidth)),
                height.toDouble() - 2.0, c2.rgb, c.rgb)

            easingHealth += ((target.health - easingHealth) / 2.0F.pow(10.0F - fadeSpeed.get())) * RenderUtils.deltaTime

            target.name?.let { fontRenderer.drawStringWithShadow(it, 34, 7, 0xffffff) }

            fontRenderer.drawStringWithShadow("Distance: ${decimalFormat.format(mc.thePlayer!!.getDistanceToEntityBox(target))}" , 34, 19, 0xffffff)

            // Draw info

            //绘制头像
            //如果target是玩家，将会绘制其头像。
            var playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
            if (classProvider.isEntityPlayer(target)) {
                playerInfo = mc.netHandler.getPlayerInfo(target.uniqueID)
            }
            if (playerInfo != null) {

                // Draw head
                val locationSkin = playerInfo.locationSkin

                val renderHurtTime = target.hurtTime - if (target.hurtTime != 0) { Minecraft.getMinecraft().timer.renderPartialTicks } else { 0f }
                // 受伤的红色效果
                val hurtPercent = renderHurtTime / 10.0F
                GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)

                val scale = if (hurtPercent == 0f) { 1f } else if (hurtPercent < 0.5f) {
                    1 - (0.2f * hurtPercent * 2)
                } else {
                    0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
                }
                val size = 30

                GL11.glPushMatrix()
                // 受伤的缩放效果
                GL11.glScalef(scale, scale, scale)
                GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

                mc.textureManager.bindTexture(locationSkin)
                RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8,size, size,
                    64F, 64F)

                GL11.glPopMatrix()
            }
        }
    }

    private fun drawHead(skin: IResourceLocation, width: Int, height: Int) {

        mc.textureManager.bindTexture(skin)
        RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, width, height,
            64F, 64F)
    }

}