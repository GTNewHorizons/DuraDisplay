package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;
import com.caedis.duradisplay.utils.Mods;

import vazkii.botania.common.item.brew.ItemBrewBase;

// Overlay for brew and potions
// currently Botania brew and Blood Magic AlchemyFlask
@SuppressWarnings("unused")
public class OverlayPotionBrew extends OverlayDurabilityLike {

    @SuppressWarnings("unused")
    public OverlayPotionBrew() {
        super(
            new ConfigDurabilityLike(
                true,
                OverlayDurabilityLike.Style.Text,
                DurabilityFormatter.Format.remaining,
                2,
                true,
                true,
                0xFFFFFF,
                ColorType.Single,
                new double[] { 30, 70 },
                new int[] { 0xFF0000, 0xFFBDC8, 0xFFFFFF },
                true,
                2,
                true) {

                @Override
                public void postLoadConfig() {
                    configCategory.setComment("""
                        PotionBrew is the module that shows the remaining swigs of potions and brews
                        currently Botania brews and Blood Magic AlchemyFlask
                                                                        """);
                }

                @Override
                public @NotNull String category() {
                    return "potion_brew";
                }
            });

        if (Mods.Botania.isLoaded())
            addHandler("vazkii.botania.common.item.brew.ItemBrewBase", OverlayPotionBrew::handleBotaniaBrew);

        if (Mods.AlchemicalWizardry.isLoaded()) addHandler(
            "WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask",
            OverlayDurability::handleDefault);

    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    public static DurabilityLikeInfo handleBotaniaBrew(@NotNull ItemStack stack) {
        ItemBrewBase brew = ((ItemBrewBase) stack.getItem());
        assert brew != null;

        double current = brew.getSwigsLeft(stack);
        double max = brew.getMaxDamage();
        return new DurabilityLikeInfo(current, max);
    }
}
