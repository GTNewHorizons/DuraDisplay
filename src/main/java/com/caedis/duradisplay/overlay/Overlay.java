package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.Config;
import com.caedis.duradisplay.render.OverlayRenderer;

public abstract class Overlay<T extends Config> {

    abstract T config();

    @Nullable
    public OverlayRenderer getRenderer(@NotNull ItemStack itemStack) {
        return null;
    }
}
