package com.caedis.duradisplay.mixins.minecraft;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.render.DurabilityRenderer;

@Mixin(value = RenderItem.class)
public abstract class MixinRenderItem {

    @SuppressWarnings({ "UnresolvedMixinReference", "mapping" })
    @Redirect(
        method = "renderItemOverlayIntoGUI(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;showDurabilityBar(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean showDurabilityBar(Item item0, ItemStack stack0, FontRenderer fontRenderer,
        TextureManager textureManager, ItemStack stack, int xPosition, int yPosition, String string) {
        var item = stack.getItem();
        assert item != null;
        if (!DurabilityRenderer.Execute) return item.showDurabilityBar(stack);
        if (!DuraDisplayConfig.Enable) return item.showDurabilityBar(stack);

        DurabilityRenderer.Render(fontRenderer, stack0, xPosition, yPosition);
        return false;
    }

}
