package com.caedis.duradisplay.utils;

import java.text.DecimalFormat;

import org.jetbrains.annotations.Nullable;

public class DurabilityFormatter {

    public enum Format {
        percent,
        remain,
        used,
        max,
        fraction,
    }

    @Nullable
    public static String format(double current, double max, Format format) {
        double percent = current / max * 100;
        switch (format) {
            case percent -> {
                return Double.isNaN(percent) ? null : String.format("%.0f%%", percent);
            }
            case remain -> {
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
