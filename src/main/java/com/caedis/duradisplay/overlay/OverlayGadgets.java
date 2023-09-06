package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;

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
                        including those maxDurability<100
                        and GregTech lighters and Paint Sprayer
                                                                        """);
                }

                @Override
                public @NotNull String category() {
                    return "gadgets";
                }
            });
        addHandler("gregtech.api.items.GT_MetaBase_Item", OverlayGadgets::handleGregtech);
    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
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
