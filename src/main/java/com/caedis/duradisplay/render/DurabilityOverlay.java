package com.caedis.duradisplay.render;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.config.DurabilityOverlayConfig;
import com.caedis.duradisplay.utils.DurabilityFormatter;

import appeng.items.tools.powered.powersink.AEBasePoweredItem;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_RadioactiveCell_Item;
import ic2.api.item.ICustomDamageItem;
import ic2.api.item.IElectricItem;
import ic2.core.item.armor.ItemArmorFluidTank;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.weaponry.AmmoItem;

public class DurabilityOverlay extends ItemStackOverlay {

    private final double durability;

    private final double maxDurability;

    public DurabilityOverlay(double durability, double maxDurability) {
        this.durability = durability;
        this.maxDurability = maxDurability;
    }

    public static final DurabilityOverlayConfig config = new DurabilityOverlayConfig("durability", 2);

    private static final ArrayList<Pair<Class<?>, Function<ItemStack, double[]>>> handlers = new ArrayList<>();

    static {
        DuraDisplayConfig.addConfig(config);
        handlers.add(Pair.of(GT_MetaBase_Item.class, DurabilityOverlay::handleGregTech));
        handlers.add(Pair.of(GT_RadioactiveCell_Item.class, DurabilityOverlay::handleGregTechRadioactiveCell));
        handlers.add(Pair.of(AmmoItem.class, i -> null));
        handlers.add(Pair.of(AEBasePoweredItem.class, i -> null));
        handlers.add(Pair.of(IElectricItem.class, i -> null));
        handlers.add(Pair.of(ToolCore.class, DurabilityOverlay::handleToolCore));
        handlers.add(Pair.of(ItemArmorFluidTank.class, DurabilityOverlay::handleItemArmorFluidTank));
        handlers.add(Pair.of(ICustomDamageItem.class, DurabilityOverlay::handleICustomDamageItem));
        handlers.add(Pair.of(Item.class, DurabilityOverlay::handleDefault));
        DurabilityRenderer.addHandlers(DurabilityOverlay::getOverlayHandler);
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
            DurabilityOverlay durabilityOverlay = new DurabilityOverlay(d[0], d[1]);
            if (!config.ShowWhenFull && d[0] == d[1]) return null;
            if (!config.ShowWhenEmpty && (int) d[0] == 0) return null;
            return durabilityOverlay;
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
        double percent = durability / maxDurability;
        if (!config.UseColorThreshold) return Color.HSBtoRGB(Math.max(0.0F, (float) percent) / 3.0F, 1.0F, 1.0F);
        else {
            double dur = percent * 100;
            if (dur <= config.ColorThreshold[0]) {
                return 0xFF0000;
            } else if (dur >= config.ColorThreshold[config.ColorThreshold.length - 1]) {
                return 0x55FF00;
            } else {
                return 0XFFD500;
            }
        }
    }

    @Override
    public int getLocation() {
        return config.Position;
    }

    private static double[] handleDefault(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        assert item != null;

        if (!item.isDamageable()) return null;

        double damagePercent = item.getDurabilityForDisplay(stack);
        double max = item.getMaxDamage();
        double current = max * (1 - damagePercent);
        return new double[] { current, max };
    }

    private static double[] handleGregTech(@NotNull ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        NBTTagCompound nbt = stack.getTagCompound();

        if (!nbt.hasKey("GT.ToolStats")) {
            return null;
        } else {

            NBTTagCompound ts = nbt.getCompoundTag("GT.ToolStats");
            double damage = ts.getLong("Damage");
            double max = ts.getLong("MaxDamage");
            double current = max - damage;
            return new double[] { current, max };
        }

    }

    private static double[] handleToolCore(@NotNull ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound()
            .hasKey("InfiTool")) return null;
        NBTTagCompound tags = stack.getTagCompound()
            .getCompoundTag("InfiTool");

        if (tags.getInteger("Unbreaking") < 10) {
            return handleDefault(stack);
        }

        return null;
    }

    private static double[] handleGregTechRadioactiveCell(@NotNull ItemStack stack) {
        GT_RadioactiveCell_Item bei = ((GT_RadioactiveCell_Item) stack.getItem());

        assert bei != null;

        double damage = bei.getDamageOfStack(stack);
        double max = bei.getMaxDamageEx();
        double current = max - damage;
        return new double[] { current, max };
    }

    private static double[] handleItemArmorFluidTank(@NotNull ItemStack stack) {
        ItemArmorFluidTank bei = ((ItemArmorFluidTank) stack.getItem());
        assert bei != null;

        return new double[] { bei.getCharge(stack), bei.getCapacity(stack) };
    }

    private static double[] handleICustomDamageItem(@NotNull ItemStack stack) {
        ICustomDamageItem bei = ((ICustomDamageItem) stack.getItem());
        assert bei != null;

        double damage = bei.getCustomDamage(stack);
        double max = bei.getMaxCustomDamage(stack);
        double current = max - damage;
        return new double[] { current, max };
    }

}
