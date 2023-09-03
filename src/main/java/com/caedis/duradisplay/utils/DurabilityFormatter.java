package com.caedis.duradisplay.utils;

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
                return String.format("%.0f", current);
            }
            case used -> {
                return String.format("%.0f", max - current);
            }
            case max -> {
                return String.format("%.0f", max);
            }
            case fraction -> {
                return Double.isNaN(percent) ? null : String.format("%.0f/%.0f", current, max);
            }
        }
        return null;
    }
}
