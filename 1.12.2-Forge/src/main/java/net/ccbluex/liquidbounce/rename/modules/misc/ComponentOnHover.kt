package net.ccbluex.liquidbounce.rename.modules.misc

import com.allatori.annotations.ControlFlowObfuscation
import com.allatori.annotations.Rename
import com.allatori.annotations.StringEncryption
import com.allatori.annotations.StringEncryptionType
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(name = "ComponentOnHover", description = "Allows you to see onclick action and value of chat message components when hovered.", category = ModuleCategory.MISC)
class ComponentOnHover : Module()