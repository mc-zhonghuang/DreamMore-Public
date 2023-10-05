package net.ccbluex.liquidbounce.rename.modules.misc;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import top.fl0wowp4rty.Native;

@ModuleInfo(name = "MoreChecker", description = "Check all players using DreamMore", category = ModuleCategory.MISC)
@Native
public class MoreChecker extends Module {
    private final BoolValue noHit = new BoolValue("Auto-Friends", true) {
        @Override
        protected void onChange(Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                set(true);
                ClientUtils.displayChatMessage("§f[§b" + LiquidBounce.CLIENT_NAME2 + " IRC§f] 让你关了吗你就关？");
            }
        }

        @Override
        protected void onChanged(Boolean oldValue, Boolean newValue) {
            set(true);
        }
    };
    private final BoolValue noRender = new BoolValue("No-Render", true);

    @EventTarget
    public void onText(TextEvent event) {
        if (!noRender.get()) {
            if (event.getText() == null || event.getText().contains("服务器IP") || mc2.player == null) return;
            for (EntityPlayer player : mc2.world.playerEntities) {
                if (player instanceof EntityPlayer) {
                    if (event.getText().contains((player.getDisplayNameString()))) {
                        if (LiquidBounce.getMoreUsers().contains(player.getDisplayNameString())) {
                            event.setText(event.getText().replaceAll(player.getDisplayNameString(), "§f[§b" + LiquidBounce.CLIENT_NAME2 + " IRC§f] " + player.getDisplayNameString()));
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc2.player == null || mc2.world == null) return;
        if (mc2.player.ticksExisted <= 1 && Minecraft.getMinecraft().player != null) {

        }
    }

    @Override
    public void onDisable() {
        toggle();
        ClientUtils.displayChatMessage("§f[§b" + LiquidBounce.CLIENT_NAME2 + " IRC§f] 让你关了吗你就关？");
    }

    public BoolValue getNoHit() {
        return this.noHit;
    }
}
