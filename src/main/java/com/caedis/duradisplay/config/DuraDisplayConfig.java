package com.caedis.duradisplay.config;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import gregtech.GT_Mod;

public class DuraDisplayConfig {

    public static ArrayList<Config> configs = null;

    public static void addConfig(Config config) {
        if (configs != null) configs.add(config);
        else {
            configs = new ArrayList<>();
            configs.add(config);
        }
    }

    private static boolean configLoaded = false;

    public static boolean Enable = true;

    public static Configuration config = null;

    public static void loadConfig() {
        if (configLoaded) {
            return;
        }
        configLoaded = true;
        final File configDir = new File(Launch.minecraftHome, "config");
        if (!configDir.isDirectory()) {
            configDir.mkdirs();
        }
        final File configFile = new File(configDir, "duradisplay.cfg");
        config = new Configuration(configFile);

        reloadConfigObject();

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void reloadConfigObject() {

        Enable = config.getBoolean("Enable", Configuration.CATEGORY_GENERAL, Enable, "Enable/disable the entire mod");

        for (var c : ConfigInfo.getConfigs()) {
            c.loadConfig(config);
        }

        if (config.hasChanged()) {
            config.save();
        }

        // Gregtech Bars
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = false;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = false;

        // EnderIO Bars
        crazypants.enderio.config.Config.renderChargeBar = false;
        crazypants.enderio.config.Config.renderDurabilityBar = false;
    }
}
