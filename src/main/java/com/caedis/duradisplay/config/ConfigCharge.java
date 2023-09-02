package com.caedis.duradisplay.config;

import com.caedis.duradisplay.overlay.OverlayDuarbilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public final class ConfigCharge extends ConfigDurabilityLike {

    public static final ConfigCharge instance = new ConfigCharge();

    private ConfigCharge() {
        super(
            true,
            OverlayDuarbilityLike.Style.NumPad,
            DurabilityFormatter.Format.percent,
            8,
            false,
            true,
            0xFF55FFFF,
            ColorType.Single,
            new double[] { 30, 70 });
    }

    @Override
    public String category() {
        return "charge";
    }
}
