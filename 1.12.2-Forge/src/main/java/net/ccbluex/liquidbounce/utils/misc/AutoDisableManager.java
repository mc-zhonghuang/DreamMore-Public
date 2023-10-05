package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.injection.backend.PacketImplKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class AutoDisableManager extends MinecraftInstance implements Listenable {
    @EventTarget
    public void onWorld(WorldEvent event) {
        final Thread thread = new Thread("Disabled module") {
            @Override
            public void run() {
                LiquidBounce.moduleManager.getModules().forEach(module -> {
                    if (module.getAutoDisableMode() == Module.AutoDisableMode.WORLDCHANGE && module.getState()) {
                        module.toggle();
                    }
                });
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.getThePlayer() == null || mc.getTheWorld() == null) return;

        if (mc.getThePlayer().getHealth() <= 0 || mc.getThePlayer().isDead()) {
            final Thread thread = new Thread("Disabled module") {
                @Override
                public void run() {
                    LiquidBounce.moduleManager.getModules().forEach(module -> {
                        if (module.getAutoDisableMode() == Module.AutoDisableMode.DEAD && module.getState()) {
                            module.toggle();
                        }
                    });
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (PacketImplKt.unwrap(event.getPacket()) instanceof SPacketPlayerPosLook) {
            final Thread thread = new Thread("Disabled module") {
                @Override
                public void run() {
                    LiquidBounce.moduleManager.getModules().forEach(module -> {
                        if (module.getAutoDisableMode() == Module.AutoDisableMode.LAG && module.getState()) {
                            module.toggle();
                        }
                    });
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}
