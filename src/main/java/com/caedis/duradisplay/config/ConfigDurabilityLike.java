package com.caedis.duradisplay.config;

import java.util.Arrays;

import com.caedis.duradisplay.overlay.OverlayDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.ConfigLoad;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public abstract class ConfigDurabilityLike extends Config {

    public boolean enabled;
    public OverlayDurabilityLike.Style style;
    public DurabilityFormatter.Format textFormat;
    public int numPadPosition;
    public boolean showWhenFull;
    public boolean showWhenEmpty;
    public int color;
    public ColorType colorType;
    public double[] colorThreshold;
    public int[] colorThresholdColors = { 0xFF0000, 0x55FF00, 0XFFD500 };
    public boolean smoothBar;
    public int barLocation;

    protected ConfigDurabilityLike(boolean enabled, OverlayDurabilityLike.Style style,
        DurabilityFormatter.Format textFormat, int numPadPosition, boolean showWhenFull, boolean showWhenEmpty,
        int color, ColorType colorType, double[] colorThreshold, boolean smoothBar, int barLocation) {
        this.enabled = enabled;
        this.style = style;
        this.textFormat = textFormat;
        this.numPadPosition = numPadPosition;
        this.showWhenFull = showWhenFull;
        this.showWhenEmpty = showWhenEmpty;
        this.color = color;
        this.colorType = colorType;
        this.colorThreshold = colorThreshold;
        this.smoothBar = smoothBar;
        this.barLocation = barLocation;
    }

    @Override
    public void loadConfig() {

        enabled = config.getBoolean("Enable", category(), enabled, String.format("Enable %s module", category()));

        style = ConfigLoad
            .loadEnum(category(), "Style", style, "Style of the Overlay, can be NumPad, Bar, or VerticalBar");

        numPadPosition = config.getInt(
            "NumPadPosition",
            category() + ".NumPad",
            numPadPosition,
            1,
            9,
            String.format("Location in item where the %s percentage will be (numpad style)", category()));

        textFormat = ConfigLoad.loadEnum(category() + ".NumPad", "TextFormat", textFormat, "Format of the text");

        showWhenFull = config.getBoolean(
            "ShowWhenFull",
            category(),
            showWhenFull,
            String.format("Show %s percentage when item is undamaged/full", category()));

        showWhenEmpty = config.getBoolean(
            "ShowWhenEmpty",
            category(),
            showWhenEmpty,
            String.format("Show %s percentage when empty", category()));

        colorType = ConfigLoad.loadEnum(
            category() + ".Color",
            "ColorType",
            colorType,
            "ColorType of the Overlay, can be RYGDurability, Threshold, Vanilla, or Single");

        colorThreshold = Arrays.stream(
            config.get(
                category() + ".Color",
                "ColorThresholds",
                colorThreshold,
                "List of numbers in ascending order from 0-100 that set the thresholds for durability color mapping. "
                    + "Colors are the list of colors in ColorThresholdColors\n"
                    + "By default from Red -> Yellow -> Green by default",
                0.0,
                100.0,
                true,
                2)
                .getDoubleList())
            .sorted()
            .toArray();

        color = config.getInt(
            "Color",
            category() + ".Color",
            color,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Color of the Overlay");

        colorThresholdColors = Arrays
            .stream(
                config
                    .get(
                        category() + ".Color",
                        "ColorThresholdColors",
                        colorThresholdColors,
                        "Colors used in Threshold color mode",
                        Integer.MIN_VALUE,
                        Integer.MAX_VALUE,
                        true,
                        3)
                    .getIntList())
            .sorted()
            .toArray();

        smoothBar = config.getBoolean("SmoothBar", category() + ".BarStyle", smoothBar, "Smooth the bar length");

        barLocation = config
            .getInt("BarLocation", category() + ".BarStyle", barLocation, 0, 9, "Raise the bar location in screen by");

        postLoadConfig();
    }

    public abstract void postLoadConfig();
}
