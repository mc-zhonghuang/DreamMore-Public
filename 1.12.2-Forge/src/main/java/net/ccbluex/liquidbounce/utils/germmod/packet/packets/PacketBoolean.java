package net.ccbluex.liquidbounce.utils.germmod.packet.packets;

import net.ccbluex.liquidbounce.utils.germmod.packet.BasePacket;
import net.minecraft.network.PacketBuffer;

public class PacketBoolean implements BasePacket {
    public static boolean canSend = false;


    @Override
    public void read(PacketBuffer buffer) {
    }

    @Override
    public void encode() {
        canSend = true;
    }

    @Override
    public int packId() {
        return 731;
    }

    @Override
    public void write(PacketBuffer buffer) {
    }
}
