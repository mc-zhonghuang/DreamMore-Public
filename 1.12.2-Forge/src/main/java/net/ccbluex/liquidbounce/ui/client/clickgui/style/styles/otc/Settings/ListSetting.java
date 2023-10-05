//===============================================//
//重新编译已禁用.请用JDK运行Recaf//
//===============================================//

// Decompiled with: CFR 0.152
// Class Version: 8
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Settings;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.ModuleRender;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.copy.Animation;
import net.ccbluex.liquidbounce.utils.copy.Direction;
import net.ccbluex.liquidbounce.utils.copy.impl.EaseInOutQuad;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ListSetting
        extends Downward {
    final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    private final Animation arrowAnimation = new EaseInOutQuad(250, 1.0, Direction.BACKWARDS);
    private final ListValue listValue;
    private float modulex;
    private float moduley;
    private float listy;

    public ListSetting(ListValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.listValue = s;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.modulex = LiquidBounce.INSTANCE.getOtc().getMainx();
        this.moduley = LiquidBounce.INSTANCE.getOtc().getMainy();
        this.listy = this.pos.y + (float) this.getScrollY();
        FontLoaders.C12.drawString(this.listValue.getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.listy + 13.0f, new Color(200, 200, 200).getRGB());
        RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 80.0f, this.moduley + 17.0f + this.listy + 8.0f, 50.0f, 11.0f, 1.0f, new Color(59, 63, 72).getRGB(), 1.0f, new Color(85, 90, 96).getRGB());
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtils.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 80.0f, this.moduley + 17.0f + this.listy + 8.0f, 50.0f, 11.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, new Color(hud.getHudColor().get()).getRGB());
        }
        FontLoaders.C12.drawString(this.listValue.get() + "", this.modulex + 5.0f + this.pos.x + 82.0f, this.moduley + 17.0f + this.listy + 13.0f, new Color(200, 200, 200).getRGB());
        this.arrowAnimation.setDirection(this.listValue.openList ? Direction.FORWARDS : Direction.BACKWARDS);
        RenderUtils.drawClickGuiArrow(this.modulex + 5.0f + this.pos.x + 123.5f, this.moduley + 17.0f + this.listy + 13.0f, 4.0f, this.arrowAnimation, new Color(222, 224, 236).getRGB());
        if (this.listValue.openList) {
            GL11.glTranslatef(0.0f, 0.0f, 2.0f);
            RenderUtils.drawBorderedRect(this.modulex + 5.0f + this.pos.x + 80.0f, this.moduley + 17.0f + this.listy + 8.0f + 13.0f, this.modulex + 5.0f + this.pos.x + 80.0f + 50.0f, this.moduley + 17.0f + this.listy + 8.0f + 13.0f + (float) this.listValue.getValues().length * 11.0f, 1.0f, new Color(85, 90, 96).getRGB(), new Color(59, 63, 72).getRGB());
            for (String option : this.listValue.getValues()) {
                FontLoaders.C12.drawString(option, this.modulex + 5.0f + this.pos.x + 82.0f, this.moduley + 17.0f + this.listy + 1.0f + 13.0f + 12.0f + (float) (this.listValue.getModeListNumber(option) * 11), option.equals(this.listValue.get()) ? new Color(hud.getHudColor().get()).getRGB() : new Color(200, 200, 200).getRGB());
            }
            GL11.glTranslatef(0.0f, 0.0f, -2.0f);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && this.isHovered(mouseX, mouseY)) {
            boolean bl = this.listValue.openList = !this.listValue.openList;
        }
        if (mouseButton == 0 && this.listValue.openList && (float) mouseX >= this.modulex + 5.0f + this.pos.x + 80.0f && (float) mouseX <= this.modulex + 5.0f + this.pos.x + 80.0f + 50.0f) {
            for (int i = 0; i < this.listValue.getValues().length; ++i) {
                int v = (int) (this.moduley + 17.0f + this.listy + 8.0f + 13.0f + (float) (i * 11));
                if (mouseY < v || mouseY > v + 11) continue;
                this.listValue.set(this.listValue.getModeGet(i));
                this.listValue.openList = false;
            }
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float) mouseX >= this.modulex + 5.0f + this.pos.x + 80.0f && (float) mouseX <= this.modulex + 5.0f + this.pos.x + 80.0f + 50.0f && (float) mouseY >= this.moduley + 17.0f + this.listy + 8.0f && (float) mouseY <= this.moduley + 17.0f + this.listy + 8.0f + 11.0f;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
 