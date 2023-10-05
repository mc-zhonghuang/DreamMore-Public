package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.HappyMan;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.rename.modules.misc.ComponentOnHover;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.injection.backend.ClassProviderImpl;
import net.ccbluex.liquidbounce.utils.AutoLMsg;
import net.ccbluex.liquidbounce.utils.render.ParticleUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Mixin(GuiScreen.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiScreen {
    @Shadow
    public Minecraft mc;

    @Shadow
    public List<GuiButton> buttonList;

    @Shadow
    public int width;

    @Shadow
    protected List<GuiLabel> labelList = Lists.newArrayList();

    @Shadow
    public int height;
    @Shadow
    public FontRenderer fontRenderer;
    @Shadow
    public int eventButton;
    @Shadow
    public long lastMouseEvent;
    @Shadow
    public int touchValue;
    @Shadow
    protected GuiButton selectedButton;


    @Shadow
    public void updateScreen() {
    }

    @Shadow
    protected abstract void handleComponentHover(ITextComponent component, int x, int y);

    @Shadow
    public abstract void drawHoveringText(List<String> textLines, int x, int y);

    @Shadow
    public abstract void drawDefaultBackground();

    /**
     * @author LvZiQiao
     * @reason Call Invest gui event
     */
    @Overwrite
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        int j;
        for(j = 0; j < this.buttonList.size(); ++j) {
            ((GuiButton)this.buttonList.get(j)).drawButton(this.mc, p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        }

        for(j = 0; j < this.labelList.size(); ++j) {
            ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, p_drawScreen_1_, p_drawScreen_2_);
        }
        if (LiquidBounce.getInvest() != null) LiquidBounce.getInvest().draw(p_drawScreen_1_, p_drawScreen_2_);
    }

    @Inject(method = "drawWorldBackground", at = @At("HEAD"))
    private void drawWorldBackground(final CallbackInfo callbackInfo) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

        if (hud.getInventoryParticle().get() && mc.player != null) {
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            final int width = scaledResolution.getScaledWidth();
            final int height = scaledResolution.getScaledHeight();
            ParticleUtils.drawParticles(Mouse.getX() * width / mc.displayWidth, height - Mouse.getY() * height / mc.displayHeight - 1);
        }
    }

    /**
     * @author CCBlueX
     * @reason 1
     */
    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void drawClientBackground(final CallbackInfo callbackInfo) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();

        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        RenderUtils.drawImage(ClassProviderImpl.INSTANCE.createResourceLocation("more/bg.png"), 0, 0, width, height);

        ParticleUtils.drawParticles(Mouse.getX() * width / mc.displayWidth, height - Mouse.getY() * height / mc.displayHeight - 1);
        callbackInfo.cancel();
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void messageSend(String msg, boolean addToChat, final CallbackInfo callbackInfo) {
        if (msg.startsWith(String.valueOf(LiquidBounce.commandManager.getPrefix())) && addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);

            LiquidBounce.commandManager.executeCommands(msg);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "handleComponentHover", at = @At("HEAD"))
    private void handleHoverOverComponent(ITextComponent component, int x, int y, final CallbackInfo callbackInfo) {
        if (component == null || component.getStyle().getClickEvent() == null || !LiquidBounce.moduleManager.getModule(ComponentOnHover.class).getState())
            return;

        final Style chatStyle = component.getStyle();

        final ClickEvent clickEvent = chatStyle.getClickEvent();
        final HoverEvent hoverEvent = chatStyle.getHoverEvent();

        drawHoveringText(Collections.singletonList("§c§l" + clickEvent.getAction().getCanonicalName().toUpperCase() + ": §a" + clickEvent.getValue()), x, y - (hoverEvent != null ? 17 : 0));
    }

    /**
     * @author CCBlueX (superblaubeere27)
     * @reason Making it possible for other mixins to receive actions
     */
    @Overwrite
    protected void actionPerformed(GuiButton button) throws IOException {
        this.injectedActionPerformed(button);
    }

    /**
     * @author LvZiQiao
     * @reason Call Invest gui event
     */
    @Overwrite
    public void handleMouseInput() throws IOException {
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int k = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            HappyMan.fuck1((GuiScreen) (Object) this, i, j, k);
            this.mouseClicked(i, j, this.eventButton);
        } else if (k != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }

            HappyMan.fuck2((GuiScreen) (Object) this, i, j, k);
            this.mouseReleased(i, j, k);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long l = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(i, j, this.eventButton, l);
        }
    }

    /**
     * @author LvZiQiao
     * @reason Call Invest gui event
     */
    @Overwrite
    public void handleKeyboardInput() throws IOException {
        char c0 = Keyboard.getEventCharacter();
        if (Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState()) {
            if (LiquidBounce.getInvest() != null) LiquidBounce.getInvest().keyPress(c0, Keyboard.getEventKey());
            this.keyTyped(c0, Keyboard.getEventKey());
        }

        this.mc.dispatchKeypresses();
    }

    /**
     * @author LvZiQiao
     * @reason Can't be clicked
     */
    @Overwrite
    protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        if (HappyMan.fuck3(p_mouseClicked_3_)) {
            for(int i = 0; i < this.buttonList.size(); ++i) {
                GuiButton guibutton = (GuiButton)this.buttonList.get(i);
                if (guibutton.mousePressed(this.mc, p_mouseClicked_1_, p_mouseClicked_2_)) {
                    GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre((GuiScreen) (Object) this, guibutton, this.buttonList);
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        break;
                    }

                    guibutton = event.getButton();
                    this.selectedButton = guibutton;
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                    if (this.equals(this.mc.currentScreen)) {
                        MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post((GuiScreen) (Object) this, event.getButton(), this.buttonList));
                    }
                }
            }
        }
    }

    @Shadow
    protected abstract void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_);

    @Shadow
    protected abstract void mouseClickMove(int p_mouseClickMove_1_, int p_mouseClickMove_2_, int p_mouseClickMove_3_, long p_mouseClickMove_4_);

    @Shadow
    protected abstract void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException;

    protected void injectedActionPerformed(GuiButton button) {
    }
}