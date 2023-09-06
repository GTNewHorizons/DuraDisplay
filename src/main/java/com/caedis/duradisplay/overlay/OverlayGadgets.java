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
        addHandler("ic2.core.item.tool.ItemToolPainter", OverlayDurability::handleDefault);
        addHandler("WayofTime.alchemicalWizardry.common.items.ScribeTool", OverlayDurability::handleDefault);
        addHandler("net.minecraft.item.Item", OverlayGadgets::handleByAllowList);
    }

    public static final Set<String> AllowListUnLocalized = Sets.newHashSet(
        "item.flintAndSteel",
        "ic2.itemWeedEx",
        "item.for.waxCast",
        "item.for.solderingIron",
        "ic2.itemTreetap",
        "item.appliedenergistics2.ToolCertusQuartzCuttingKnife",
        "ic2.itemToolForgeHammer");

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
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
        if (!stack.hasTagCompound()) return null;
        var tag = stack.getTagCompound();
        if (tag.hasKey("GT.RemainingPaint")) {
            long paint = tag.getLong("GT.RemainingPaint");
            return new DurabilityLikeInfo(paint, 512);
        }
        if (tag.hasKey("GT.LighterFuel")) {
            long max;
            long paint = tag.getLong("GT.LighterFuel");
            switch (stack.getUnlocalizedName()) {
                case "gt.metaitem.01.32478" -> max = 1000;
                case "gt.metaitem.01.32475" -> max = 100;
                case "gt.metaitem.01.32472" -> max = 16;
                default -> max = 0;
            }

            return new DurabilityLikeInfo(paint, max);
        }
        return null;
    }
}
