package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Settings;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.ModuleRender;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TextSetting
        extends Downward<TextValue> {
    public TextValue textValue;
    HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    private float modulex;
    private float moduley;
    private float texty;

    public TextSetting(TextValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.textValue = s;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.modulex = LiquidBounce.INSTANCE.getOtc().getMainy();
        this.moduley = LiquidBounce.INSTANCE.getOtc().getMainx();
        this.texty = this.pos.y + (float) this.getScrollY();
        RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.texty + 8.0f, 75.0f, 11.0f, 1.0f, new Color(59, 63, 72).getRGB(), 1.0f, this.textValue.getTextHovered() ? new Color(hud.getHudColor().get()).getRGB() : new Color(85, 90, 96).getRGB());
        if (this.isHovered(mouseX, mouseY) && !this.textValue.getTextHovered()) {
            RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.texty + 8.0f, 75.0f, 11.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, new Color(hud.getHudColor().get()).getRGB());
        }
        FontLoaders.C12.drawString(this.textValue.getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        if (FontLoaders.C12.getStringWidth(this.textValue.getTextHovered() ? this.textValue.get() + "_" : this.textValue.get()) > 70) {
            FontLoaders.C12.drawString(FontLoaders.F16.trimStringToWidth(this.textValue.getTextHovered() ? this.textValue.get() + "_" : this.textValue.get(), 78, true), this.modulex + 5.0f + this.pos.x + 57.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        } else if (this.textValue.get().isEmpty() && !this.textValue.getTextHovered()) {
            FontLoaders.C12.drawString("Type Here...", this.modulex + 5.0f + this.pos.x + 57.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        } else {
            FontLoaders.C12.drawString(this.textValue.getTextHovered() ? this.textValue.get() + "_" : (String) this.textValue.get(), this.modulex + 5.0f + this.pos.x + 57.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float) mouseX >= this.modulex + 5.0f + this.pos.x + 55.0f && (float) mouseX <= this.modulex + 5.0f + this.pos.x + 55.0f + 75.0f && (float) mouseY >= this.moduley + 17.0f + this.texty + 8.0f && (float) mouseY <= this.moduley + 17.0f + this.texty + 8.0f + 11.0f;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            this.textValue.setTextHovered(!this.textValue.getTextHovered());
        } else if (this.textValue.getTextHovered()) {
            this.textValue.setTextHovered(false);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.textValue.getTextHovered()) {
            if (keyCode == 1) {
                this.textValue.setTextHovered(false);
            } else if (keyCode != 14 && keyCode != 157 && keyCode != 29 && keyCode != 54 && keyCode != 42 && keyCode != 15 && keyCode != 58 && keyCode != 211 && keyCode != 199 && keyCode != 210 && keyCode != 200 && keyCode != 208 && keyCode != 205 && keyCode != 203 && keyCode != 56 && keyCode != 184 && keyCode != 197 && keyCode != 70 && keyCode != 207 && keyCode != 201 && keyCode != 209 && keyCode != 221 && keyCode != 59 && keyCode != 60 && keyCode != 61 && keyCode != 62 && keyCode != 63 && keyCode != 64 && keyCode != 65 && keyCode != 66 && keyCode != 67 && keyCode != 68 && keyCode != 87 && keyCode != 88) {
                this.textValue.append(Character.valueOf(typedChar));
            }
            if (this.setting.getTextHovered() && Keyboard.isKeyDown(14) && this.textValue.get().length() >= 1) {
                this.textValue.set(this.textValue.get().substring(0, this.textValue.get().length() - 1));
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
 