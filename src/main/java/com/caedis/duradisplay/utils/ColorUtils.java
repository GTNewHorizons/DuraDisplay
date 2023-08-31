package com.caedis.duradisplay.utils;

import java.awt.*;

public class ColorUtils {

    public static int getThresholdColor(double percent, double[] colorThreshold) {
        double dur = percent * 100;
        if (dur <= colorThreshold[0]) {
            return 0xFF0000;
        } else if (dur >= colorThreshold[colorThreshold.length - 1]) {
            return 0x55FF00;
        } else {
            return 0XFFD500;
        }
    }

    public static int getDurabilityColor(double percent) {
        return Color.HSBtoRGB(Math.max(0.0F, (float) percent) / 3.0F, 1.0F, 1.0F);
    }
}
