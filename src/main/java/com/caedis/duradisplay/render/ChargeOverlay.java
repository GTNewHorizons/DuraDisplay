package com.caedis.duradisplay.render;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.client.gui.FontRenderer;
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
        ChargeOverlay chargeOverlay = new ChargeOverlay();
        Optional<Pair<Class<?>, Function<ItemStack, double[]>>> result = handlers.stream()
            .filter(
                p -> p.getLeft()
                    .isInstance(stack.getItem()))
            .findFirst();
        if (!result.isPresent()) {
            return null;
        }
        double[] d = result.get()
            .getRight()
            .apply(stack);
        if (d == null) {
            return null;
        }
        chargeOverlay.value = DurabilityFormatter.format(d[0], d[1], DurabilityFormatter.Format.percent);
        return chargeOverlay;
    }

    @Override
    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition, float zLevel) {
        if (!config.ShowWhenFull && this.isFull) return;
        super.Render(fontRenderer, xPosition, yPosition, zLevel);
    }

    @Override
    public int getColor() {
        return 0xFF55FFFF;
    }

    @Override
    public int getLocation() {
        return config.Position;
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
