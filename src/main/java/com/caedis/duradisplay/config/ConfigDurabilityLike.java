package com.caedis.duradisplay.config;

import java.util.Arrays;

import com.caedis.duradisplay.overlay.OverlayDuarbilityLike;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public abstract class ConfigDurabilityLike extends Config {

    public boolean enabled;
    public OverlayDuarbilityLike.Style style;
    public DurabilityFormatter.Format textFormat;
    public int numPadPosition;
    public boolean showWhenFull;
    public boolean showWhenEmpty;
    public int color;
    public boolean useColorThreshold;
    public double[] colorThreshold;

    protected ConfigDurabilityLike(boolean enabled, OverlayDuarbilityLike.Style style,
        DurabilityFormatter.Format textFormat, int numPadPosition, boolean showWhenFull, boolean showWhenEmpty,
        int color, boolean useColorThreshold, double[] colorThreshold) {
        this.enabled = enabled;
        this.style = style;
        this.textFormat = textFormat;
        this.numPadPosition = numPadPosition;
        this.showWhenFull = showWhenFull;
        this.showWhenEmpty = showWhenEmpty;
        this.color = color;
        this.useColorThreshold = useColorThreshold;
        this.colorThreshold = colorThreshold;
    }

    @Override
    public void loadConfig() {

        enabled = config.getBoolean("Enable", category(), enabled, String.format("Enable %s module", category()));

        style = OverlayDuarbilityLike.Style.valueOf(
            config.getString(
                "Style",
                category() + ".NumPad",
                OverlayDuarbilityLike.Style.NumPad.toString(),
                "Style of the Overlay, can be NumPad, Bar, or VerticalBar",
                Arrays.stream(OverlayDuarbilityLike.Style.values())
                    .map(Enum::toString)
                    .toArray(String[]::new)));

        numPadPosition = config.getInt(
            "NumPadPosition",
            category() + ".NumPad",
            numPadPosition,
            1,
            9,
            String.format("Location in item where the %s percentage will be (numpad style)", category()));

        textFormat = DurabilityFormatter.Format.valueOf(
            config.getString(
                "TextFormat",
                category() + ".NumPad",
                DurabilityFormatter.Format.percent.toString(),
                "Format of the text, can be percent, fraction, or both",
                Arrays.stream(DurabilityFormatter.Format.values())
                    .map(Enum::toString)
                    .toArray(String[]::new)));

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

        useColorThreshold = config.getBoolean(
            "UseColorThreshold",
            category() + ".Color",
            useColorThreshold,
            String.format("Use color threshold for %s", category()));

        colorThreshold = Arrays.stream(
            config.get(
                category() + ".Color",
                "ColorThresholds",
                colorThreshold,
                "List of numbers in ascending order from 0-100 that set the thresholds for durability color mapping. "
                    + "Colors are from Red -> Yellow -> Green with Red being less than or equal to the first value "
                    + "and Green being greater than or equal to the last value",
                0.0,
                100.0,
                true,
                2)
                .getDoubleList())
            .sorted()
            .toArray();

    }
}
