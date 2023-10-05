package net.ccbluex.liquidbounce.utils.germmod.packet.packets;

import net.ccbluex.liquidbounce.rename.modules.misc.HYTJoin4v4;
import net.ccbluex.liquidbounce.utils.germmod.packet.BasePacket;
import net.minecraft.network.PacketBuffer;

public class PacketDigging implements BasePacket {

    @Override
    public void read(PacketBuffer buffer) {
    }

    @Override
    public void encode() {
        if (PacketBoolean.canSend) {
            HYTJoin4v4.INSTANCE.sendPacket(new PacketChannel("3.4.2", HYTJoin4v4.INSTANCE.getBasePacket()));
            PacketBoolean.canSend = false;
        }
    }

    @Override
    public int packId() {
        return 72;
    }

    @Override
    public void write(PacketBuffer buffer) {
    }
}
