package com.caedis.duradisplay.config;

import net.minecraftforge.common.config.Configuration;

public abstract class OverlayConfig {

    protected final String category;

    public OverlayConfig(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public abstract void loadConfig(Configuration config);
}
