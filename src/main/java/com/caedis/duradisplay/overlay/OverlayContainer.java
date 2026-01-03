package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;
import com.caedis.duradisplay.utils.ModSelfDrawnBar;

import cpw.mods.fml.common.Loader;
import forestry.core.inventory.ItemInventory;
import forestry.storage.items.ItemBackpack;

@SuppressWarnings("unused")
public class OverlayContainer extends OverlayDurabilityLike {

    private static boolean showFullness = false;

    public OverlayContainer() {
        super(new ConfigContainer());
        if (Loader.isModLoaded("Forestry"))
            addHandler("forestry.storage.items.ItemBackpack", OverlayContainer::handleForestryBackpack);
    }

    public static DurabilityLikeInfo handleForestryBackpack(@NotNull ItemStack stack) {
        ItemBackpack backpack = (ItemBackpack) stack.getItem();
        assert backpack != null;

        final int backpackSize = backpack.getBackpackSize();
        final int used = ItemInventory.getOccupiedSlotCount(stack);
        final int remaining = backpackSize - used;
        if (showFullness) {
            return new DurabilityLikeInfo(used, backpackSize);
        } else {
            return new DurabilityLikeInfo(remaining, backpackSize);
        }

    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    private static class ConfigContainer extends ConfigDurabilityLike {

        public ConfigContainer() {
            super(
                true,
                Style.Text,
                DurabilityFormatter.Format.container,
                2,
                false,
                true,
                0x00FF00,
                ColorType.RYGDurability,
                new double[] { 30, 70 },
                new int[] { 0xFF0000, 0x55FF00, 0x00FF00 },
                true,
                0,
                true);
        }

        @Override
        public void loadConfig() {
            super.loadConfig();

            showFullness = config.getBoolean(
                "ShowFullness",
                category(),
                false,
                "Makes containers show how many slots are taken on true, how many slots are remaining when false");
        }

        @Override
        public void postLoadConfig() {
            if (enabled && DuraDisplayConfig.Enable) ModSelfDrawnBar.changeDurabilitybar(false);
            else ModSelfDrawnBar.restoreDurabilitybar();
            configCategory.setComment("""
                Container shows slots used out of remaining
                                                                """);
        }

        @Override
        public @NotNull String category() {
            return "container";
        }
    }
}
