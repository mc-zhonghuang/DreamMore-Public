package net.ccbluex.liquidbounce.utils.germmod.packet.packets;

import net.ccbluex.liquidbounce.utils.germmod.packet.BasePacket;
import net.minecraft.network.PacketBuffer;

import java.util.UUID;

public class PacketUUID implements BasePacket {
    private final int n;
    private final UUID uuid;

    public PacketUUID(int n, UUID uuid) {
        this.n = n;
        this.uuid = uuid;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }

    @Override
    public void encode() throws Exception {
    }

    @Override
    public int packId() {
        return 19;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(this.n);
        buffer.writeUniqueId(this.uuid);
    }
}
