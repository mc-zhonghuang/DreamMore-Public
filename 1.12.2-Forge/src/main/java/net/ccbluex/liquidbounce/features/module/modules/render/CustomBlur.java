package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name = "CustomBlur" , description = "CustomBlur." , category = ModuleCategory.RENDER)
public class CustomBlur extends Module {
    public static final BoolValue chat = new BoolValue("Chat", false);
    public static final IntegerValue radius = new IntegerValue("Radius", 4, 0, 10);
}
