package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.ui.font.simple.api.FontManager;
import net.ccbluex.liquidbounce.ui.font.simple.impl.SimpleFontManager;
import net.ccbluex.liquidbounce.ui.client.newdropdown.SideGui.SideGui;

public class ClientMain {

    private static ClientMain INSTANCE;

    public static ClientMain getInstance() {
        try {
            if (INSTANCE == null) INSTANCE = new ClientMain();
            return INSTANCE;
        } catch (Throwable t) {
            ClientUtils.getLogger().warn(t);
            throw t;
        }
    }

    public FontManager fontManager = SimpleFontManager.create();
    public FontManager getFontManager() {
        return fontManager;
    }

    private final SideGui sideGui = new SideGui();

    public SideGui getSideGui() {
        return sideGui;
    }

}
