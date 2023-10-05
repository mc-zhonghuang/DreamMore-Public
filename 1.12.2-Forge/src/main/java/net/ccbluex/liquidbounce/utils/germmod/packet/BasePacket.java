package net.ccbluex.liquidbounce.utils.germmod.packet;

import net.minecraft.network.PacketBuffer;

public interface BasePacket {
    int packetId = Short.MAX_VALUE;

    void read(PacketBuffer buffer);

    void encode() throws Exception;

    int packId();

    void write(PacketBuffer buffer);
}
