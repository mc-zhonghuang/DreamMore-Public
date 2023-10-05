package net.ccbluex.liquidbounce.utils.germmod.packet.packets;

import net.ccbluex.liquidbounce.utils.germmod.packet.BasePacket;
import net.minecraft.network.PacketBuffer;

public class PacketChannel implements BasePacket {
    private final String version;
    private final int packetId = 16;
    private final String digging;

    @Override
    public void read(PacketBuffer packetBuffer) {
    }

    @Override
    public int packId() {
        return 16;
    }

    public PacketChannel(String version, String digging) {
        this.version = version;
        this.digging = digging;
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(this.version);
        packetBuffer.writeString(this.digging);
    }

    @Override
    public void encode() {
    }
}