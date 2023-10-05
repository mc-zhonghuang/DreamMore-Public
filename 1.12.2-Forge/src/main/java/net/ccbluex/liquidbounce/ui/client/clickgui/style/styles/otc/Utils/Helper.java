/*
 * Decompiled with CFR 0_132.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otc.Utils;

import net.minecraft.client.Minecraft;

public class Helper {
    public static Minecraft mc = Minecraft.getMinecraft();


    public static boolean onServer(String server) {
        if (!mc.isSingleplayer() && Helper.mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
            return true;
        }
        return false;
    }
}

