//===============================================//
//重新编译已禁用.请用JDK运行Recaf//
//===============================================//

// Decompiled with: CFR 0.152
// Class Version: 8
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
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class FloatSetting
        extends Downward<FloatValue> {
    private float modulex;
    private float moduley;
    private float numbery;
    public float percent = 0.0f;
    private boolean iloveyou;

    public FloatSetting(FloatValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    @Override
    public void draw(int mouseX, int mouseY) {
        this.modulex = LiquidBounce.INSTANCE.getOtc().getMainx();
        this.moduley = LiquidBounce.INSTANCE.getOtc().getMainy();
        this.numbery = this.pos.y + (float)this.getScrollY();
        Minecraft.getMinecraft();
        double clamp = MathHelper.clamp((double)(Minecraft.getDebugFPS() / 30), (double)1.0, (double)9999.0);
        double percentBar = (((Float)((FloatValue)this.setting).get()).doubleValue() - (double)((FloatValue)this.setting).getMinimum()) / (double)(((FloatValue)this.setting).getMaximum() - ((FloatValue)this.setting).getMinimum());
        this.percent = Math.max(0.0f, Math.min(1.0f, (float)((double)this.percent + (Math.max(0.0, Math.min(percentBar, 1.0)) - (double)this.percent) * (0.2 / clamp))));
        RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f, 2.5f, 1.0f, new Color(34, 38, 48));
        RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f * this.percent, 2.5f, 1.0f, new Color((Integer)hud.getHudColor().get()));
        FontLoaders.C12.drawString((String) ((FloatValue)this.setting).getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.numbery + 8.0f, new Color(200, 200, 200).getRGB());
        if (this.iloveyou) {
            float percentt = Math.min(1.0f, Math.max(0.0f, ((float)mouseX - (this.modulex + 5.0f + this.pos.x + 55.0f)) / 99.0f * 1.3f));
            double newValue = percentt * (((FloatValue)this.setting).getMaximum() - ((FloatValue)this.setting).getMinimum()) + ((FloatValue)this.setting).getMinimum();
            double set = MathUtil.incValue(newValue, 0.1);
            ((FloatValue)this.setting).set(set);
        }
        ClickGUI cg = (ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class);
        if (this.iloveyou || this.isHovered(mouseX, mouseY) || ((Boolean)cg.disp.get()).booleanValue()) {
            RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f + 61.0f * this.percent, this.moduley + 17.0f + this.numbery + 8.0f + 6.0f, Fonts.font35.getStringWidth(((FloatValue)this.setting).get() + "") + 2, 6.0f, 1.0f, new Color(32, 34, 39));
            FontLoaders.C12.drawString((String) (((FloatValue)this.setting).get() + ""), this.modulex + 5.0f + this.pos.x + 55.0f + 62.0f * this.percent, this.moduley + 17.0f + this.numbery + 8.0f + 8.0f, new Color(250, 250, 250).getRGB());
        }
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f, 2.5f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, new Color((Integer)hud.getHudColor().get()).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0) {
            this.iloveyou = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.iloveyou = false;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 55.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 55.0f + 75.0f && (float)mouseY >= this.moduley + 17.0f + this.numbery + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.numbery + 8.0f + 2.5f;
    }
}
 