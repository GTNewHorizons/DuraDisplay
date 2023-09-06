package com.caedis.duradisplay.overlay;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;

public class OverlayGTGadgets extends OverlayDurabilityLike {

    public OverlayGTGadgets() {
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
                    return;
                }

                @Override
                public @NotNull String category() {
                    return "gregtech_gadgets";
                }
            });
        addHandler("gregtech.api.items.GT_MetaBase_Item", OverlayGTGadgets::handleGregtech);
    }

    @Override
    @NotNull
    ConfigDurabilityLike config() {
        return config;
    }

    @Nullable
    private static DurabilityLikeInfo handleGregtech(@NotNull ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        var tag = stack.getTagCompound();
        if (tag.hasKey("GT.RemainingPaint")) {
            long paint = tag.getLong("GT.RemainingPaint");
            return new DurabilityLikeInfo(paint, paint);
        }
        if (tag.hasKey("GT.LighterFuel")) {
            long paint = tag.getLong("GT.LighterFuel");
            return new DurabilityLikeInfo(paint, paint);
        }
        return null;
    }
}
