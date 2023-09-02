package com.caedis.duradisplay.config;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import com.caedis.duradisplay.DuraDisplay;
import com.caedis.duradisplay.utils.ModSelfDrawnBar;

public class DuraDisplayConfig {

    public static ArrayList<Config> configs = null;

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
            if (!configDir.mkdirs()) {
                DuraDisplay.LOG.warn("Could not create config directory: " + configDir.getAbsolutePath());
            }
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
        if (Enable) {
            ModSelfDrawnBar.pushDisable();
        } else {
            ModSelfDrawnBar.pop();
        }

        for (var c : ConfigInfo.getConfigs()) {
            c.loadConfig(config);
        }

        if (config.hasChanged()) {
            config.save();
        }

    }
}
