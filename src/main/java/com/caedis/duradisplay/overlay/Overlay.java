package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.Config;
import com.caedis.duradisplay.render.OverlayRenderer;

/**
 * <p>
 * The base type of all Overlays
 * <p/>
 * By extending this, non-abstract classes will be automatically registered as an Overlay
 * with config automatically loaded at launch and registered in the GUI
 * 
 * @param <C> Config type extending {@link Config}
 */
@com.caedis.duradisplay.annotation.Overlay
public abstract class Overlay<C extends Config> {

    @NotNull
    abstract C config();

    @Nullable
    public abstract OverlayRenderer getRenderer(@NotNull ItemStack itemStack);
}
