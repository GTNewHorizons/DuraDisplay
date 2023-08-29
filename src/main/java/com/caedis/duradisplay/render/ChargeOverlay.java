package com.caedis.duradisplay.render;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.config.DurabilityOverlayConfig;
import com.caedis.duradisplay.utils.DurabilityFormatter;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import tconstruct.library.tools.ToolCore;

public class ChargeOverlay extends ItemStackOverlay {

    private final double durability;

    private final double maxDurability;

    public ChargeOverlay(double durability, double maxDurability) {
        this.durability = durability;
        this.maxDurability = maxDurability;
    }

    public static final DurabilityOverlayConfig config = new DurabilityOverlayConfig("charge", 8);
    private static final ArrayList<Pair<Class<?>, Function<ItemStack, double[]>>> handlers = new ArrayList<>();

    static {
        DuraDisplayConfig.addConfig(config);
        handlers.add(Pair.of(IElectricItem.class, ChargeOverlay::handleIElectricItem));
        handlers.add(Pair.of(ToolCore.class, ChargeOverlay::handleToolCore));
        handlers.add(Pair.of(IEnergyContainerItem.class, ChargeOverlay::handleEnergyContainer));
        DurabilityRenderer.addHandlers(ChargeOverlay::getOverlayHandler);
    }

    @Nullable
    private static ItemStackOverlay getOverlayHandler(ItemStack stack) {
        if (!config.Enabled) return null;
        Optional<Pair<Class<?>, Function<ItemStack, double[]>>> result = handlers.stream()
            .filter(
                p -> p.getLeft()
                    .isInstance(stack.getItem()))
            .findFirst();
        if (result.isPresent()) {
            double[] d = result.get()
                .getRight()
                .apply(stack);
            if (d == null) {
                return null;
            }
            ChargeOverlay chargeOverlay = new ChargeOverlay(d[0], d[1]);
            if (!config.ShowWhenFull && d[0] == d[1]) return null;
            if (!config.ShowWhenEmpty && d[0] == 0) return null;
            return chargeOverlay;
        } else {
            return null;
        }
    }

    @Override
    public String getValue() {
        return DurabilityFormatter.format(durability, maxDurability, DurabilityFormatter.Format.percent);
    }

    @Override
    public int getColor() {
        return 0xFF55FFFF;
    }

    @Override
    public int getLocation() {
        return config.NumPadPosition;
    }

    private static double[] handleIElectricItem(@NotNull ItemStack stack) {
        IElectricItem bei = ((IElectricItem) stack.getItem());
        assert bei != null;

        return new double[] { ElectricItem.manager.getCharge(stack), bei.getMaxCharge(stack) };
    }

    private static double[] handleEnergyContainer(@NotNull ItemStack stack) {

        IEnergyContainerItem eci = ((IEnergyContainerItem) stack.getItem());
        assert eci != null;

        return new double[] { eci.getEnergyStored(stack), eci.getMaxEnergyStored(stack) };
    }

    private static double[] handleToolCore(@NotNull ItemStack stack) {

        if (!stack.hasTagCompound() && !stack.getTagCompound()
            .hasKey("Energy")) return null;
        return handleEnergyContainer(stack);
    }

}
