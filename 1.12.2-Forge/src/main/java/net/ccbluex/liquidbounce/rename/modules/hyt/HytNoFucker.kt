package net.ccbluex.liquidbounce.rename.modules.hyt

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.rename.modules.world.Fucker
import net.ccbluex.liquidbounce.utils.block.BlockUtils
import net.ccbluex.liquidbounce.utils.block.BlockUtils.getBlock
import net.ccbluex.liquidbounce.value.BlockValue

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name ="HytNoFucker", description = "CNM", category = ModuleCategory.HYT)
class HytNoFucker: Module(){
    private val blockValue = BlockValue("Block", 26)
    val targetId =blockValue.get()
    private var pos: WBlockPos? = null
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(pos == null || functions.getIdFromBlock(getBlock(pos!!)!!) != targetId || BlockUtils.getCenterDistance(Fucker.pos!!) >7)
            pos = Fucker.find(targetId)
    }
}