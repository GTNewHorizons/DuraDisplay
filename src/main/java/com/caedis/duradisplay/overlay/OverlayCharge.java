package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;
import com.caedis.duradisplay.utils.ModSelfDrawnBar;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class OverlayCharge extends OverlayDurabilityLike {

    public OverlayCharge() {
        super(
            new ConfigDurabilityLike(
                true,
                OverlayDurabilityLike.Style.NumPad,
                DurabilityFormatter.Format.percent,
                8,
                false,
                true,
                0xFF55FFFF,
                ColorType.Single,
                new double[] { 30, 70 },
                true,
                2) {

                @Override
                public void postLoadConfig() {
                    if (enabled && DuraDisplayConfig.Enable) ModSelfDrawnBar.changeChargebar(false);
                    else ModSelfDrawnBar.restoreChargebar();
                }

                @Override
                public @NotNull String category() {
                    return "charge";
                }
            });
        addHandler("ic2.api.item.IElectricItem", OverlayCharge::handleIElectricItem);
        addHandler("tconstruct.library.tools.ToolCore", OverlayCharge::handleToolCore);
        addHandler("cofh.api.energy.IEnergyContainerItem", OverlayCharge::handleEnergyContainer);
    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    private static DurabilityLikeInfo handleIElectricItem(@NotNull ItemStack stack) {
        IElectricItem bei = ((IElectricItem) stack.getItem());
        assert bei != null;

        return new DurabilityLikeInfo(ElectricItem.manager.getCharge(stack), bei.getMaxCharge(stack));
    }

    private static DurabilityLikeInfo handleEnergyContainer(@NotNull ItemStack stack) {

        IEnergyContainerItem eci = ((IEnergyContainerItem) stack.getItem());
        assert eci != null;

        return new DurabilityLikeInfo(eci.getEnergyStored(stack), eci.getMaxEnergyStored(stack));
    }

    private static DurabilityLikeInfo handleToolCore(@NotNull ItemStack stack) {

        if (!stack.hasTagCompound() && !stack.getTagCompound()
            .hasKey("Energy")) return null;
        return handleEnergyContainer(stack);
    }
}
