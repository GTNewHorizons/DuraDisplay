package com.caedis.duradisplay;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caedis.duradisplay.config.DuraDisplayConfig;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]",
    guiFactory = "com.caedis.duradisplay.config.GuiFactory",
    acceptableRemoteVersions = "*",
    dependencies = "required-after:gregtech@[5.09.43.63,);" + " required-after:EnderIO@[2.4.18,);")
public class DuraDisplay {

    public static final Logger LOG = LogManager.getLogger(Tags.MODID);

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
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Tags.MODID)) {
            DuraDisplayConfig.config.save();
            DuraDisplayConfig.reloadConfigObject();
        }
    }

}
