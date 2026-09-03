package com.caedis.duradisplay.mixins.early.minecraft;

import net.minecraft.client.gui.inventory.GuiContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.duradisplay.render.OverlayRenderer;

// Batches the slot loop into one font flush. Also covers AE2, whose AEBaseGui extends GuiContainer.
@Mixin(value = GuiContainer.class)
public class MixinGuiContainer {

    @Inject(
        method = "drawScreen(IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderHelper;enableGUIStandardItemLighting()V"))
    private void drawSlotsBatchStart(int mouseX, int mouseY, float partialTicks, CallbackInfo cbi) {
        OverlayRenderer.beginBatch();
    }

    @Inject(
        method = "drawScreen(IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerForegroundLayer(II)V"))
    private void drawSlotsBatchEnd(int mouseX, int mouseY, float partialTicks, CallbackInfo cbi) {
        OverlayRenderer.endBatch();
    }
}
