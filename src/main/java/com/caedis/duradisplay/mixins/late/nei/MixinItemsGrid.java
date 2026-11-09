package com.caedis.duradisplay.mixins.late.nei;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.caedis.duradisplay.render.OverlayRenderer;

import codechicken.nei.ItemsGrid;

// Batches NEI's item, bookmark and craftables panels, which draw outside the slot loop.
@Mixin(value = ItemsGrid.class, remap = false)
public class MixinItemsGrid {

    @Inject(method = "drawItems()V", at = @At("HEAD"))
    private void drawItemsBatchStart(CallbackInfo cbi) {
        OverlayRenderer.beginBatch();
    }

    @Inject(method = "drawItems()V", at = @At("RETURN"))
    private void drawItemsBatchEnd(CallbackInfo cbi) {
        OverlayRenderer.endBatch();
    }
}
