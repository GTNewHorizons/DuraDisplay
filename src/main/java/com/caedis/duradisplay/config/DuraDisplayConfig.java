package com.caedis.duradisplay.config;

import java.io.File;
import java.util.ArrayList;

import com.caedis.duradisplay.render.ChargeOverlay;
import com.caedis.duradisplay.render.DurabilityOverlay;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import crazypants.enderio.config.Config;
import gregtech.GT_Mod;

public class DuraDisplayConfig {

    public static ArrayList<OverlayConfig> configs = null;

    public static void addConfig(OverlayConfig config) {
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

        DurabilityOverlay.config.loadConfig(config);
        ChargeOverlay.config.loadConfig(config);

        if (config.hasChanged()) {
            config.save();
        }

        // Gregtech Bars
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = false;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = false;

        // EnderIO Bars
        Config.renderChargeBar = false;
        Config.renderDurabilityBar = false;
    }
}
