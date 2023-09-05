package com.caedis.duradisplay.config;

import com.caedis.duradisplay.overlay.OverlayDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public final class ConfigCharge extends ConfigDurabilityLike {

    public ConfigCharge() {
        super(
            true,
            OverlayDurabilityLike.Style.NumPad,
            DurabilityFormatter.Format.percent,
            8,
            false,
            true,
            0xFF55FFFF,
            ColorType.Single,
            new double[] { 30, 70 },
            true);
    }

    @Override
    public String category() {
        return "charge";
    }
}
