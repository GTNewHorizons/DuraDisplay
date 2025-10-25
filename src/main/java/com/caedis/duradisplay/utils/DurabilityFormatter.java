package com.caedis.duradisplay.utils;

import java.text.DecimalFormat;

import org.jetbrains.annotations.Nullable;

public class DurabilityFormatter {

    public enum Format {
        percent,
        remaining,
        used,
        max,
        fraction,
        container,
    }

    @Nullable
    public static String format(double current, double max, Format format) {
        double percent = current / max * 100;
        switch (format) {
            case percent -> {
                return Double.isNaN(percent) ? null : String.format("%.0f%%", percent);
            }
            case remaining -> {
                return shortenNumber(current);
            }
            case used -> {
                return shortenNumber(max - current);
            }
            case max -> {
                return shortenNumber(max);
            }
            case fraction -> {
                return Double.isNaN(percent) ? null : String.format("%.0f/%.0f", current, max);
            }
            case container -> {
                final long maxLong = Math.round(max);
                final long currentLong = Math.round(current);
                if (maxLong >= 100) {
                    if (currentLong >= 1000) {
                        return "*";
                    }
                    return String.format("%d", currentLong);
                }
                return String.format("%d/%d", currentLong, maxLong);
            }
        }
        return null;
    }

    // Logic from Durability101
    public static String shortenNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");

        if (number >= 1000000000) return decimalFormat.format(number / 1000000000) + "b";
        if (number >= 1000000) return decimalFormat.format(number / 1000000) + "m";
        if (number >= 1000) return decimalFormat.format(number / 1000) + "k";

        return decimalFormat.format(number);
    }
}
