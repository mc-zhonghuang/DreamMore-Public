package net.ccbluex.liquidbounce.ui.font.yalan;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class FontLoaders {
    public static FontDrawer F14;
    public static FontDrawer F16;
    public static FontDrawer F18;
    public static FontDrawer F20;
    public static FontDrawer F22;
    public static FontDrawer F23;
    public static FontDrawer F24;
    public static FontDrawer F30;
    public static FontDrawer F40;
    public static FontDrawer F50;
    public static FontDrawer C12;
    public static FontDrawer C14;
    public static FontDrawer C16;
    public static FontDrawer C18;
    public static FontDrawer C20;
    public static FontDrawer C22;
    public static FontDrawer Logo;
    public static FontDrawer Check;
    public static List<FontDrawer> fonts;

    public static FontDrawer getFontRender(int size) {
        return fonts.get(size - 10);
    }

    public static void initFonts() {
        Check = new FontDrawer(FontLoaders.getCheck(35), true);
        Logo = new FontDrawer(FontLoaders.getNovo(40), true);
        C22 = new FontDrawer(FontLoaders.getComfortaa(22), true);
        C20 = new FontDrawer(FontLoaders.getComfortaa(20), true);
        C18 = new FontDrawer(FontLoaders.getComfortaa(18), true);
        C16 = new FontDrawer(FontLoaders.getComfortaa(16), true);
        C14 = new FontDrawer(FontLoaders.getComfortaa(14), true);
        C12 = new FontDrawer(FontLoaders.getComfortaa(12), true);
        F50 = new FontDrawer(FontLoaders.getFont(50), true);
        F40 = new FontDrawer(FontLoaders.getFont(40), true);
        F30 = new FontDrawer(FontLoaders.getFont(30), true);
        F24 = new FontDrawer(FontLoaders.getFont(24), true);
        F23 = new FontDrawer(FontLoaders.getFont(23), true);
        F22 = new FontDrawer(FontLoaders.getFont(22), true);
        F20 = new FontDrawer(FontLoaders.getFont(20), true);
        F18 = new FontDrawer(FontLoaders.getFont(18), true);
        F14 = new FontDrawer(FontLoaders.getFont(14), true);
        F16 = new FontDrawer(FontLoaders.getFont(16), true);
        fonts = new ArrayList<>();
    }

    public static Font getFont(int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("more/font/product-sans.ttf")).getInputStream()).deriveFont(0, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getCheck(int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("more/font/check.ttf")).getInputStream()).deriveFont(0, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getComfortaa(int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("more/font/product-sans.ttf")).getInputStream()).deriveFont(0, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getNovo(int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("more/font/product-sans.ttf")).getInputStream()).deriveFont(0, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}
