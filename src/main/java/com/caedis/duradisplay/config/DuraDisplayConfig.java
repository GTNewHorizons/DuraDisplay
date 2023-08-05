package com.caedis.duradisplay.config;

import java.io.File;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import crazypants.enderio.config.Config;
import gregtech.GT_Mod;

public class DuraDisplayConfig {

    public static DurabilityOverlayConfig DurabilityConfig = new DurabilityOverlayConfig("durability");
    public static DurabilityOverlayConfig ChargeConfig = new DurabilityOverlayConfig("charge");
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

        DurabilityConfig.loadConfig(config);
        ChargeConfig.loadConfig(config);
        ChargeConfig.Position=8;

        if (config.hasChanged()) {
            config.save();
        }

        // Gregtech Bars
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = Enable
            && !(DurabilityConfig.Enabled && !DurabilityConfig.RenderBar);
        GT_Mod.gregtechproxy.mRenderItemChargeBar = Enable && !(ChargeConfig.Enabled && !ChargeConfig.RenderBar);

        // EnderIO Bars
        Config.renderChargeBar = Enable && !(ChargeConfig.Enabled && !ChargeConfig.RenderBar);
        Config.renderDurabilityBar = Enable && !(DurabilityConfig.Enabled && !DurabilityConfig.RenderBar);
    }
}
