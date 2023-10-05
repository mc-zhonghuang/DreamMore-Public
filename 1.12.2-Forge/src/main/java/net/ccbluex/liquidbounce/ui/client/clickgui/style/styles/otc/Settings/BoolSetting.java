package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Settings;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.ModuleRender;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

import java.awt.*;

public class BoolSetting
        extends Downward<BoolValue> {
    private float modulex;
    private float moduley;
    private float booly;

    public BoolSetting(BoolValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    @Override
    public void draw(int mouseX, int mouseY) {
        this.modulex = LiquidBounce.INSTANCE.getOtc().getMainx();
        this.moduley = LiquidBounce.INSTANCE.getOtc().getMainy();
        this.booly = this.pos.y + (float)this.getScrollY();
        RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.booly + 8.0f, 7.0f, 7.0f, 1.0f, (Boolean)((BoolValue)this.setting).get() != false ? new Color(86, 94, 115).getRGB() : new Color(50, 54, 65).getRGB(), 1.0f, (Boolean)((BoolValue)this.setting).get() != false ? new Color(86, 94, 115).getRGB() : new Color(85, 90, 96).getRGB());
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.booly + 8.0f, 7.0f, 7.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, new Color((Integer)hud.getHudColor().get()).getRGB());
        }
        FontLoaders.C12.drawString((String) ((BoolValue)this.setting).getName(), this.modulex + 5.0f + this.pos.x + 4.0f + 10.0f, this.moduley + 17.0f + this.booly + 8.0f + 3.0f, new Color(200, 200, 200).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            ((BoolValue)this.setting).toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 4.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 4.0f + 135.0f - 128.0f && (float)mouseY >= this.moduley + 17.0f + this.booly + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.booly + 8.0f + 7.0f;
    }
}
 