package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Settings;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Utils.MathUtil;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class NumberSetting extends Downward<IntegerValue> {

    public NumberSetting(IntegerValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    //获取主界面xy跟随移动
    private float modulex,moduley,numbery;

    public float percent = 0;

    //anti-bug
    private boolean iloveyou;

    @Override
    public void draw(int mouseX, int mouseY) {
        modulex = LiquidBounce.INSTANCE.getOtc().getMainx();
        moduley = LiquidBounce.INSTANCE.getOtc().getMainy();

        //修复滚轮
        numbery = pos.y + getScrollY();

        double clamp = MathHelper.clamp(Minecraft.getDebugFPS() / 30, 1, 9999);
        double percentBar = (((Integer)((IntegerValue)this.setting).get()).doubleValue() - (double)((IntegerValue)this.setting).getMinimum()) / (double)(((IntegerValue)this.setting).getMaximum() - ((IntegerValue)this.setting).getMinimum());

        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        percent = Math.max(0, Math.min(1, (float) (percent + (Math.max(0, Math.min(percentBar, 1)) - percent) * (0.2 / clamp))));
        //V2 Style
        //背景
        RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75,2.5f,1, new Color(34,38,48));
        //value大小线条
        RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75 * percent,2.5f,1, new Color(hud.getHudColor().get()));
        //Value名字显示
        FontLoaders.C12.drawString(setting.getName(),modulex+ 5+ pos.x + 4 ,moduley+ 17 +numbery + 8,new Color(200,200,200).getRGB());

        //设置Value
        if (this.iloveyou) {
            float percentt = Math.min(1.0f, Math.max(0.0f, ((float)mouseX - (this.modulex + 5.0f + this.pos.x + 55.0f)) / 99.0f * 1.3f));
            double newValue = percentt * (float)(((IntegerValue)this.setting).getMaximum() - ((IntegerValue)this.setting).getMinimum()) + (float)((IntegerValue)this.setting).getMinimum();
            double set = MathUtil.incValue(newValue, 1.0);
            ((IntegerValue)this.setting).set(set);
        }

        //绘制文字框
        ClickGUI cg = (ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class);
        if (this.iloveyou || this.isHovered(mouseX, mouseY) || ((Boolean)cg.disp.get()).booleanValue()) {
            RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f + 61.0f * this.percent, this.moduley + 17.0f + this.numbery + 8.0f + 6.0f, Fonts.font35.getStringWidth(((IntegerValue)this.setting).get() + "") + 2, 6.0f, 1.0f, new Color(32, 34, 39));
            FontLoaders.C12.drawString((String) (((IntegerValue)this.setting).get() + ""), this.modulex + 5.0f + this.pos.x + 55.0f + 62.0f * this.percent, this.moduley + 17.0f + this.numbery + 8.0f + 8.0f, new Color(250, 250, 250).getRGB());
        }
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f, 2.5f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f,new Color((Integer)hud.getHudColor().get()).getRGB());
        }
        //V3 Style
    //    RoundedUtil.drawRound(modulex+ 5+ pos.x + 55 ,moduley+ 17 +numbery + 8,75,2.5f,1, new Color(HUD.Hudcolor.getVaule()));
     //   Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(setting.getName(),modulex+ 5+ pos.x + 4 ,moduley+ 17 +numbery + 8,new Color(200,200,200).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                iloveyou = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) iloveyou = false;
    }

    //如果指针在线条上
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=modulex+ 5+ pos.x + 55 && mouseX <= modulex+ 5+ pos.x + 55 + 75 && mouseY >= moduley+ 17 +numbery + 8 && mouseY <= moduley+ 17 +numbery + 8 + 2.5f;
    }
}
