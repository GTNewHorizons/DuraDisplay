package com.caedis.duradisplay.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import org.jetbrains.annotations.NotNull;

public abstract class Config {

    protected Configuration config;
    protected ConfigCategory configCategory;

    protected Config(Configuration config) {
        this.config = config;
        this.configCategory = config.getCategory(category());
    }

    protected Config() {
        this(DuraDisplayConfig.config);
    }

    @NotNull
    public abstract String category();

    public abstract void loadConfig();
}
