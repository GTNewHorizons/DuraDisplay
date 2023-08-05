package com.caedis.duradisplay.render;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ItemHandler {

    @Nullable
    ItemStackOverlay getOverlay(@NotNull ItemStack stack);

}
