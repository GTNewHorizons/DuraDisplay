package com.caedis.duradisplay.utils;

import java.text.DecimalFormat;

import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.Nullable;

public class DurabilityFormatter {

    public enum Format {
        PERCENT,
        REMAINING,
        USED,
        MAX,
        FRACTION,
        CONTAINER,
    }

    @Nullable
    public static String format(double current, double max, Format format) {
        switch (format) {
            case PERCENT -> {
                double percent = current / max * 100;
                return Double.isNaN(percent) ? null : MathHelper.floor_double(percent) + "%";
            }
            case REMAINING -> {
                return shortenNumber(current);
            }
            case USED -> {
                return shortenNumber(max - current);
            }
            case MAX -> {
                return shortenNumber(max);
            }
            case FRACTION -> {
                double percent = current / max * 100;
                // current and max will always be full values
                return Double.isNaN(percent) ? null
                    : MathHelper.floor_double_long(current) + "/" + MathHelper.floor_double_long(max);
            }
            case CONTAINER -> {
                final long maxLong = MathHelper.floor_double_long(max);
                final long currentLong = MathHelper.floor_double_long(current);
                if (maxLong >= 100) {
                    if (currentLong >= 1000) {
                        return "*";
                    }
                    return Long.toString(currentLong);
                }
                return currentLong + "/" + maxLong;
            }
        }
        return null;
    }

    // Reused; construction is expensive
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.#");

    // Logic from Durability101
    public static String shortenNumber(double number) {
        if (number >= 1000000000) return decimalFormat.format(number / 1000000000) + "b";
        if (number >= 1000000) return decimalFormat.format(number / 1000000) + "m";
        if (number >= 1000) return decimalFormat.format(number / 1000) + "k";

        return decimalFormat.format(number);
    }
}
