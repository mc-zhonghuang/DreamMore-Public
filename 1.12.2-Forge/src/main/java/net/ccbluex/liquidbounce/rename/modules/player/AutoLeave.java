package net.ccbluex.liquidbounce.rename.modules.player;

import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.ItemImplKt;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

@ModuleInfo(name = "AntiLeave", description = "Pass", category = ModuleCategory.PLAYER)
public class AutoLeave extends Module {
    private final BoolValue runningValue = new BoolValue("Running-talk", true);
    private final TextValue runningManValue = new TextValue("Runaway-word", "[More] 拜拜了您嘞");
    private final TextValue leaveValue = new TextValue("Leave-command", "/hub");
    private final BoolValue keepArmorValue = new BoolValue("Keep-Armor", true);
    private final BoolValue noChainValue = new BoolValue("No-Chain-Armor", true);
    private final FloatValue healthValue = new FloatValue("Health", 7F, 0F, 20F);

    private boolean notChain(IItem item) {
        final Item unItem = ItemImplKt.unwrap(item);

        return !(unItem == Items.CHAINMAIL_HELMET || unItem == Items.CHAINMAIL_BOOTS || unItem == Items.CHAINMAIL_CHESTPLATE || unItem == Items.CHAINMAIL_LEGGINGS);
    }

    private void click(int item) {
        if (mc.getThePlayer() == null || mc.getTheWorld() == null) return;

        if (item != -1) {
            final boolean openInventory = !(mc2.currentScreen instanceof GuiInventory);
            if (openInventory) mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(),ICPacketEntityAction.WAction.OPEN_INVENTORY));
            if (!noChainValue.get() || (mc.getThePlayer().getInventory().armorItemInSlot(item).getItem() != null && notChain(mc.getThePlayer().getInventory().armorItemInSlot(item).getItem()))) mc.getPlayerController().windowClick(
                    mc.getThePlayer().getInventoryContainer().getWindowId(), item, 0, 1, mc.getThePlayer()
            );
            if (openInventory) mc.getNetHandler().addToSendQueue(classProvider.createCPacketCloseWindow());
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.getThePlayer() == null || mc.getTheWorld() == null) return;

        if (mc.getThePlayer().getHealth() <= healthValue.get()) {
            if (runningValue.get()) {
                mc.getThePlayer().sendChatMessage(runningManValue.get());
            }

            if (keepArmorValue.get()) {
                for (int i = 0;i < 4;i++) {
                    final int armorSlot = 3-i;
                    click(8 - armorSlot);
                }
            }

            mc.getThePlayer().sendChatMessage(leaveValue.get());
        }
    }
}
