package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;
import com.caedis.duradisplay.utils.ModSelfDrawnBar;
import com.caedis.duradisplay.utils.Mods;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class OverlayCharge extends OverlayDurabilityLike {

    public OverlayCharge() {
        super(
            new ConfigDurabilityLike(
                true,
                OverlayDurabilityLike.Style.Text,
                DurabilityFormatter.Format.percent,
                8,
                true,
                true,
                0xFF55FF,
                ColorType.Smooth,
                new double[] { 30, 70 },
                new int[] { 0xFFB9AA, 0xBDD6FF, 0x55FFFF },
                true,
                2,
                true) {

                @Override
                public void postLoadConfig() {
                    if (enabled && DuraDisplayConfig.Enable) ModSelfDrawnBar.changeChargebar(false);
                    else ModSelfDrawnBar.restoreChargebar();
                    configCategory.setComment("""
                        Charge is the module that shows charge(Electricity/Power) of items
                        GT EU, IC2 EU, RF included
                                                                    """);
                }

                @Override
                public @NotNull String category() {
                    return "charge";
                }
            });
        if (Mods.IC2.isLoaded()) addHandler("ic2.api.item.IElectricItem", OverlayCharge::handleIElectricItem);
        if (Mods.TinkersConstruct.isLoaded())
            addHandler("tconstruct.library.tools.ToolCore", OverlayCharge::handleToolCore);
        if (Mods.CofhCore.isLoaded())
            addHandler("cofh.api.energy.IEnergyContainerItem", OverlayCharge::handleEnergyContainer);
    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    public static DurabilityLikeInfo handleIElectricItem(@NotNull ItemStack stack) {
        IElectricItem bei = ((IElectricItem) stack.getItem());
        assert bei != null;

        return new DurabilityLikeInfo(ElectricItem.manager.getCharge(stack), bei.getMaxCharge(stack));
    }

    public static DurabilityLikeInfo handleEnergyContainer(@NotNull ItemStack stack) {

        IEnergyContainerItem eci = ((IEnergyContainerItem) stack.getItem());
        assert eci != null;

        return new DurabilityLikeInfo(eci.getEnergyStored(stack), eci.getMaxEnergyStored(stack));
    }

    public static DurabilityLikeInfo handleToolCore(@NotNull ItemStack stack) {

        if (!stack.hasTagCompound() || !stack.getTagCompound()
            .hasKey("Energy")) return null;
        return handleEnergyContainer(stack);
    }
}
