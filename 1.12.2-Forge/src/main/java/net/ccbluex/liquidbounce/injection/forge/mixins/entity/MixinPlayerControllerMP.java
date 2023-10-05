package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.injection.backend.EntityImplKt;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.ccbluex.liquidbounce.utils.render.BlockAnimationUtils.thePlayerisBlocking;

@Mixin(PlayerControllerMP.class)
@SideOnly(Side.CLIENT)
public abstract class MixinPlayerControllerMP {

    @Shadow
    protected Minecraft mc;
    @Shadow
    private GameType currentGameType = GameType.SURVIVAL;
    @Shadow
    private NetHandlerPlayClient connection;

    @Shadow
    public abstract void syncCurrentPlayItem();

    @Shadow
    public abstract float getBlockReachDistance();

    @Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V"))
    private void attackEntity(EntityPlayer entityPlayer, Entity targetEntity, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new AttackEvent(EntityImplKt.wrap(targetEntity)));
    }

    @Inject(method = "getIsHittingBlock", at = @At("HEAD"), cancellable = true)
    private void getIsHittingBlock(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (LiquidBounce.moduleManager.getModule(AbortBreaking.class).getState())
            callbackInfoReturnable.setReturnValue(false);
    }

    /**
     * @author LvZiQiao
     * @reason ViaVersion Fix
     */
    @Overwrite
    public ItemStack windowClick(int p_windowClick_1_, int p_windowClick_2_, int p_windowClick_3_, ClickType p_windowClick_4_, EntityPlayer p_windowClick_5_) {
        final ClickWindowEvent event = new ClickWindowEvent(p_windowClick_1_, p_windowClick_2_, p_windowClick_3_, BackendExtentionsKt.toInt(p_windowClick_4_));
        LiquidBounce.eventManager.callEvent(event);

        if (event.isCancelled())
            return null;
        short short1 = p_windowClick_5_.openContainer.getNextTransactionID(p_windowClick_5_.inventory);
        ItemStack itemstack = p_windowClick_5_.openContainer.slotClick(p_windowClick_2_, p_windowClick_3_, p_windowClick_4_, p_windowClick_5_);
        this.connection.sendPacket(new CPacketConfirmTransaction(p_windowClick_1_, (short) 1, true));
        this.connection.sendPacket(new CPacketClickWindow(p_windowClick_1_, p_windowClick_2_, p_windowClick_3_, p_windowClick_4_, itemstack, short1));
        return itemstack;
    }

    /**
     * @author 1
     * @reason 1
     */
    @Overwrite
    public EnumActionResult processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand) {
        this.syncCurrentPlayItem();
        ItemStack itemstack = player.getHeldItem(hand);
        float f = (float) (vec.x - (double) pos.getX());
        float f1 = (float) (vec.y - (double) pos.getY());
        float f2 = (float) (vec.z - (double) pos.getZ());
        boolean flag = false;
        if (!this.mc.world.getWorldBorder().contains(pos)) {
            return EnumActionResult.FAIL;
        } else {
            PlayerInteractEvent.RightClickBlock event = ForgeHooks.onRightClickBlock(player, hand, pos, direction, ForgeHooks.rayTraceEyeHitVec(player, this.getBlockReachDistance() + 1.0F));
            if (event.isCanceled()) {
                this.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
                return event.getCancellationResult();
            } else {
                EnumActionResult result = EnumActionResult.PASS;
                if (this.currentGameType != GameType.SPECTATOR) {
                    EnumActionResult ret = itemstack.onItemUseFirst(player, worldIn, pos, hand, direction, f, f1, f2);
                    if (ret != EnumActionResult.PASS) {
                        this.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
                        return ret;
                    }

                    IBlockState iblockstate = worldIn.getBlockState(pos);
                    boolean bypass = player.getHeldItemMainhand().doesSneakBypassUse(worldIn, pos, player) && player.getHeldItemOffhand().doesSneakBypassUse(worldIn, pos, player);
                    if (!player.isSneaking() || bypass || event.getUseBlock() == Event.Result.ALLOW) {
                        if (event.getUseBlock() != Event.Result.DENY) {
                            flag = iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, direction, f, f1, f2);
                        }

                        if (flag) {
                            result = EnumActionResult.SUCCESS;
                        }
                    }

                    if (!flag && itemstack.getItem() instanceof ItemBlock) {
                        ItemBlock itemblock = (ItemBlock) itemstack.getItem();
                        if (!itemblock.canPlaceBlockOnSide(worldIn, pos, direction, player, itemstack)) {
                            return EnumActionResult.FAIL;
                        }
                    }
                }

                this.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
                if ((flag || this.currentGameType == GameType.SPECTATOR) && event.getUseItem() != Event.Result.ALLOW) {
                    return EnumActionResult.SUCCESS;
                } else if (itemstack.isEmpty()) {
                    return EnumActionResult.PASS;
                } else if (player.getCooldownTracker().hasCooldown(itemstack.getItem())) {
                    return EnumActionResult.PASS;
                } else {
                    if (itemstack.getItem() instanceof ItemBlock && !player.canUseCommandBlock()) {
                        Block block = ((ItemBlock) itemstack.getItem()).getBlock();
                        if (block instanceof BlockCommandBlock || block instanceof BlockStructure) {
                            return EnumActionResult.FAIL;
                        }
                    }

                    if (this.currentGameType.isCreative()) {
                        int i = itemstack.getMetadata();
                        int j = itemstack.getCount();
                        if (event.getUseItem() != Event.Result.DENY) {
                            EnumActionResult enumactionresult = itemstack.onItemUse(player, worldIn, pos, hand, direction, f, f1, f2);
                            itemstack.setItemDamage(i);
                            itemstack.setCount(j);
                            return enumactionresult;
                        } else {
                            return result;
                        }
                    } else {
                        ItemStack copyForUse = itemstack.copy();
                        if (event.getUseItem() != Event.Result.DENY) {
                            result = itemstack.onItemUse(player, worldIn, pos, hand, direction, f, f1, f2);
                        }

                        if (itemstack.isEmpty()) {
                            ForgeEventFactory.onPlayerDestroyItem(player, copyForUse, hand);
                        }

                        return result;
                    }
                }
            }
        }
    }

    /**
     * @author 1
     * @reason 1
     */
    @Overwrite
    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, EnumHand hand) {
        Animations ot = (Animations) LiquidBounce.moduleManager.getModule(Animations.class);
        ItemStack itemstack = player.getHeldItem(hand);
        ItemStack shield = new ItemStack(Items.SHIELD);
        if (this.currentGameType == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        } else {
            this.syncCurrentPlayItem();
            if (ot.getState() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                this.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
            } else {
                this.connection.sendPacket(new CPacketPlayerTryUseItem(hand));
            }
            thePlayerisBlocking = true;

            if (player.getCooldownTracker().hasCooldown(itemstack.getItem())) {
                return EnumActionResult.PASS;
            } else {
                EnumActionResult cancelResult = ForgeHooks.onItemRightClick(player, hand);
                if (cancelResult != null) {
                    return cancelResult;
                } else {
                    int i = itemstack.getCount();
                    ActionResult<ItemStack> actionresult = itemstack.useItemRightClick(worldIn, player, hand);
                    ItemStack itemstack1 = actionresult.getResult();
                    if (itemstack1 != itemstack || itemstack1.getCount() != i) {
                        player.setHeldItem(hand, itemstack1);
                        if (itemstack1.isEmpty()) {
                            ForgeEventFactory.onPlayerDestroyItem(player, itemstack, hand);
                        }
                    }

                    return actionresult.getType();
                }
            }
        }
    }

    /**
     * @author 1
     * @reason 1
     */
    @Overwrite
    public void onStoppedUsingItem(EntityPlayer playerIn) {
        final NoSlow noSlow = (NoSlow) LiquidBounce.moduleManager.getModule(NoSlow.class);
        thePlayerisBlocking = false;
        this.syncCurrentPlayItem();
        this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopActiveHand();
    }


}