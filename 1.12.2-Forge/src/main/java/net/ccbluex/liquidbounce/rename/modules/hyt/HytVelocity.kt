package net.ccbluex.liquidbounce.rename.modules.hyt

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.server.SPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketPlayerAbilities

@ModuleInfo(name = "HytVelocity", description = "FUCK U MAN.", category = ModuleCategory.HYT)
class HytVelocity: Module() {
    private var cancelTicks = 0
    private val packets = arrayListOf<Packet<INetHandlerPlayClient>>()

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()

        if (packet is SPacketConfirmTransaction || packet is SPacketPlayerAbilities) {
            packets.add((packet as Packet<INetHandlerPlayClient>))
            event.cancelEvent()
        }
        if (packet is SPacketEntityVelocity) {
            cancelTicks = 6
            event.cancelEvent()
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        cancelTicks--
        if (cancelTicks == 0)
            blink()
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        blink()
    }

    private fun blink() {
        cancelTicks = 0
        packets.forEach{ packet -> packet.processPacket(mc.netHandler.unwrap()) }
        packets.clear()
    }
}