package com.caedis.duradisplay.mixins.early.minecraft;

import net.minecraft.client.gui.inventory.GuiContainerCreative;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.duradisplay.render.DurabilityRenderer;

@Mixin(value = GuiContainerCreative.class)
public class MixinGuiContainerCreative {

    @Inject(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("HEAD"))
    private void drawBackgroundLayerStart(CallbackInfo cbi) {
        DurabilityRenderer.Execute = false;
    }

    @Inject(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("RETURN"))
    private void drawBackgroundLayerEnd(CallbackInfo cbi) {
        DurabilityRenderer.Execute = true;
    }
}
