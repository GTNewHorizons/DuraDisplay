package com.caedis.duradisplay.config;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class DurabilityOverlayConfig extends OverlayConfig {

    public boolean Enabled = true;
    public int Position = 2;
    public boolean ShowWhenFull = false;
    public boolean ShowWhenEmpty = true;
    public boolean RenderBar = false;

    public int color = 0xFFFFFF;

    public boolean UseColorThreshold;
    public double[] ColorThreshold = new double[] { 15, 50 };

    public DurabilityOverlayConfig(String category) {
        super(category);
    }

    public DurabilityOverlayConfig(String category, int position, boolean enabled, boolean showWhenFull,
        boolean renderBar, boolean useColorThreshold, double[] colorThreshold) {
        super(category);
        this.Enabled = enabled;
        this.Position = position;
        this.ShowWhenFull = showWhenFull;
        this.RenderBar = renderBar;
        this.UseColorThreshold = useColorThreshold;
        this.ColorThreshold = colorThreshold;
    }

    public DurabilityOverlayConfig(String category, int position) {
        super(category);
        this.Position = position;
    }

    @Override
    public void loadConfig(Configuration config) {

        Enabled = config.getBoolean("Enable", category, Enabled, String.format("Enable %s module", category));

        RenderBar = config.getBoolean("RenderBar", category, RenderBar, String.format("Render %s bar", category));

        Position = config.getInt(
            "Position",
            category,
            Position,
            1,
            9,
            String.format("Location in item where the %s percentage will be (numpad style)", category));

        ShowWhenFull = config.getBoolean(
            "ShowWhenFull",
            category,
            ShowWhenFull,
            String.format("Show %s percentage when item is undamaged/full", category));

        ShowWhenFull = config.getBoolean(
            "ShowWhenEmpty",
            category,
            ShowWhenEmpty,
            String.format("Show %s percentage when empty", category));

        Property dura_colorThresh = config.get(
            category,
            "ColorThresholds",
            ColorThreshold,
            "List of numbers in ascending order from 0-100 that set the thresholds for durability color mapping. "
                + "Colors are from Red -> Yellow -> Green with Red being less than or equal to the first value "
                + "and Green being greater than or equal to the last value");
        ColorThreshold = dura_colorThresh.getDoubleList();

        // clean up whatever the user inputs
        ColorThreshold = Arrays.stream(ColorThreshold)
            .filter(num -> num >= 0.0 && num <= 100.0)
            .sorted()
            .toArray();
        dura_colorThresh.set(ColorThreshold);

    }
}
