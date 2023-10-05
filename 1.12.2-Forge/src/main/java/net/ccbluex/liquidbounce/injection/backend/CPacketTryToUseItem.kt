package net.ccbluex.liquidbounce.injection.backend

import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerTryToUseItem
import net.minecraft.network.play.client.CPacketPlayerTryUseItem

class CPacketPlayerTryUseItemImpl<T : CPacketPlayerTryUseItem>(wrapper: T) : PacketImpl<T>(wrapper), ICPacketPlayerTryToUseItem {
    override val hand: WEnumHand
        get() = WEnumHand.valueOf(wrapped.hand.name)
}
inline fun ICPacketPlayerTryToUseItem.unwrap(): CPacketPlayerTryUseItem = (this as CPacketPlayerTryUseItemImpl<*>).wrapped

inline fun CPacketPlayerTryUseItem.wrap(): ICPacketPlayerTryToUseItem = CPacketPlayerTryUseItemImpl(this)