package com.caedis.duradisplay.config;

import net.minecraftforge.common.config.Configuration;

@com.caedis.duradisplay.annotation.Config
public abstract class Config {

    public abstract String category();

    public abstract void loadConfig(Configuration config);
}
