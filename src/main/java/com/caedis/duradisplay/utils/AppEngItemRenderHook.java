package com.caedis.duradisplay.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.render.DurabilityRenderer;

import appeng.api.storage.IItemDisplayRegistry;
import appeng.core.Api;

public class AppEngItemRenderHook implements IItemDisplayRegistry.ItemRenderHook {

    private AppEngItemRenderHook() {}

    @Override
    public boolean renderOverlay(FontRenderer fr, TextureManager tm, ItemStack is, int x, int y) {
        if (!DurabilityRenderer.Execute) return false;
        if (!DuraDisplayConfig.Enable) return false;
        DurabilityRenderer.Render(fr, is, x, y);
        return false;
    }

    public static void init() throws NoClassDefFoundError {
        Api.INSTANCE.registries()
            .itemDisplay()
            .addPostItemRenderHook(new AppEngItemRenderHook());
    }

    @Override
    public boolean showDurability(ItemStack is) {
        var item = is.getItem();
        if (item == null) return false;
        return !DuraDisplayConfig.Enable && item.showDurabilityBar(is);
    }
}
