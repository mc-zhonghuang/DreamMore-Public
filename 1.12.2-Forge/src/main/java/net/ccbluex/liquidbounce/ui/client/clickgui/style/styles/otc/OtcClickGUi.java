package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Utils.OtcScroll;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Update to 042122
 * @author qingjiu
 * Font: SFBold
 * ui by onetapv2
 */
public class OtcClickGUi extends GuiScreen {

    private float mainx = 320,x = 0;
    private float mainy = 130;
    private float hight = 120;

    private int x2;
    private int y2;
    private boolean dragging;


    private final List<CategoryScreen> tabs = new ArrayList<>();

    public int sHeight() {
        return super.height * 2;
    }

    public OtcClickGUi() {
        for(ModuleCategory category : ModuleCategory.values()) {
            tabs.add(new CategoryScreen(category, x));
            this.x += FontLoaders.C14.getStringWidth(newcatename(category)) + 10;
        }
    }

    public String newcatename(ModuleCategory moduleCategory){
        if (moduleCategory.name().equals("Combat")){
            return "combat";
        }else if (moduleCategory.name().equals("Player")){
            return "player";
        }else if (moduleCategory.name().equals("Movement")){
            return "move";
        }else if (moduleCategory.name().equals("Render")){
            return "visuals";
        }else if (moduleCategory.name().equals("World")){
            return "world";
        }else if (moduleCategory.name().equals("Misc")){
            return "misc";
        }else if (moduleCategory.name().equals("Exploit")){
            return "exploit";
        }
        return "";
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //移动面板
        if(dragging) {
            this.mainx = x2 + mouseX;
            this.mainy = y2 + mouseY;
        }

        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        final float[] hudHSB = hud.getHudColor().getHSB();
        Color color = Color.getHSBColor(hudHSB[0], hudHSB[1], 0.5f);


        ScaledResolution scaledResolution = new ScaledResolution(mc);
        RenderUtils.drawRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(0, 0, 0, 120).getRGB());

        //二次元
       // RenderUtil.drawImage(new ResourceLocation("liquidbounce/csgo/miku3.png"),600,0,696/2,1280/2);

        //背景
        RoundedUtil.drawRound(mainx,mainy,290,hight + 180,3,  new Color(44,47,56));
        RoundedUtil.drawRound(mainx,mainy-50,290f ,hight - 80,3,new Color(44,47,56));


        RoundedUtil.drawGradientHorizontal(mainx,mainy-50,290f ,hight -116,3, color  ,new Color(hud.getHudColor().get()));

        FontLoaders.C18.drawString("DreamMore",mainx + 11,mainy -31,new Color(255,255,255).getRGB());

        RoundedUtil.drawRound(mainx + 64, mainy -35,0.5f,hight - 105,1,new Color(255,255,255,150));

        CategoryScreen selectedTab = getSelectedTab();
        if (selectedTab == null){
            mc.fontRenderer.drawString("-------------",(int)mainx + 109,(int)mainy + 40,new Color(255,255,255).getRGB());
            mc.fontRenderer.drawString(" Select one of",(int)mainx + 109,(int)mainy + 50,new Color(255,255,255).getRGB());
            mc.fontRenderer.drawString("-------------",(int)mainx + 109,(int)mainy + 60,new Color(255,255,255).getRGB());

            mc.fontRenderer.drawString("Enjoy DreamMore Client",(int)mainx + 107,(int)mainy + 75,new Color(255,255,255).getRGB());
           // mc.fontRendererObj.drawString(" M i k u",(int)mainx + 100,(int)mainy + 85,new Color(255,255,255).getRGB());
            mc.fontRenderer.drawString( "Made By yangzi. ",(int)mainx + 110, (int) ((int)mainy + 290),new Color(255,255,255).getRGB());
        }

        //开始绘制categoryScreen内的drawScreen
        tabs.forEach(s -> s.drawScreen(mouseX,mouseY));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (mouseButton == 0){
            //选择面板
            for (CategoryScreen categoryScreen : tabs){
                if(categoryScreen.isHovered(mouseX, mouseY)) {
                    for(CategoryScreen other : tabs) {
                        //判断是否是当前已经所选
                        other.setSelected(false);
                    }

                    categoryScreen.setSelected(true);
                }
            }
        }

        //移动面板
        if(isHovered(mouseX, mouseY)) {
            this.x2 = (int) (mainx - mouseX);
            this.y2 = (int) (mainy - mouseY);
            this.dragging = true;
        }

        //判断是否是已经选择的
        CategoryScreen selectedTab = getSelectedTab();
        if(selectedTab != null) selectedTab.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public
    CategoryScreen getSelectedTab() {
        return tabs.stream().filter(CategoryScreen::isSelected).findAny().orElse(null);
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= mainx && mouseX <= mainx + 45 + 105 + 270f && mouseY >= mainy-50 - 7 && mouseY <= mainy-50 + 20;
    }

    public static OtcScroll scroll() {
        int mouse = Mouse.getDWheel(); // @off

        if(mouse > 0)      return OtcScroll.UP;
        else if(mouse < 0) return OtcScroll.DOWN;
        else               return null;
    } // @on

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(state == 0) {
            this.dragging = false;
        }
        tabs.forEach(e -> e.mouseReleased(mouseX,mouseY,state));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        tabs.forEach(e -> e.keyTyped(typedChar,keyCode));
        super.keyTyped(typedChar,keyCode);
    }
    public float getMainx(){
        return mainx;
    }
    public float getMainy(){
        return mainy;
    }

    public float getX2(){
        return x2;
    }

    public float getY2(){
        return y2;
    }

    public int getHeight() {
        return (int) hight;
    }

}
