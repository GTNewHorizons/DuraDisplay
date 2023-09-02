package com.caedis.duradisplay.config;

import java.util.Arrays;

import com.caedis.duradisplay.overlay.OverlayDuarbilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.ConfigLoad;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public abstract class ConfigDurabilityLike extends Config {

    public boolean enabled;
    public OverlayDuarbilityLike.Style style;
    public DurabilityFormatter.Format textFormat;
    public int numPadPosition;
    public boolean showWhenFull;
    public boolean showWhenEmpty;
    public int color;
    public ColorType colorType;
    public double[] colorThreshold;

    protected ConfigDurabilityLike(boolean enabled, OverlayDuarbilityLike.Style style,
        DurabilityFormatter.Format textFormat, int numPadPosition, boolean showWhenFull, boolean showWhenEmpty,
        int color, ColorType colorType, double[] colorThreshold) {
        this.enabled = enabled;
        this.style = style;
        this.textFormat = textFormat;
        this.numPadPosition = numPadPosition;
        this.showWhenFull = showWhenFull;
        this.showWhenEmpty = showWhenEmpty;
        this.color = color;
        this.colorType = colorType;
        this.colorThreshold = colorThreshold;
    }

    @Override
    public void loadConfig() {

        enabled = config.getBoolean("Enable", category(), enabled, String.format("Enable %s module", category()));

        style = ConfigLoad.loadEnum(
            category(),
            "Style",
            OverlayDuarbilityLike.Style.NumPad,
            "Style of the Overlay, can be NumPad, Bar, or VerticalBar");

        numPadPosition = config.getInt(
            "NumPadPosition",
            category() + ".NumPad",
            numPadPosition,
            1,
            9,
            String.format("Location in item where the %s percentage will be (numpad style)", category()));

        textFormat = ConfigLoad
            .loadEnum(category() + ".NumPad", "TextFormat", DurabilityFormatter.Format.percent, "Format of the text");

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
