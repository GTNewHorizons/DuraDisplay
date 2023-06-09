package com.caedis.duradisplay.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.caedis.duradisplay.Tags;
import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.GuiConfig;

@SuppressWarnings("unused")
public class GuiConfigDuraDisplay extends GuiConfig {

    public GuiConfigDuraDisplay(GuiScreen parent) {
        super(
            parent,
            Lists.newArrayList(
                new ConfigElement<>(DuraDisplayConfig.config.getCategory(Configuration.CATEGORY_GENERAL)),
                new ConfigElement<>(DuraDisplayConfig.config.getCategory(DuraDisplayConfig.CATEGORY_DURABILITY)),
                new ConfigElement<>(DuraDisplayConfig.config.getCategory(DuraDisplayConfig.CATEGORY_CHARGE))),
            Tags.MODID,
            "general",
            false,
            false,
            getAbridgedConfigPath(DuraDisplayConfig.config.toString()));
    }

}
