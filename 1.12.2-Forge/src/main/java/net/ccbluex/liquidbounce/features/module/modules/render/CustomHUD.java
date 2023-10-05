package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.rename.modules.misc.blur.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "CustomHUD" , description = "CustomHUD." , category = ModuleCategory.RENDER)
public class CustomHUD extends Module {
    public static ListValue gsValue = new ListValue("NameMode", new String[]{"None","WaterMark"}, "WaterMark");
    public static final TextValue domainValue = new TextValue("Domain", "More");
    public static final BoolValue ChineseScore = new BoolValue("ChineseScore", false);
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (LiquidBounce.INSTANCE.getHeight() != -14){
            //  RenderUtils.drawRect(8,scaledResolution.getScaledHeight() - ((LiquidBounce.INSTANCE.getHeight() + 30)),348,scaledResolution.getScaledHeight() - ((LiquidBounce.INSTANCE.getHeight() + 30)) + LiquidBounce.INSTANCE.getHeight() + 10,new Color(20,20,20,100));
            RoundedUtil.drawGradientRound(6,scaledResolution.getScaledHeight() - ((LiquidBounce.INSTANCE.getHeight() + 30)),340,
                    LiquidBounce.INSTANCE.getHeight() + 10,CustomColor.ra.get(),
                    ColorUtil.applyOpacity(new Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get()), .85f),
                    new Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get()),
                    new Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get()),
                    new Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get()));
            if (CustomBlur.chat.get()) {
                GL11.glTranslated(0.0, 0.0, 0.3);
                BlurBuffer.blurRoundArea(
                        5,scaledResolution.getScaledHeight() - ((LiquidBounce.INSTANCE.getHeight() + 32)),342,
                        LiquidBounce.INSTANCE.getHeight() + 12,CustomBlur.radius.get());
                GL11.glTranslated(0.0, 0.0, 0.3);
            }
        }
        if (gsValue.get().equals("WaterMark")) {
            String text = "More" + " | " + LiquidBounce.CLIENT_VERSION + " | " + "Fps:" + Minecraft.getDebugFPS();
            RoundedUtil.drawGradientRound(3, 4, Fonts.font40.getStringWidth(text) + 4, 15,CustomColor.ra.get(),
                    ColorUtil.applyOpacity(new Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get()), .85f),
                    new Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get()),
                    new Color(CustomColor.r2.get(),CustomColor.g2.get(),CustomColor.b2.get(),CustomColor.a2.get()),
                    new Color(CustomColor.r.get(),CustomColor.g.get(),CustomColor.b.get(),CustomColor.a.get()));
            Fonts.font40.drawString(text, 5, 8, new Color(255, 255, 255, 255).getRGB());
        }
    }
}
