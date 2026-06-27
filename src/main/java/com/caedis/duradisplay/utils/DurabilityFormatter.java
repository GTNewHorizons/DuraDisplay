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
                return Double.isNaN(percent) ? null : Math.round(percent) + "%";
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
                return Double.isNaN(percent) ? null : Math.round(current) + "/" + Math.round(max);
            }
            case container -> {
                final long maxLong = Math.round(max);
                final long currentLong = Math.round(current);
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
