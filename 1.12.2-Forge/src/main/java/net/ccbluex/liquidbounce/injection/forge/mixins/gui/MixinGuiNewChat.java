/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.rename.modules.misc.animation.AnimationUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.font.yalan.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Shadow
    private int scrollPos;
    @Shadow
    private boolean isScrolled;

    @Shadow
    public abstract int getLineCount();

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getChatWidth();


    @Inject(method = "drawChat", at = @At("HEAD"), cancellable = true)
    private void drawChat(int updateCounter, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getState() && hud.getFontChatValue().get()) {
            callbackInfo.cancel();

            if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
            {
                int i = this.getLineCount();
                boolean flag = false;
                int j = 0;
                int k = this.drawnChatLines.size();
                float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

                if (k > 0)
                {
                    if (this.getChatOpen())
                    {
                        flag = true;
                    }

                    float f1 = this.getChatScale();
                    int l = MathHelper.ceil((float)this.getChatWidth() / f1);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(2.0F, 20.0F, 0.0F);
                    GlStateManager.scale(f1, f1, 1.0F);

                    ArrayList<ChatLine> chatLines = (ArrayList<ChatLine>) drawnChatLines.stream().filter(chatLine ->  {
                        int j1 = updateCounter - chatLine.getUpdatedCounter();
                        double d0 = (double)j1 / 200.0D;
                        d0 = 1.0D - d0;
                        d0 = d0 * 10.0D;
                        d0 = MathHelper.clamp(d0, 0.0D, 1.0D);
                        d0 = d0 * d0;
                        int l1 = (int)(255.0D * d0);
                        if (getChatOpen())
                        {
                            l1 = 255;
                        }
                        l1 = (int)((float)l1 * f);

                        return l1 > 3;
                    }).collect(Collectors.toList());

                    if (!(chatLines.size() >= 1)){
                        if (LiquidBounce.INSTANCE.getHeight() > 0){
                            LiquidBounce.INSTANCE.setHeight(AnimationUtils.smoothAnimation(LiquidBounce.INSTANCE.getHeight(),-14,60,0.3f));
                        }else{
                            if (LiquidBounce.INSTANCE.getHeight()!=-14){
                                LiquidBounce.INSTANCE.setHeight(AnimationUtils.smoothAnimation(LiquidBounce.INSTANCE.getHeight(),-14,60,0.3f));
                            }
                        }
                    }else{
                        LiquidBounce.INSTANCE.setHeight(AnimationUtils.smoothAnimation(LiquidBounce.INSTANCE.getHeight(),Math.min( getChatOpen() ? 240 : 120 , LiquidBounce.INSTANCE.getAniHeight()),60,0.3f));
                    }


                    for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1)
                    {
                        ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);

                        if (chatline != null)
                        {
                            int j1 = updateCounter - chatline.getUpdatedCounter();

                            if (j1 < 200 || flag)
                            {
                                double d0 = (double)j1 / 200.0D;
                                d0 = 1.0D - d0;
                                d0 = d0 * 10.0D;
                                d0 = MathHelper.clamp(d0, 0.0D, 1.0D);
                                d0 = d0 * d0;
                                int l1 = (int)(255.0D * d0);

                                if (flag)
                                {
                                    l1 = 255;
                                }

                                l1 = (int)((float)l1 * f);
                                ++j;

                                if (l1 > 3)
                                {
                                    int i2 = 0;
                                    int j2 = -i1 * 9;

                                    String s = chatline.getChatComponent().getFormattedText();
                                    GlStateManager.enableBlend();
                                    FontLoaders.F18.drawStringWithShadow(s, (float)i2 + 7, (float)(j2 - 3F), 16777215 + (l1 << 24));
                                    GlStateManager.disableAlpha();
                                    GlStateManager.disableBlend();
                                    LiquidBounce.INSTANCE.setAniHeight(Math.abs(j2) + 1.5F);
                                }
                            }
                        }
                    }


                    GlStateManager.popMatrix();
                }
            }
        }
    }
    // TODO: Make real fix
    /*@Inject(method = "setChatLine", at = @At("HEAD"), cancellable = true)
    private void setChatLine(IChatComponent p_setChatLine_1_, int p_setChatLine_2_, int p_setChatLine_3_, boolean p_setChatLine_4_, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

        if(hud.getState() && hud.fontChatValue.asBoolean()) {
            callbackInfo.cancel();

            if (p_setChatLine_2_ != 0) {
                this.deleteChatLine(p_setChatLine_2_);
            }

            int lvt_5_1_ = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
            List<IChatComponent> lvt_6_1_ = GuiUtilRenderComponents.splitText(p_setChatLine_1_, lvt_5_1_, FontLoaders.C18, false, false);
            boolean lvt_7_1_ = this.getChatOpen();

            IChatComponent lvt_9_1_;
            for(Iterator l = lvt_6_1_.iterator(); l.hasNext(); this.drawnChatLines.add(0, new ChatLine(p_setChatLine_3_, lvt_9_1_, p_setChatLine_2_))) {
                lvt_9_1_ = (IChatComponent)l.next();
                if (lvt_7_1_ && this.scrollPos > 0) {
                    this.isScrolled = true;
                    this.scroll(1);
                }
            }

            while(this.drawnChatLines.size() > 100) {
                this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
            }

            if (!p_setChatLine_4_) {
                this.chatLines.add(0, new ChatLine(p_setChatLine_3_, p_setChatLine_1_, p_setChatLine_2_));

                while(this.chatLines.size() > 100) {
                    this.chatLines.remove(this.chatLines.size() - 1);
                }
            }
        }
    }*/

    @Inject(method = "getChatComponent", at = @At("HEAD"), cancellable = true)
    private void getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_, final CallbackInfoReturnable<ITextComponent> callbackInfo) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

        if (hud.getState() && hud.getFontChatValue().get()) {
            if (this.getChatOpen()) {
                ScaledResolution lvt_3_1_ = new ScaledResolution(this.mc);
                int lvt_4_1_ = lvt_3_1_.getScaleFactor();
                float lvt_5_1_ = this.getChatScale();
                int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
                int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
                lvt_6_1_ = MathHelper.floor((float) lvt_6_1_ / lvt_5_1_);
                lvt_7_1_ = MathHelper.floor((float) lvt_7_1_ / lvt_5_1_);
                if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                    int l = Math.min(this.getLineCount(), this.drawnChatLines.size());
                    if (lvt_6_1_ <= MathHelper.floor((float) this.getChatWidth() / this.getChatScale()) && lvt_7_1_ < FontLoaders.F18.FONT_HEIGHT * l + l) {
                        int lvt_9_1_ = lvt_7_1_ / FontLoaders.F18.FONT_HEIGHT + this.scrollPos;
                        if (lvt_9_1_ >= 0 && lvt_9_1_ < this.drawnChatLines.size()) {
                            ChatLine lvt_10_1_ = this.drawnChatLines.get(lvt_9_1_);
                            int j1 = 0;

                            for (ITextComponent lvt_13_1_ : lvt_10_1_.getChatComponent()) {
                                if (lvt_13_1_ instanceof TextComponentString) {
                                    j1 += FontLoaders.F18.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString) lvt_13_1_).getText(), false));
                                    if (j1 > lvt_6_1_) {
                                        callbackInfo.setReturnValue(lvt_13_1_);
                                        return;
                                    }
                                }
                            }
                        }

                    }
                }

            }

            callbackInfo.setReturnValue(null);
        }
    }
}
