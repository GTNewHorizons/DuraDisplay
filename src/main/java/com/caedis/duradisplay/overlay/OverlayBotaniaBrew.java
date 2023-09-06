package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;

import vazkii.botania.common.item.brew.ItemBrewBase;

public class OverlayBotaniaBrew extends OverlayDurabilityLike {

    public OverlayBotaniaBrew() {
        super(
            new ConfigDurabilityLike(
                true,
                OverlayDurabilityLike.Style.NumPad,
                DurabilityFormatter.Format.remain,
                2,
                true,
                true,
                0xFFFFFFFF,
                ColorType.Single,
                new double[] { 30, 70 },
                true,
                2) {

                @Override
                public void postLoadConfig() {
                    return;
                }

                @Override
                public @NotNull String category() {
                    return "botania_brew";
                }
            });
        addHandler("vazkii.botania.common.item.brew.ItemBrewBase", OverlayBotaniaBrew::handleBotaniaBrew);
    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    private static DurabilityLikeInfo handleBotaniaBrew(@NotNull ItemStack stack) {
        ItemBrewBase brew = ((ItemBrewBase) stack.getItem());
        assert brew != null;

        double current = brew.getSwigsLeft(stack);
        double max = brew.getMaxDamage();
        return new DurabilityLikeInfo(current, max);
    }
}
