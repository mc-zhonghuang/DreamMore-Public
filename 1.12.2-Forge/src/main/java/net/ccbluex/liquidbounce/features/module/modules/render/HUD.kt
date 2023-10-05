package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ColorValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.renderer.GlStateManager
import top.fl0wowp4rty.MethodParameter
import java.awt.Color
import kotlin.math.hypot
import kotlin.math.roundToLong


@ModuleInfo(
    name = "HUD",
    description = "Toggles visibility of the HUD.",
    category = ModuleCategory.RENDER,
    array = false
)
class HUD : Module() {
    var HudColor = ColorValue("HudColor", Color(121, 106, 229).rgb)
    private val hotbar = BoolValue("InGameGui", false)
    val fontChatValue = BoolValue("FontChat", false)
    val blurStrength = IntegerValue("GlobalBlurStrength", 1, 1, 20)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val blurValue = BoolValue("GuiBlur", false)
    val r = IntegerValue("Red", 255, 0, 255)
    val g = IntegerValue("Green", 255, 0, 255)
    val b = IntegerValue("Blue", 255, 0, 255)
    val r2 = IntegerValue("Red2", 255, 0, 255)
    val g2 = IntegerValue("Green2", 255, 0, 255)
    val b2 = IntegerValue("Blue2", 255, 0, 255)
    val Radius = IntegerValue("BlurRadius", 10 , 1 , 50 )
    private val bottomLeftText: MutableMap<String, String> = LinkedHashMap()
    fun getHotbar(): BoolValue {
        return hotbar
    }
    private fun getClientColor(hud: HUD): Color {
        return Color(CustomColor.r.get(), CustomColor.g.get(), CustomColor.b.get())
    }

    private fun getAlternateClientColor(hud: HUD): Color {
        return Color(CustomColor.r2.get(), CustomColor.g2.get(), CustomColor.b2.get())
    }

    private fun getClientColors(hud: HUD): Array<Color> {
        val firstColor: Color = mixColors(
            getClientColor(hud),
            getAlternateClientColor(hud)
        )
        val secondColor: Color = mixColors(
            getAlternateClientColor(hud),
            getClientColor(hud)
        )
        return arrayOf(firstColor, secondColor)
    }

    @EventTarget
    fun shader(event: BlurEvent) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return
        draw()
    }

    private fun draw() {
        GlStateManager.resetColor()
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return

        LiquidBounce.hud.render(false)

        draw()
    }


    private fun mixColors(color1: Color, color2: Color): Color {
        return ColorUtil.interpolateColorsBackAndForth(
            15,
            1,
            color1,
            color2,
                CustomColor.hueInterpolation.get()
        )
    }

    private fun calculateBPS(): Double {
        val bps = hypot(
            mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
            mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
        ) * mc.timer.timerSpeed * 20
        return (bps * 100.0).roundToLong() / 100.0
    }


    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
            !(classProvider.isGuiChat(event.guiScreen) || classProvider.isGuiHudDesigner(event.guiScreen))
        ) mc.entityRenderer.loadShader(classProvider.createResourceLocation("more" + "/blur.json")) else if (mc.entityRenderer.shaderGroup != null &&
            mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("more/blur.json")
        ) mc.entityRenderer.stopUseShader()
    }

    init {
        state = true
    }
}