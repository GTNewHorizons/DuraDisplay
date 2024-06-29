package com.caedis.duradisplay;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.utils.AppEngItemRenderHook;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(
    modid = DuraDisplay.MODID,
    version = Tags.VERSION,
    name = DuraDisplay.NAME,
    acceptedMinecraftVersions = "[1.7.10]",
    guiFactory = "com.caedis.duradisplay.config.GuiFactory",
    acceptableRemoteVersions = "*",
    dependencies = "after:gregtech@[5.09.43.63,);" + " after:EnderIO@[2.4.18,);")
public class DuraDisplay {
    public final static String MODID = "duradisplay";
    public final static String NAME = "DuraDisplay";

    public static final Logger LOG = LogManager.getLogger(MODID);

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            DuraDisplayConfig.loadConfig();
            FMLCommonHandler.instance()
                .bus()
                .register(this);
        }
        try {
            AppEngItemRenderHook.init();
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("AE2 not found, skipping AppEngItemRenderHook");
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(DuraDisplay.MODID)) {
            DuraDisplayConfig.config.save();
            DuraDisplayConfig.reloadConfigObject();
        }
    }

}
