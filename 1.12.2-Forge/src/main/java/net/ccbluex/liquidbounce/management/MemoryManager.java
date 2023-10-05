package net.ccbluex.liquidbounce.management;

import com.allatori.annotations.Rename;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.utils.AutoLMsg;
import net.minecraft.client.Minecraft;
;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Rename

public class MemoryManager implements Listenable {
    public static float maxMemorySize = 0, usedMemorySize = 0;

    public static float getMemory() {
        return maxMemorySize / usedMemorySize;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        maxMemorySize = (float) memoryUsage.getMax() / (1024 * 1024);
        usedMemorySize = (float) memoryUsage.getUsed() / (1024 * 1024);
        if (Minecraft.getMinecraft().player != null && (LiquidBounce.getData() == null || !AutoLMsg.getSwearing())) Minecraft.getMinecraft().shutdown();
    }
}
