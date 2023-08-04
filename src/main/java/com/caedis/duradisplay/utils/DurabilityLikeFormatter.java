package com.caedis.duradisplay.utils;

import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;


public class DurabilityLikeFormatter {
    private static final NumberFormat nf = NumberFormat.getNumberInstance();

    public enum Format {
        percent,
        remain,
        used,
        max,
        fraction,
    }


    @Nullable
    public static String format(double current,double max,Format format) {
        double percent = current / max * 100;
        switch (format) {
            case percent -> {
                return Double.isNaN(percent) ? null : nf.format(percent) + "%";
            }
            case remain -> {
                return nf.format(current);
            }
            case used -> {
                return nf.format(max - current);
            }
            case max -> {
                return nf.format(max);
            }
            case fraction -> {
                return nf.format(current) + "/" + nf.format(max);
            }
        }
        return null;
    }
}
