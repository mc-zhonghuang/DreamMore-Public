package net.ccbluex.liquidbounce.api.minecraft.network.play.client

import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket

interface ICPacketPlayerTryToUseItem: IPacket {
    val hand: WEnumHand
}