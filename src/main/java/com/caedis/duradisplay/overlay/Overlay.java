package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.Config;
import com.caedis.duradisplay.render.OverlayRenderer;

@com.caedis.duradisplay.annotation.Overlay
public abstract class Overlay<C extends Config> {

    @NotNull
    abstract C config();

    @Nullable
    public OverlayRenderer getRenderer(@NotNull ItemStack itemStack) {
        return null;
    }
}
