package com.caedis.duradisplay.config;

import com.caedis.duradisplay.overlay.OverlayDuarbilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public final class ConfigDurability extends ConfigDurabilityLike {

    private ConfigDurability() {
        super(
            true,
            OverlayDuarbilityLike.Style.NumPad,
            DurabilityFormatter.Format.percent,
            2,
            false,
            true,
            0x00FF00,
            ColorType.Vanilla,
            new double[] { 30, 70 });
    }

    public static final ConfigDurability instance = new ConfigDurability();

    @Override
    public String category() {
        return "durability";
    }
}