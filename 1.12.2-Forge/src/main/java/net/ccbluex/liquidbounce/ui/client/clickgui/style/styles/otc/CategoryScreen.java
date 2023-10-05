package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Utils.Position;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.MathUtils;
import net.ccbluex.liquidbounce.utils.copy.Animation;
import net.ccbluex.liquidbounce.utils.copy.Direction;
import net.ccbluex.liquidbounce.utils.copy.impl.SmoothStepAnimation;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CategoryScreen {

    private float maxScroll = Float.MAX_VALUE, minScroll = 0, rawScroll;

    private float scroll;

    public Position pos;

    private final ModuleCategory category;
    private float x;

    private float categoryX;
    private float categoryY;

    //选择大类
    private boolean selected;

    private final List<ModuleRender> moduleList = new CopyOnWriteArrayList<>();

    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);


    public CategoryScreen(ModuleCategory category, float x) {
        this.category = category;
        this.x = x;

        this.pos = new Position(0, 0, 0, 0);

        int count = 0;

        //new ly ry
        int leftAdd = 0;
        int rightAdd = 0;

        for (Module holder : LiquidBounce.moduleManager.getModules()) {
            Module module = holder;
            if (module.getCategory().equals(this.category)) {


                //奇偶判断
                float posWidth = 0;

                //判断左右分别添加
                float posX = pos.x + ((count % 2 == 0) ? 0 : 145);
                float posY = pos.y + ((count % 2 == 0) ? leftAdd : rightAdd);

                Position pos = new Position(posX, posY, posWidth, 30);
                //只需要x y height
                ModuleRender otlM = new ModuleRender(module, pos.x, pos.y, pos.width, pos.height);
                pos.height = otlM.height;
                if (count % 2 == 0) {
                    leftAdd += pos.height + 20;
                } else {
                    rightAdd += pos.height + 20;
                }
                moduleList.add(otlM);
                count++;
            }

        }
    }

    public String newcatename(ModuleCategory moduleCategory) {
        if (moduleCategory.name().equals("Combat")) {
            return "combat";
        } else if (moduleCategory.name().equals("Player")) {
            return "player";
        } else if (moduleCategory.name().equals("Movement")) {
            return "move";
        } else if (moduleCategory.name().equals("Render")) {
            return "visuals";
        } else if (moduleCategory.name().equals("World")) {
            return "world";
        } else if (moduleCategory.name().equals("Misc")) {
            return "misc";
        } else if (moduleCategory.name().equals("Exploit")) {
            return "exploit";
        } else if (moduleCategory.name().equals("Hyt")) {
            return "huayuting";
        }
        return "";
    }


    public void drawScreen(int mouseX, int mouseY) {
        categoryX = LiquidBounce.INSTANCE.getOtc().getMainx();
        categoryY = LiquidBounce.INSTANCE.getOtc().getMainy();


        if (selected) {

            double scrolll = getScroll();
            for (ModuleRender module : moduleList) {
                module.scrollY = (int) MathUtils.roundToHalf(scrolll);
            }

            onScroll(30);
            //判断
            maxScroll = Math.max(0, moduleList.get(moduleList.size() - 1).getY() + moduleList.get(moduleList.size() - 1).height * 2 + 15);
            //

        }

        FontLoaders.C12.drawString(newcatename(category),
                x + categoryX + 77, categoryY - 29, 0xFFFFFFFF);
        if (selected) {
            RoundedUtil.drawRound(x + categoryX + 76, categoryY - 29 - 3, FontLoaders.C12.getStringWidth(newcatename(category)) + 2, 5 + 4, 1, new Color(255, 255, 255, 60));
            //  RenderUtils.drawRect();
            //隐藏不可见的
            GL11.glPushMatrix();
            DrRenderUtils.scissor(0, LiquidBounce.INSTANCE.getOtc().getMainy(), 1920, 300);
            //  GL11.glScissor(0, (int) (LiquidBounce.otc.sHeight() - LiquidBounce.otc.getMainy() * 2  + 375 - LiquidBounce.otc.getHeight() * 2) + 40, 1920, LiquidBounce.otc.getHeight() * 2 - 42 -375 );
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            moduleList.stream() ////
                    .sorted((o1, o2) -> Boolean.compare(o1.isSelected(), o2.isSelected())) //
                    .forEach(module -> module.drawScreen(mouseX, mouseY));

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }


        if (isHovered(mouseX, mouseY)) {
            RoundedUtil.drawRound(x + categoryX + 76, categoryY - 29 - 3, FontLoaders.C12.getStringWidth(newcatename(category)) + 2, 5 + 4, 1, new Color(255, 255, 255, 60));
        }

    }

    public void onScroll(int ms) {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        rawScroll += Mouse.getDWheel() / 4f;
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }

    public float getScroll() {
        scroll = (float) (rawScroll - scrollAnimation.getOutput());
        return scroll;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x + categoryX + 76 && mouseX <= x + categoryX + 76 + FontLoaders.C12.getStringWidth(newcatename(category)) + 2 && mouseY >= categoryY - 29 - 3 && mouseY <= categoryY - 29 - 3 + 9;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        moduleList.forEach(s -> s.mouseClicked(mouseX, mouseY, mouseButton));

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        moduleList.forEach(e -> e.mouseReleased(mouseX, mouseY, state));
    }

    public void keyTyped(char typedChar, int keyCode) {
        moduleList.forEach(e -> e.keyTyped(typedChar, keyCode));
    }

    public void setSelected(boolean s) {
        this.selected = s;
    }


    public boolean isSelected() {
        return this.selected;
    }
}
