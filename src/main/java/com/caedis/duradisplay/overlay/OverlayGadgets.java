package com.caedis.duradisplay.overlay;

import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;
import com.google.common.collect.Sets;

// Gadgets are items to show UseCount(remain) as default
// GT Lighter and GT Paint Sprayer for example
public class OverlayGadgets extends OverlayDurabilityLike {

    public OverlayGadgets() {
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
                        Gadgets are items that show UseCount(remain) as default
                        including some items whose maxDurability<100
                        and GregTech lighters and Paint Sprayer
                                                                        """);
                }

                @Override
                public @NotNull String category() {
                    return "gadgets";
                }
            });
        addHandler("gregtech.api.items.GT_MetaBase_Item", OverlayGadgets::handleGregtech);
        addHandler("buildcraft.core.ItemPaintbrush", OverlayGadgets::handleBCBrush);
        addHandler("tmechworks.items.SpoolOfWire", OverlayGadgets::handleMechworks);
        addHandler("ic2.core.item.tool.ItemToolPainter", OverlayDurability::handleDefault);
        addHandler("WayofTime.alchemicalWizardry.common.items.ScribeTool", OverlayDurability::handleDefault);
        addHandler("thaumcraft.api.IScribeTools", OverlayDurability::handleDefault);
        addHandler("net.minecraft.item.Item", OverlayGadgets::handleByAllowList);
    }

    public static final Set<String> AllowListUnLocalized = Sets.newHashSet(
        "item.flintAndSteel",
        "ic2.itemWeedEx",
        "item.for.waxCast",
        "item.for.solderingIron",
        "ic2.itemTreetap",
        "item.appliedenergistics2.ToolCertusQuartzCuttingKnife",
        "item.appliedenergistics2.ToolNetherQuartzCuttingKnife",
        "ic2.itemToolForgeHammer",
        "item.spellCloth",
        "item.WoodenBrickForm");

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    private static DurabilityLikeInfo handleMechworks(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        assert item != null;

        double max = item.getMaxDamage();
        double current = max - item.getDamage(stack);
        return new DurabilityLikeInfo(current, max);
    }

    @Nullable
    public static DurabilityLikeInfo handleBCBrush(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        assert item != null;

        if (!stack.hasTagCompound()) return null;
        double max = item.getMaxDamage() + 1;
        double current = max - item.getDamage(stack);
        return new DurabilityLikeInfo(current, max);
    }

    @Nullable
    public static DurabilityLikeInfo handleByAllowList(@NotNull ItemStack stack) {
        if (!AllowListUnLocalized.contains(stack.getUnlocalizedName())) return null;
        Item item = stack.getItem();
        assert item != null;

        if (!item.isDamageable()) return null;

        double max = item.getMaxDamage();
        double current = max - item.getDamage(stack);
        return new DurabilityLikeInfo(current, max);
    }

    @Nullable
    public static DurabilityLikeInfo handleGregtech(@NotNull ItemStack stack) {
        long max;
        long current = 0;
        if (stack.stackSize != 1) return null;
        var damage = stack.getItemDamage();
        switch (damage) {
            case 32472 -> max = 16;
            case 32473 -> {
                max = 16;
                current = max;
            }
            case 32474, 32475 -> max = 100;
            case 32476 -> {
                max = 100;
                current = max;
            }
            case 32477, 32478 -> max = 1000;
            case 32479 -> {
                max = 1000;
                current = max;
            }
            case 32430, 32431, 32432, 32433, 32434, 32435, 32436, 32437, 32438, 32439, 32440, 32441, 32442, 32443, 32444, 32445, 32446, 32447, 32448, 32449, 32450, 32451, 32452, 32453, 32454, 32455, 32456, 32457, 32458, 32459, 32460 -> {
                max = 512;
                if (damage % 2 == 0) {
                    current = max;
                }
            }
            default -> {
                return null;
            }
        }

        if (stack.hasTagCompound()) {
            var tag = stack.getTagCompound();
            if (tag.hasKey("GT.RemainingPaint")) {
                current = tag.getLong("GT.RemainingPaint");
            }
            if (tag.hasKey("GT.LighterFuel")) {
                current = tag.getLong("GT.LighterFuel");

            }
        }
        return new DurabilityLikeInfo(current, max);
    }
}
