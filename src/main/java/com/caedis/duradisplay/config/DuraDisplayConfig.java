package com.caedis.duradisplay.config;

import java.io.File;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import com.caedis.duradisplay.DuraDisplay;
import com.caedis.duradisplay.overlay.OverlayInfo;
import com.caedis.duradisplay.utils.ModSelfDrawnBar;

public class DuraDisplayConfig {

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
            ModSelfDrawnBar.change(false);
        } else {
            ModSelfDrawnBar.restore();
        }

        for (var c : OverlayInfo.getConfigs()) {
            if (c != null) c.loadConfig();
        }

        if (config.hasChanged()) {
            config.save();
        }

    }
}
