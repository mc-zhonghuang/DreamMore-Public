package net.ccbluex.liquidbounce.rename.modules.hyt;

import com.allatori.annotations.ControlFlowObfuscation;
import com.allatori.annotations.Rename;
import com.allatori.annotations.StringEncryption;
import com.allatori.annotations.StringEncryptionType;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.script.api.global.Chat;
import net.minecraft.network.Packet;

import java.util.concurrent.LinkedBlockingDeque;

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "HytBlink", description = "LiYingNMSL", category = ModuleCategory.HYT)
public class HytBlink extends Module {
    private boolean disable = false;
    private LinkedBlockingDeque<IPacket> packets;

    @EventTarget
    public void onPacket(PacketEvent event){
        IPacket packet = event.getPacket();
        if(disable || mc.getThePlayer() == null){
            return;
        }

        if(packet.toString().contains("client")){
            packets.add(packet);
            event.cancelEvent();
            KillAura killAura = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);
            if (killAura.getState()){
                killAura.setState(false);
            }
        }
    }

    @Override
    public void onEnable() {
        disable = false;
        packets = new LinkedBlockingDeque<>();
    }

    @Override
    public void onDisable() {
        disable = true;
        while(!packets.isEmpty()){
            Chat.print("send");
            try {
                mc.getNetHandler().getNetworkManager().sendPacket(packets.take());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
