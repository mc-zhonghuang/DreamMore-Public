package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.api.enums.MaterialType
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.simple.impl.Fonts
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Armor")
class Armor(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F,
            side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    private val modeValue = ListValue("Alignment", arrayOf("Horizontal", "Vertical"), "Horizontal")
    val r = IntegerValue("Red", 255, 0, 255)
    val g = IntegerValue("Green", 255, 0, 255)
    val b = IntegerValue("Blue", 255, 0, 255)
    val a = IntegerValue("Alpha", 255, 0, 255)

    /**
     * Draw element
     */
    override fun drawElement(): Border {
//        val arrayList: ArrayList<ArmorUtils> = ArrayList<liying.utils.armorUtils>()

        if (mc.playerController.isNotCreative) {
            GL11.glPushMatrix()

            val renderItem = mc.renderItem
            val isInsideWater = mc.thePlayer!!.isInsideOfMaterial(classProvider.getMaterialEnum(MaterialType.WATER))
            val sb = Color(r.get(), g.get(), b.get(), a.get()).rgb
            var x = 1
            var y = if (isInsideWater) -10 else 0
            val gradientColor1 = Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get())
            val gradientColor2 = Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get())
            val gradientColor3 = Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get())
            val gradientColor4 = Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get())
            val mode = modeValue.get()
            // RenderUtils.drawRoundedRect(x-4f,-12f,75f,30f,4f, Color(0,0,0,50).rgb)
            RoundedUtil.drawRound(x-2f,-12f,75f,40f, CustomColor.ra.get(),
                Color(0,0,0,200))

            net.ccbluex.liquidbounce.ui.font.Fonts.flux.drawString("r",x.toFloat() + 14f,-8f,Color(255, 255, 255).rgb)

            Fonts.SF.SF_18.SF_18.drawString("Armor",x.toFloat() + 25f,-8f,sb)
            for (index in 3 downTo 0) {
                val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue
                val stack2 = mc2.player.inventory.armorInventory[index]

                renderItem.renderItemIntoGUI(stack, x, y)
                renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
                GlStateManager.pushMatrix();
                Fonts.SF.SF_15.SF_15.drawString((stack2.maxDamage - stack.itemDamage).toString(),x.toFloat()+2f,
                    y.toFloat()+15f+Fonts.SF.SF_15.SF_15.height, sb)
                GlStateManager.popMatrix();
                if (mode.equals("Horizontal", true))
                    x += 18
                else if (mode.equals("Vertical", true))
                    y += 18
            }

            classProvider.getGlStateManager().enableAlpha()
            classProvider.getGlStateManager().disableBlend()
            classProvider.getGlStateManager().disableLighting()
            classProvider.getGlStateManager().disableCull()
            GL11.glPopMatrix()
        }

        return if (modeValue.get().equals("Horizontal", true))
            Border(0F, 0F, 72F, 17F)
        else
            Border(0F, 0F, 18F, 72F)
    }
}