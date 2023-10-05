package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Settings.*;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Utils.Position;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.value.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class ModuleRender {

    public Position pos;

    public float y,x;

    private float offset =0;

    private final Module parentModule;

    public boolean selected,binds;

    public int height = 0;

    //单独的滚轮Y
    public int scrollY = 0;

    public List<Downward> downwards = new ArrayList<>();

    public ModuleRender(Module module, float modX, float modY, float w, float h) {
        this.parentModule = module;
        int cHeight = 20;
        for (Value<?> setting : module.getValues()) {
            if (setting instanceof IntegerValue) {
                this.downwards.add(new NumberSetting((IntegerValue)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 16;
            }
            if (setting instanceof FloatValue) {
                this.downwards.add(new FloatSetting((FloatValue)setting, modX, modY + (float)cHeight, 0, 0, this));
                cHeight += 16;
            }
            if (setting instanceof BoolValue) {
                this.downwards.add(new BoolSetting((BoolValue)setting, modX, modY + (float)cHeight - 6.0f, 0, 0, this));
                cHeight += 16;
            }
            if (setting instanceof ListValue) {
                this.downwards.add(new ListSetting((ListValue)setting, modX, modY + (float)cHeight, -6, 0, this));
                cHeight += 22;
            }
            if (setting instanceof TextValue) {
                this.downwards.add(new TextSetting((TextValue)setting, modX, modY + (float)cHeight, -6, 0, this));
                cHeight += 22;
            }
            if (!(setting instanceof ColorValue)) continue;
            this.downwards.add(new ColorSetting((ColorValue)setting, modX, modY + (float)cHeight, -6, 0, this));
            cHeight += 22;
        }
        this.height = cHeight;
        this.pos = new Position(modX, modY, w, cHeight);
    }

    //获取主界面xy跟随移动
    private float modulex,moduley;

    HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

    public void drawScreen(int mouseX, int mouseY) {
        modulex = LiquidBounce.INSTANCE.getOtc().getMainx();
        moduley=  LiquidBounce.INSTANCE.getOtc().getMainy();

        this.x = pos.x;
        this.y = pos.y + getScrollY();

        final float[] hudHSB = hud.getHudColor().getHSB();
        Color color = Color.getHSBColor(hudHSB[0], hudHSB[1], 0.5f);

        //width始终不变 height跟随valuesize改变 默认0value = 23
        RoundedUtil.drawRound(modulex+ 5+ x,moduley+ 17 +y ,135, pos.height,3,new Color(50,54,65));
        RoundedUtil.drawGradientHorizontal(modulex+ 5 + x,moduley+ 18 +y,135,1.5f,1, color  ,new Color(hud.getHudColor().get()));

        FontLoaders.C16.drawString(parentModule.getSpacedName(),modulex+ 5 + x,moduley+ 17 +y-8,new Color(255,255,255).getRGB());

        //2color utils 86,94,115
        RenderUtils.drawRoundedRect(modulex+ 5+ x + 4,moduley+ 17 +y + 8,135 -128,7,1,parentModule.getState() == true ?new Color(86,94,115).getRGB() :new Color(50,54,65).getRGB()  ,1,parentModule.getState() == true ? new Color(86,94,115).getRGB():new Color(85,90,96).getRGB());
        //显示指针所选
        if (isHovered(mouseX,mouseY)){
            RenderUtils.drawRoundedRect(modulex+ 5+ x + 4,moduley+ 17 +y + 8,135 -128,7,1,new Color(0,0,0,0).getRGB()  ,1,new Color(hud.getHudColor().get()).getRGB());
        }
        FontLoaders.C12.drawString("Enable",modulex+ 5+ x + 4 + 10,moduley+ 17 +y + 8+ 3,new Color(200,200,200).getRGB());


        //循环添加Valuerender
        for (Downward downward : downwards){
            downward.draw(mouseX,mouseY);
        }

        //绘制按键绑定 28,32,40 , 49,53,61
        RenderUtils.drawRoundedRect(modulex+ 5+ x + 115 - FontLoaders.C12.getStringWidth(binds ? "..." : Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 13,moduley+ 17 +y + 8 + 0.5f,FontLoaders.C12.getStringWidth(binds ? "..." :Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 4,7,1,new Color(28,32,40).getRGB(),1,new Color(86,94,115).getRGB());
        FontLoaders.C12.drawString(binds ? "..." :Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase(),modulex+ 5+ x + 117 - FontLoaders.C12.getStringWidth(binds ? "..." :Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 13,moduley+ 17 +y + 11,-1);
        if (isKeyBindHovered(mouseX,mouseY)){
            RenderUtils.drawRoundedRect(modulex+ 5+ x + 115 - FontLoaders.C12.getStringWidth(binds ? "..." :Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 13 ,moduley+ 17 +y + 8 + 0.5f,FontLoaders.C12.getStringWidth(binds ? "..." :Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 4,7,1,new Color(0,0,0,0).getRGB()  ,1,new Color(hud.getHudColor().get()).getRGB());
        }
    }


    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton){


            if (isKeyBindHovered(mouseX,mouseY)){
                if (mouseButton == 0) {
                    binds = true;
                }
            }
            if (!isKeyBindHovered(mouseX,mouseY) && binds){
                if (mouseButton == 0) {
                    binds = false;
                }
            }
            if (isHovered(mouseX,mouseY)){
                if (mouseButton == 0) {
                    parentModule.toggle();
                }
            }
                //null binds
                if (this.binds) {
                    if (mouseButton == 1) {
                    parentModule.setKeyBind(0);
                    this.binds = false;
                    }
                }


        downwards.forEach(e -> e.mouseClicked(mouseX,mouseY,mouseButton));

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
      //  if (state == 0) binds = false;

        downwards.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
    }
    public void keyTyped(char typedChar, int keyCode) {
        int finalKeyCode = keyCode;
        downwards.forEach(e -> e.keyTyped(typedChar,finalKeyCode));
        if (this.binds) {
            if (keyCode == 211) {
                keyCode = 0;
            }
            parentModule.setKeyBind(keyCode);
            this.binds = false;
        }

    }
    //

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=modulex+ 5+ x + 4   && mouseX <= modulex+ 5+ x + 4 + 15 && mouseY >= moduley+ 17 +y + 8 && mouseY <= moduley+ 17 +y + 8+ 6;
    }
    //isHovered
    public boolean isKeyBindHovered(int mouseX, int mouseY) {
        return mouseX >=modulex+ 5+ x + 115 - FontLoaders.C12.getStringWidth(Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 13   && mouseX <= modulex+ 5+ x + 115 - FontLoaders.C12.getStringWidth(Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 13 + FontLoaders.C12.getStringWidth(Keyboard.getKeyName(parentModule.getKeyBind()).toLowerCase()) + 3   && mouseY >= moduley+ 17 +y + 8 + 0.5f && mouseY <= moduley+ 17 +y + 8 + 0.5f + 7;
    }

    public Module getparent() {
        return parentModule;
    }
    //只有滚轮在使用
    public float getY(){
        return pos.y + getScrollY();
    }
    public float getMaxValueY(){
        return downwards.get(downwards.size() -1).getY();
    }
    public void setY(float y){
        this.pos.y = y;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
    //
}
