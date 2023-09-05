package com.caedis.duradisplay.config;

import com.caedis.duradisplay.overlay.OverlayDurabilityLike;
import com.caedis.duradisplay.utils.ColorType;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public final class ConfigDurability extends ConfigDurabilityLike {

    public ConfigDurability() {
        super(
            true,
            OverlayDurabilityLike.Style.NumPad,
            DurabilityFormatter.Format.percent,
            2,
            false,
            true,
            0x00FF00,
            ColorType.Vanilla,
            new double[] { 30, 70 },
            true);
    }

    @Override
    public String category() {
        return "durability";
    }
}
