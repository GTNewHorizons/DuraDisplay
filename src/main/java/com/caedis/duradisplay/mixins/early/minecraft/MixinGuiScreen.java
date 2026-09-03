package com.caedis.duradisplay.mixins.early.minecraft;

import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.duradisplay.render.DurabilityRenderer;

@Mixin(value = GuiScreen.class)
public class MixinGuiScreen {

    @Inject(method = "drawScreen(IIF)V", at = @At("HEAD"))
    private void drawScreenStart(CallbackInfo cbi) {
        DurabilityRenderer.Execute = false;
    }

    @Inject(method = "drawScreen(IIF)V", at = @At("RETURN"))
    private void drawScreenEnd(CallbackInfo cbi) {
        DurabilityRenderer.Execute = true;
    }
}
