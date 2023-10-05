/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import java.awt.Color

/**
 * CustomHUD Model element
 *
 * Draw mini figure of your character to the HUD
 */
@ElementInfo(name = "Inventory")
class Inventory(x: Double = 300.0, y: Double = 50.0) : Element(x, y) {

    /**
     * Draw element
     */
    private val rectValue = ListValue("RectMode", arrayOf("SB", "None"), "SB")
    override fun drawElement(): Border {
        var gradientColor1 = Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get())
        var gradientColor2 = Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get())
        var gradientColor3 = Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get())
        var gradientColor4 = Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get())
        val startY = -12.0;

        RoundedUtil.drawRound(0F, startY.toFloat(), 174F, 76F,CustomColor.ra.get(), Color(0, 0, 0, 200))
        Fonts.nbicon20.drawString("m", 53.0F, (startY + (if (this.rectValue.get().equals("SB") || this.rectValue.get().equals("None")) 7.0F else 4.0F)).toFloat(), Color(255, 255, 255, 255).rgb)

        Fonts.font40.drawString("Inventory", 65.0F, (startY + (if (this.rectValue.get().equals("SB") || this.rectValue.get().equals("None")) 6.0F else 4.0F)).toFloat(), Color(255, 255, 255, 255).rgb);
        // render item
        RenderHelper.enableGUIStandardItemLighting()
        renderInv(9, 17, 6, 6)
        renderInv(18, 26, 6, 24)
        renderInv(27, 35, 6, 42)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        return Border(0F, startY.toFloat(), 174F, 66F)
    }

    private fun renderInv(slot: Int, endSlot: Int, x: Int, y: Int) {
        var xOffset = x
        for (i in slot..endSlot) {
            xOffset += 18
            val stack = mc.thePlayer!!.inventoryContainer.getSlot(i).stack ?: continue

            mc.renderItem.renderItemAndEffectIntoGUI(stack, xOffset - 18, y)
            mc.renderItem.renderItemOverlays(Fonts.posterama30, stack, xOffset - 18, y)
        }
    }
}