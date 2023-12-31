/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.rename.modules.world;

import com.allatori.annotations.ControlFlowObfuscation;
import com.allatori.annotations.Rename;
import com.allatori.annotations.StringEncryption;
import com.allatori.annotations.StringEncryptionType;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.util.*;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.injection.backend.EnumFacingImplKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.*;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@Rename
@ControlFlowObfuscation("enable")
@StringEncryption("enable")
@StringEncryptionType("fast")
@ModuleInfo(
        name = "Scaffold",
        description = "修复版",
        category = ModuleCategory.WORLD,
        keyBind = Keyboard.KEY_G,
        autoDisableMode = Module.AutoDisableMode.WORLDCHANGE
)
public class Scaffold extends Module {

    /**
     * OPTIONS
     */

    // Mode
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "HuaYuTing", "Rewinside", "Expand"}, "Normal") {
        @Override
        protected void onChange(String oldValue, String newValue) {
            if (newValue.equalsIgnoreCase("HuaYuTing")) {
                sprintModeValue.set("HuaYuTing");
                placeConditionValue.set("HuaYuTing");
            }
        }
    };

    // Delay
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = minDelayValue.get();

            if (i > newValue)
                set(i);
        }
    };

    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = maxDelayValue.get();

            if (i < newValue)
                set(i);
        }
    };
    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);

    // AutoBlock
    private final ListValue autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Spoof", "Pick", "Switch"}, "Spoof");

    private final ListValue sprintModeValue = new ListValue("Sprint-Mode", new String[]{"Normal", "OnGround", "HuaYuTing", "OffGround", "Off"}, "Normal");
    private final ListValue placeConditionValue = new ListValue("Place-Condition", new String[]{"Air", "FallDown", "NegativeMotion", "HuaYuTing", "Always"}, "Always");
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue searchValue = new BoolValue("Search", true);
    private final BoolValue downValue = new BoolValue("Down", true);
    private final BoolValue picker = new BoolValue("Picker", false);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");

    // Eagle
    private final ListValue eagleValue = new ListValue("Eagle", new String[]{"Normal", "EdgeDistance", "Silent", "Off"}, "Off");
    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);
    private final FloatValue edgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2F, 0F, 0.5F);

    // Expand
    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);

    // RotationStrafe
    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false);

    // Rotations
    private final ListValue rotationModeValue = new ListValue("RotationMode", new String[]{"Basic", "Normal", "Static", "StaticPitch", "StaticYaw", "Off"}, "Normal");
    private final BoolValue silentRotation = new BoolValue("SilentRotation", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);
    private final FloatValue staticPitchValue = new FloatValue("StaticPitchOffset", 86F, 70F, 90F);
    private final FloatValue staticYawOffsetValue = new FloatValue("StaticYawOffset", 0F, 0F, 90F);


    // Other
    private final FloatValue xzRangeValue = new FloatValue("xzRange", 0.8F, 0.1F, 1.0F);
    private final FloatValue yRangeValue = new FloatValue("yRange", 0.8F, 0.1F, 1.0F);

    // SearchAccuracy
    private final IntegerValue searchAccuracyValue = new IntegerValue("SearchAccuracy", 8, 1, 24) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            if (getMaximum() < newValue) {
                set(getMaximum());
            } else if (getMinimum() > newValue) {
                set(getMinimum());
            }
        }
    };

    // Turn Speed
    private final FloatValue maxTurnSpeedValue = new FloatValue("MaxTurnSpeed", 180, 1, 180) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            float v = minTurnSpeedValue.get();
            if (v > newValue) set(v);
            if (getMaximum() < newValue) {
                set(getMaximum());
            } else if (getMinimum() > newValue) {
                set(getMinimum());
            }
        }
    };
    private final FloatValue minTurnSpeedValue = new FloatValue("MinTurnSpeed", 180, 1, 180) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            float v = maxTurnSpeedValue.get();
            if (v < newValue) set(v);
            if (getMaximum() < newValue) {
                set(getMaximum());
            } else if (getMinimum() > newValue) {
                set(getMinimum());
            }
        }
    };

    // Zitter
    private final BoolValue zitterValue = new BoolValue("Zitter", false);
    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[]{"Teleport", "Smooth"}, "Teleport");
    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13F, 0.1F, 0.3F);
    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072F, 0.05F, 0.2F);

    // Game
    private final FloatValue timerValue = new FloatValue("Timer", 1F, 0.1F, 10F);
    private final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1F, 0, 2F);
    private final BoolValue slowValue = new BoolValue("Slow", false) {
        @Override
        protected void onChanged(final Boolean oldValue, final Boolean newValue) {
            if (newValue)
                sprintModeValue.set("Off");
        }
    };
    private final FloatValue slowSpeed = new FloatValue("SlowSpeed", 0.6F, 0.2F, 0.8F);

    // Safety
    private long lastMS = 0L;
    private float progress = 0;
    private final BoolValue sameYValue = new BoolValue("SameY", false);

    private final BoolValue smartSpeedValue = new BoolValue("SmartSpeed", false);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", true);
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    // Visuals
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);
    private final BoolValue markValue = new BoolValue("Mark", false);

    private final BoolValue autoTimer = new BoolValue("AutoTimer", false);
    /**
     * MODULE
     */

    private int lastSprint = 0;

    // Target block
    private PlaceInfo targetPlace;

    // Launch position
    private int launchY;

    // Rotation lock
    private Rotation lockRotation;
    private Rotation limitedRotation;
    private boolean facesBlock = false;

    // Auto block slot
    private int slot;

    // Zitter Smooth
    private boolean zitterDirection;

    // Delay
    private final MSTimer delayTimer = new MSTimer();
    private final MSTimer zitterTimer = new MSTimer();
    private long delay;

    // Eagle
    private int placedBlocksWithoutEagle = 0;
    private boolean eagleSneaking;

    // Down
    private boolean shouldGoDown = false;

    public boolean shouldSprint() {
        return ((sprintModeValue.equals("Normal", true)) || (sprintModeValue.equals("OffGround", true) && !mc.getThePlayer().getOnGround()) || ( sprintModeValue.equals("OnGround", true) && mc.getThePlayer().getOnGround()) || (sprintModeValue.equals("HuaYuTing", true) && mc.getThePlayer().getTicksExisted() >= lastSprint));
    }

    public boolean shouldPlace() {
        boolean placeWhenAir = placeConditionValue.get().equalsIgnoreCase("air");
        boolean placeWhenFall = placeConditionValue.get().equalsIgnoreCase("falldown");
        boolean placeWhenNegativeMotion = placeConditionValue.get().equalsIgnoreCase("negativemotion");
        boolean huayuting = placeConditionValue.get().equalsIgnoreCase("huayuting");
        boolean alwaysPlace = placeConditionValue.get().equalsIgnoreCase("always");
        return LiquidBounce.moduleManager.getModule(Tower.class).getState() || alwaysPlace || (placeWhenAir && !mc.getThePlayer().getOnGround()) || (placeWhenFall && mc.getThePlayer().getFallDistance() > 0) || (placeWhenNegativeMotion && mc.getThePlayer().getMotionY() < 0) || (huayuting && mc.getThePlayer().getTicksExisted() >= lastSprint);
    }

    /**
     * Enable module
     */
    @Override
    public void onEnable() {
        if (mc.getThePlayer() == null) return;
        if(autoTimer.get()){

            Timer timer = (Timer) LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
            timer.setState(true);
        }
        progress = 0;
        lastSprint = 0;
        launchY = (int) mc.getThePlayer().getPosY();
    }

    /**
     *  Tick event
     * @param event
     */

    @EventTarget
    public void onTick(TickEvent event) {
        mc.getTimer().setTimerSpeed(timerValue.get());
    }

    /**
     * Update event
     *
     * @param event
     */
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (modeValue.get().equalsIgnoreCase("HuaYuTing")) {
            if (mc.getThePlayer().getOnGround() && !mc.getGameSettings().getKeyBindJump().getPressed()) {
                if (MovementUtils.isMoving()) {
                    lockRotation = null;
                    limitedRotation = null;
                    RotationUtils.reset();
                    facesBlock = false;
                    shouldGoDown = false;
                    lastSprint = mc.getThePlayer().getTicksExisted() + 2;
                    mc.getThePlayer().setSprinting(true);
                    mc.getGameSettings().getKeyBindJump().setPressed(true);
                }
            } else if (!mc.getThePlayer().getOnGround()) {
                mc.getGameSettings().getKeyBindJump().setPressed(false);
            }
        }

        if(getBlocksAmount() == 0){
            this.toggle();
        }
        getBestBlocks();

        shouldGoDown = downValue.get() && !sameYValue.get() && mc.getGameSettings().getKeyBindSneak().isKeyDown() && getBlocksAmount() > 1;
        if (shouldGoDown)
            mc.getGameSettings().getKeyBindSneak().setPressed(false);

        if (slowValue.get()) {
            mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * slowSpeed.get());
            mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * slowSpeed.get());
        }

        if (shouldSprint()) {
            if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSprint())) {
                mc.getGameSettings().getKeyBindSprint().setPressed(false);
            }
            if (mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSprint())) {
                mc.getGameSettings().getKeyBindSprint().setPressed(true);
            }
            if (mc.getGameSettings().getKeyBindSprint().isKeyDown()) {
                mc.getThePlayer().setSprinting(true);
            }
            if (!mc.getGameSettings().getKeyBindSprint().isKeyDown()) {
                mc.getThePlayer().setSprinting(false);
            }
        }

        if (mc.getThePlayer().getOnGround()) {
            final String mode = modeValue.get();

            // Rewinside scaffold mode
            if (mode.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2F);
                mc.getThePlayer().setMotionY(0D);
            }

            // Smooth Zitter
            if (zitterValue.get() && zitterModeValue.get().equalsIgnoreCase("smooth")) {
                if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight()))
                    mc.getGameSettings().getKeyBindRight().setPressed(false);

                if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft()))
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);

                if (zitterTimer.hasTimePassed(100)) {
                    zitterDirection = !zitterDirection;
                    zitterTimer.reset();
                }

                if (zitterDirection) {
                    mc.getGameSettings().getKeyBindRight().setPressed(true);
                    mc.getGameSettings().getKeyBindLeft().setPressed(false);
                } else {
                    mc.getGameSettings().getKeyBindRight().setPressed(false);
                    mc.getGameSettings().getKeyBindLeft().setPressed(true);
                }
            }

            // Eagle
            if (!eagleValue.get().equalsIgnoreCase("Off") && !shouldGoDown) {
                double dif = 0.5D;
                if (eagleValue.get().equalsIgnoreCase("EdgeDistance") && !shouldGoDown) {
                    for (int i = 0; i < 4; i++) {
                        switch (i) {
                            case 0: {
                                final WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX() - 1, mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? 0D : 1.0D), mc.getThePlayer().getPosZ());
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.getThePlayer().getPosX() - blockPos.getX();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                            case 1: {
                                final WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX() + 1, mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? 0D : 1.0D), mc.getThePlayer().getPosZ());
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.getThePlayer().getPosX() - blockPos.getX();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                            case 2: {
                                final WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? 0D : 1.0D), mc.getThePlayer().getPosZ() - 1);
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.getThePlayer().getPosZ() - blockPos.getZ();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                            case 3: {
                                final WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? 0D : 1.0D), mc.getThePlayer().getPosZ() + 1);
                                final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

                                if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                                    double calcDif = mc.getThePlayer().getPosZ() - blockPos.getZ();
                                    calcDif -= 0.5D;

                                    if (calcDif < 0)
                                        calcDif *= -1;
                                    calcDif -= 0.5;

                                    if (calcDif < dif)
                                        dif = calcDif;
                                }

                            }
                        }
                    }
                }

                if (placedBlocksWithoutEagle >= blocksToEagleValue.get()) {
                    final boolean shouldEagle = mc.getTheWorld().getBlockState(new WBlockPos(mc.getThePlayer().getPosX(),
                            mc.getThePlayer().getPosY() - 1D, mc.getThePlayer().getPosZ())).getBlock().equals(classProvider.getBlockEnum(BlockType.AIR)) || (dif < edgeDistanceValue.get() && eagleValue.get().equalsIgnoreCase("EdgeDistance"));

                    if (eagleValue.get().equalsIgnoreCase("Silent") && !shouldGoDown) {
                        if (eagleSneaking != shouldEagle) {
                            mc.getNetHandler().addToSendQueue(
                                    classProvider.createCPacketEntityAction(mc.getThePlayer(), shouldEagle ?
                                            ICPacketEntityAction.WAction.START_SNEAKING :
                                            ICPacketEntityAction.WAction.STOP_SNEAKING)
                            );
                        }

                        eagleSneaking = shouldEagle;
                    } else
                        mc.getGameSettings().getKeyBindSneak().setPressed(shouldEagle);

                    placedBlocksWithoutEagle = 0;
                } else
                    placedBlocksWithoutEagle++;
            }

            // Zitter
            if (zitterValue.get() && zitterModeValue.get().equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(zitterSpeed.get());


                final double yaw = Math.toRadians(mc.getThePlayer().getRotationYaw() + (zitterDirection ? 90D : -90D));
                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() - Math.sin(yaw) * zitterStrength.get());
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() + Math.cos(yaw) * zitterStrength.get());
                zitterDirection = !zitterDirection;
            }

        }
        //Auto Jump thingy
        if (shouldGoDown) launchY = (int) mc.getThePlayer().getPosY() - 1;
        else if (!sameYValue.get()) {
            mc.getThePlayer().jump();
        }
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        if (mc.getThePlayer() == null)
            return;

        final IPacket packet = event.getPacket();

        // AutoBlock
        if (classProvider.isCPacketHeldItemChange(packet)) {
            final ICPacketHeldItemChange packetHeldItemChange = packet.asCPacketHeldItemChange();

            slot = packetHeldItemChange.getSlotId();
        }
    }

    @EventTarget
    private void onStrafe(StrafeEvent event) {

        if (!rotationStrafeValue.get())
            return;
        RotationUtils.serverRotation.applyStrafeToPlayer(event);
        event.cancelEvent();
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        final EventState eventState = event.getEventState();

        // Lock Rotation
        if (!rotationModeValue.get().equalsIgnoreCase("Off") && keepRotationValue.get() && lockRotation != null)
            setRotation(lockRotation);


        if (shouldPlace() && (facesBlock || rotationModeValue.get().equalsIgnoreCase("Off")) && placeModeValue.get().equalsIgnoreCase(eventState.getStateName()))
            place();

        // Update and search for new block
        if (eventState == EventState.PRE)
            update();

        // Reset placeable delay
        if (targetPlace == null && placeableDelay.get())
            delayTimer.reset();
    }

    private void update() {
        final boolean isHeldItemBlock = mc.getThePlayer().getHeldItem() != null && classProvider.isItemBlock(mc.getThePlayer().getHeldItem().getItem());
        if (!shouldPlace() || !autoBlockValue.get().equalsIgnoreCase("Off") ? InventoryUtils.findAutoBlockBlock() == -1 && !isHeldItemBlock : !isHeldItemBlock)
            return;

        findBlock(modeValue.get().equalsIgnoreCase("expand"));
    }

    private void setRotation(Rotation rotation, int keepRotation) {
        if (silentRotation.get()) {
            RotationUtils.setTargetRotation(rotation, keepRotation);
        } else {
            mc.getThePlayer().setRotationYaw(rotation.getYaw());
            mc.getThePlayer().setRotationPitch(rotation.getPitch());
        }
    }

    private void setRotation(Rotation rotation) {
        setRotation(rotation, 0);
    }

    /**
     * Search for new target block
     */
    private void findBlock(final boolean expand) {
        /*final WBlockPos blockPosition = shouldGoDown ? (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ?
                new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6D, mc.getThePlayer().getPosZ())
                : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()).down()) :
                (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? new WBlockPos(mc.getThePlayer())
                        : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ()).down());*/

        final WBlockPos blockPosition = shouldGoDown ? (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6D, mc.getThePlayer().getPosZ())
                : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY() - 0.6, mc.getThePlayer().getPosZ()).down()) :
                ((sameYValue.get() || ((autoJumpValue.get() || (smartSpeedValue.get() && LiquidBounce.moduleManager.getModule(Speed.class).getState())) && !(mc.getGameSettings().getKeyBindJump().isKeyDown())) && launchY <= mc.getThePlayer().getPosY() ? (new WBlockPos(mc.getThePlayer().getPosX(), launchY - 1, mc.getThePlayer().getPosZ())) :
                        (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? new WBlockPos(mc.getThePlayer())
                                : new WBlockPos(mc.getThePlayer().getPosX(), mc.getThePlayer().getPosY(), mc.getThePlayer().getPosZ()).down())));


        if (!expand && (!BlockUtils.isReplaceable(blockPosition) || search(blockPosition, !shouldGoDown)))
            return;

        if (expand) {
            for (int i = 0; i < expandLengthValue.get(); i++) {
                if (search(blockPosition.add(
                        mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.WEST)) ? -i : mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.EAST)) ? i : 0,
                        0,
                        mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.NORTH)) ? -i : mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.SOUTH)) ? i : 0
                ), false))

                    return;
            }
        } else if (searchValue.get()) {
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                    if (search(blockPosition.add(x, 0, z), !shouldGoDown))
                        return;
        }
    }

    /**
     * Place target block
     */
    private void place() {
        if (targetPlace == null) {
            if (placeableDelay.get())
                delayTimer.reset();
            return;
        }

        if (!delayTimer.hasTimePassed(delay) || (sameYValue.get() && launchY - 1 != (int) targetPlace.getVec3().getYCoord()))
            return;

        int blockSlot = -1;
        IItemStack itemStack = mc.getThePlayer().getHeldItem();

        if (itemStack == null || !classProvider.isItemBlock(itemStack.getItem()) ||
                classProvider.isBlockBush(itemStack.getItem().asItemBlock().getBlock()) || mc.getThePlayer().getHeldItem().getStackSize() <= 0) {
            if (autoBlockValue.get().equalsIgnoreCase("Off"))
                return;

            blockSlot = InventoryUtils.findAutoBlockBlock();

            if (blockSlot == -1)
                return;

            if(autoBlockValue.get().equalsIgnoreCase("Pick"))
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36));
            mc.getPlayerController().updateController();


            if (autoBlockValue.get().equalsIgnoreCase("Spoof")) {
                if (blockSlot - 36 != slot)
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(blockSlot - 36));
            } else if(autoBlockValue.get().equalsIgnoreCase("Switch")) {
                mc.getThePlayer().getInventory().setCurrentItem(blockSlot - 36);
                mc.getPlayerController().updateController();
            } else {
                mc.getThePlayer().getInventory().setCurrentItem(blockSlot - 36);
                mc.getPlayerController().updateController();
            }
            itemStack = mc.getThePlayer().getInventoryContainer().getSlot(blockSlot).getStack();
        }


        if (mc.getPlayerController().onPlayerRightClick(mc.getThePlayer(), mc.getTheWorld(), itemStack, targetPlace.getBlockPos(), targetPlace.getEnumFacing(), targetPlace.getVec3())) {
            delayTimer.reset();
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());

            if (mc.getThePlayer().getOnGround()) {
                final float modifier = speedModifierValue.get();

                mc.getThePlayer().setMotionX(mc.getThePlayer().getMotionX() * modifier);
                mc.getThePlayer().setMotionZ(mc.getThePlayer().getMotionZ() * modifier);
            }

            if (swingValue.get())
                mc.getThePlayer().swingItem();
            else
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketAnimation());
        }

        /*
        if (!stayAutoBlock.get() && blockSlot >= 0)
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
         */

        // Reset
        this.targetPlace = null;
    }

    /**
     * Disable scaffold module
     */
    @Override
    public void onDisable() {
        if (mc.getThePlayer() == null) return;

        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindSneak())) {
            mc.getGameSettings().getKeyBindSneak().setPressed(false);

            if (eagleSneaking)
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketEntityAction(mc.getThePlayer(), ICPacketEntityAction.WAction.STOP_SNEAKING));
        }

        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindRight()))
            mc.getGameSettings().getKeyBindRight().setPressed(false);

        if (!mc.getGameSettings().isKeyDown(mc.getGameSettings().getKeyBindLeft()))
            mc.getGameSettings().getKeyBindLeft().setPressed(false);
        if(autoTimer.get()){

            Timer timer = (Timer) LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
            timer.setState(false);
        }
        lockRotation = null;
        limitedRotation = null;
        facesBlock = false;
        mc.getTimer().setTimerSpeed(1F);
        shouldGoDown = false;

        if (slot != mc.getThePlayer().getInventory().getCurrentItem())
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
    }

    /**
     * Entity movement event
     *
     * @param event
     */
    @EventTarget
    public void onMove(final MoveEvent event) {
        if (!safeWalkValue.get() || shouldGoDown)
            return;

        if (airSafeValue.get() || mc.getThePlayer().getOnGround())
            event.setSafeWalk(true);
    }

    /**
     * Scaffold visuals
     *
     * @param event
     */
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (counterDisplayValue.get()) {
            progress = (float) (System.currentTimeMillis() - lastMS) / 100F;
            if (progress >= 1) progress = 1;
            final ScaledResolution scaledResolution = new ScaledResolution(mc2);
            final String info = getBlocksAmount() + " blocks";
            int infoWidth = Fonts.font40.getStringWidth(info);
            int infoWidth2 = Fonts.minecraftFont.getStringWidth(getBlocksAmount() + "");
            /*GL11.glPushMatrix();

            final BlockOverlay blockOverlay = (BlockOverlay) LiquidBounce.moduleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && blockOverlay.getInfoValue().get() && blockOverlay.getCurrentBlock() != null)
                GL11.glTranslatef(0, 15F, 0);

            final String info = "Blocks: §7" + getBlocksAmount();
            final IScaledResolution scaledResolution = classProvider.createScaledResolution(mc);

            RenderUtils.drawBorderedRect((scaledResolution.getScaledWidth() / 2.0f) - 2,
                    (scaledResolution.getScaledHeight() / 2.0f) + 5,
                    (scaledResolution.getScaledWidth() / 2.0f) + Fonts.font40.getStringWidth(info) + 2,
                    (scaledResolution.getScaledHeight() / 2.0f) + 16, 3, Color.BLACK.getRGB(), Color.BLACK.getRGB());

            classProvider.getGlStateManager().resetColor();

            Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2.0f,
                    scaledResolution.getScaledHeight() / 2.0f + 7, Color.WHITE.getRGB());

            GL11.glPopMatrix();*/
            GlStateManager.translate(0, -14F - (progress * 4F), 0);
            //GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glColor4f(0.15F, 0.15F, 0.15F, progress);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            GL11.glVertex2d(scaledResolution.getScaledWidth() / 2 - 3, scaledResolution.getScaledHeight() - 60);
            GL11.glVertex2d(scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() - 57);
            GL11.glVertex2d(scaledResolution.getScaledWidth() / 2 + 3, scaledResolution.getScaledHeight() - 60);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            //GL11.glPopMatrix();
            RenderUtils.drawRoundedRect(scaledResolution.getScaledWidth() / 2 - (infoWidth / 2) - 4, scaledResolution.getScaledHeight() - 60, scaledResolution.getScaledWidth() / 2 + (infoWidth / 2) + 4, scaledResolution.getScaledHeight() - 74, 2F, new Color(0.15F, 0.15F, 0.15F, progress).getRGB());
            GlStateManager.resetColor();
            Fonts.font35.drawCenteredString(info, scaledResolution.getScaledWidth() / 2 + 0.1F, scaledResolution.getScaledHeight() - 70, new Color(1F, 1F, 1F, 0.8F * progress).getRGB(), false);
            GlStateManager.translate(0, 14F + (progress * 4F), 0);
        }
    }

    /**
     * Scaffold visuals
     *
     * @param event
     */
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!markValue.get())
            return;

        for (int i = 0; i < (modeValue.get().equalsIgnoreCase("Expand") ? expandLengthValue.get() + 1 : 2); i++) {
            final WBlockPos blockPos = new WBlockPos(mc.getThePlayer().getPosX() + (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.WEST)) ? -i : mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.EAST)) ? i : 0), mc.getThePlayer().getPosY() - (mc.getThePlayer().getPosY() == (int) mc.getThePlayer().getPosY() + 0.5D ? 0D : 1.0D) - (shouldGoDown ? 1D : 0), mc.getThePlayer().getPosZ() + (mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.NORTH)) ? -i : mc.getThePlayer().getHorizontalFacing().equals(classProvider.getEnumFacing(EnumFacingType.SOUTH)) ? i : 0));
            final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                RenderUtils.drawBlockBox(blockPos, new Color(68, 117, 255, 100), false);
                break;
            }
        }
    }

    public static float[] getRotations(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc2.player.posX +  (double) face.getFrontOffsetX()/2;
        double z = block.getZ() + 0.5 - mc2.player.posZ +  (double) face.getFrontOffsetZ()/2;
        double y = (block.getY() + 0.5);
        double d1 = mc2.player.posY + mc2.player.getEyeHeight() - y;
        double d3 = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 82.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @param checks        visible
     * @return
     */
    private boolean search(final WBlockPos blockPosition, final boolean checks) {
        if (!BlockUtils.isReplaceable(blockPosition))
            return false;
        // StaticModes
        final boolean basicMode = rotationModeValue.get().equalsIgnoreCase("Basic");
        final boolean staticMode = rotationModeValue.get().equalsIgnoreCase("Static");
        final boolean staticPitchMode = staticMode || rotationModeValue.get().equalsIgnoreCase("StaticPitch");
        final boolean staticYawMode = staticMode || rotationModeValue.get().equalsIgnoreCase("StaticYaw");
        final float staticPitch = staticPitchValue.get();
        final float staticYawOffset = staticYawOffsetValue.get();

        // SearchRanges
        final double xzRV = xzRangeValue.get();
        final double xzSSV = calcStepSize(xzRV);
        final double yRV = yRangeValue.get();
        final double ySSV = calcStepSize(yRV);

        double xSearchFace = 0;
        double ySearchFace = 0;
        double zSearchFace = 0;


        final WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());

        PlaceRotation placeRotation = null;

        for (final EnumFacingType facingType : EnumFacingType.values()) {
            IEnumFacing side = classProvider.getEnumFacing(facingType);
            final WBlockPos neighbor = blockPosition.offset(side);

            if (!BlockUtils.canBeClicked(neighbor))
                continue;

            final WVec3 dirVec = new WVec3(side.getDirectionVec());

            for (double xSearch = 0.5D - (xzRV / 2); xSearch <= 0.5D + (xzRV / 2); xSearch += xzSSV) {
                for (double ySearch = 0.5D - (yRV / 2); ySearch <= 0.5D + (yRV / 2); ySearch += ySSV) {
                    for (double zSearch = 0.5D - (xzRV / 2); zSearch <= 0.5D + (xzRV / 2); zSearch += xzSSV) {
                        final WVec3 posVec = new WVec3(blockPosition).addVector(xSearch, ySearch, zSearch);
                        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        final WVec3 hitVec = posVec.add(new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5));

                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.getTheWorld().rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
                            continue;

                        // face block
                        for (int i = 0; i < (staticYawMode ? 2 : 1); i++) {
                            final double diffX = staticYawMode && i == 0 ? 0 : hitVec.getXCoord() - eyesPos.getXCoord();
                            final double diffY = hitVec.getYCoord() - eyesPos.getYCoord();
                            final double diffZ = staticYawMode && i == 1 ? 0 : hitVec.getZCoord() - eyesPos.getZCoord();

                            final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

//                            final float pitch = staticPitchMode ? staticPitch : WMathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)));
                            final float[] facePos = getRotations(new BlockPos(neighbor.getX(), neighbor.getY(), neighbor.getZ()), EnumFacingImplKt.unwrap(side));
                            float pitch;
                            if (staticPitchMode) {
                                pitch = staticPitch;
                            } else if (basicMode) {
                                pitch = facePos[1];
                            } else {
                                pitch = WMathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)));
                            }
                            float yaw;
                            double degrees = Math.toDegrees(Math.atan2(diffZ, diffX));
                            if (staticYawMode) {
                                yaw = WMathHelper.wrapAngleTo180_float((float) degrees - 90F +
                                        staticYawOffset);
                            } else if (basicMode) {
                                yaw = facePos[0];
                            } else {
                                yaw = WMathHelper.wrapAngleTo180_float((float) degrees - 90F);
                            }
                            final Rotation rotation = new Rotation(
                                   yaw , pitch);

                            final WVec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                            final WVec3 vector = eyesPos.addVector(rotationVector.getXCoord() * 4, rotationVector.getYCoord() * 4, rotationVector.getZCoord() * 4);
                            final IMovingObjectPosition obj = mc.getTheWorld().rayTraceBlocks(eyesPos, vector, false, false, true);

                            if (obj.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK || !obj.getBlockPos().equals(neighbor))
                                continue;

                            if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation())) {
                                placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                            }
                            xSearchFace = xSearch;
                            ySearchFace = ySearch;
                            zSearchFace = zSearch;
                        }
                    }
                }
            }
        }

        if (placeRotation == null || !shouldPlace()) return false;

        if (!rotationModeValue.get().equalsIgnoreCase("Off")) {
            if (minTurnSpeedValue.get() < 180) {
                limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, placeRotation.getRotation(), (float) (Math.random() * (maxTurnSpeedValue.get() - minTurnSpeedValue.get()) + minTurnSpeedValue.get()));
                setRotation(limitedRotation, keepLengthValue.get());
                lockRotation = limitedRotation;

                facesBlock = false;
                for (final EnumFacingType facingType : EnumFacingType.values()) {
                    IEnumFacing side = classProvider.getEnumFacing(facingType);
                    final WBlockPos neighbor = blockPosition.offset(side);

                    if (!BlockUtils.canBeClicked(neighbor))
                        continue;

                    final WVec3 dirVec = new WVec3(side.getDirectionVec());

                    final WVec3 posVec = new WVec3(blockPosition).addVector(xSearchFace, ySearchFace, zSearchFace);
                    final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                    final WVec3 hitVec = posVec.add(new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5));

                    if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.getTheWorld().rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
                        continue;

                    final WVec3 rotationVector = RotationUtils.getVectorForRotation(limitedRotation);
                    final WVec3 vector = eyesPos.addVector(rotationVector.getXCoord() * 4, rotationVector.getYCoord() * 4, rotationVector.getZCoord() * 4);
                    final IMovingObjectPosition obj = mc.getTheWorld().rayTraceBlocks(eyesPos, vector, false, false, true);

                    if (!(obj.getTypeOfHit() == IMovingObjectPosition.WMovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor)))
                        continue;
                    facesBlock = true;
                    break;
                }
            } else {
                lockRotation = placeRotation.getRotation();
                setRotation(lockRotation, keepLengthValue.get());
                facesBlock = true;
            }
        }
        targetPlace = placeRotation.getPlaceInfo();
        return true;
    }

    private double calcStepSize(double range) {
        double accuracy = searchAccuracyValue.get();
        accuracy += accuracy % 2; // If it is set to uneven it changes it to even. Fixes a bug
        if (range / accuracy < 0.01D)
            return 0.01D;
        return range / accuracy;
    }

    /**
     * @return hotbar blocks amount
     */
    private int getBlocksAmount() {
        int amount = 0;

        for (int i = 36; i < 45; i++) {
            final IItemStack itemStack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();

            if (itemStack != null && classProvider.isItemBlock(itemStack.getItem())) {
                final IBlock block = (itemStack.getItem().asItemBlock()).getBlock();

                IItemStack heldItem = mc.getThePlayer().getHeldItem();

                if (heldItem != null && heldItem.equals(itemStack) || !InventoryUtils.BLOCK_BLACKLIST.contains(block) && !classProvider.isBlockBush(block))
                    amount += itemStack.getStackSize();
            }
        }

        return amount;
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

    public void getBestBlocks(){

        if(getBlocksAmount() == 0)
            return;
        if(picker.get()){
            int bestInvSlot = getBiggestBlockSlotInv();
            int bestHotbarSlot = getBiggestBlockSlotHotbar();
            int bestSlot = getBiggestBlockSlotHotbar() > 0 ? getBiggestBlockSlotHotbar() : getBiggestBlockSlotInv();
            int spoofSlot = 42;
            if(bestHotbarSlot > 0 && bestInvSlot > 0){
                if (mc.getThePlayer().getInventoryContainer().getSlot(bestInvSlot).getHasStack() && mc.getThePlayer().getInventoryContainer().getSlot(bestHotbarSlot).getHasStack() ) {
                    if(mc.getThePlayer().getInventoryContainer().getSlot(bestHotbarSlot).getStack().getStackSize() < mc.getThePlayer().getInventoryContainer().getSlot(bestInvSlot).getStack().getStackSize()){
                        bestSlot = bestInvSlot;
                    }
                }
            }
            if(hotbarContainBlock()){
                for (int a = 36; a < 45; a++) {
                    if (mc.getThePlayer().getInventoryContainer().getSlot(a).getHasStack()) {
                        IItem item = mc.getThePlayer().getInventoryContainer().getSlot(a).getStack().getItem();
                        if(item instanceof IItemBlock){
                            spoofSlot = a;
                            break;
                        }
                    }
                }
            }else{
                for (int a = 36; a < 45; a++) {
                    if (!mc.getThePlayer().getInventoryContainer().getSlot(a).getHasStack()) {
                        spoofSlot = a;
                        break;
                    }
                }
            }

            if (mc.getThePlayer().getInventoryContainer().getSlot(spoofSlot).getSlotNumber() != bestSlot) {

                swap(bestSlot, spoofSlot - 36);
                mc.getPlayerController().updateController();


            }
        }else{
            if (invCheck()) {

                IItemStack is = functions.getItemById(261).getDefaultInstance();
                for (int i = 9; i < 36; i++) {

                    if (mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) {
                        IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
                        int count = 0;
                        if (item instanceof IItemBlock) {
                            for (int a = 36; a < 45; a++) {
                                if (functions.canAddItemToSlot(mc.getThePlayer().getInventoryContainer().getSlot(a), is, true)) {
                                    swap(i, a - 36);
                                    count++;
                                    break;
                                }
                            }

                            if (count == 0) {

                                swap(i, 7);
                            }
                            break;

                        }
                    }
                }
            }
        }
    }

    private boolean hotbarContainBlock() {
        int i = 36;

        while (i < 45) {
            try {
                IItemStack stack = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
                if ((stack == null) || (stack.getItem() == null) || !(stack.getItem() instanceof IItemBlock)) {
                    i++;
                    continue;
                }
                return true;
            } catch (Exception e) {

            }
        }

        return false;

    }

    public int getBiggestBlockSlotHotbar(){
        int slot = -1;
        int size = 0;
        if(getBlocksAmount() == 0)
            return - 1;
        for (int i = 36; i < 45; i++) {
            if (mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) {
                IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
                IItemStack is = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
                if (item instanceof IItemBlock) {
                    if(is.getStackSize() > size){
                        size = is.getStackSize();
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }

    protected void swap(int slot, int hotbarNum) {

        mc.getPlayerController().windowClick(mc.getThePlayer().getInventoryContainer().getWindowId(), slot, hotbarNum,2, mc.getThePlayer());

    }

    private boolean invCheck() {
        for (int i = 36; i < 45; i++) {
            if (mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) {
                IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
                if (item instanceof IItemBlock) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getBiggestBlockSlotInv(){
        int slot = -1;
        int size = 0;
        if(getBlocksAmount() == 0)
            return - 1;
        for (int i = 9; i < 36; i++) {
            if (mc.getThePlayer().getInventoryContainer().getSlot(i).getHasStack()) {
                IItem item = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack().getItem();
                IItemStack is = mc.getThePlayer().getInventoryContainer().getSlot(i).getStack();
                if (item instanceof IItemBlock) {
                    if(is.getStackSize() > size){
                        size = is.getStackSize();
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }
}
