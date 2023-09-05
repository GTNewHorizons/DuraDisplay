package com.caedis.duradisplay.utils;

import com.caedis.duradisplay.DuraDisplay;

import gregtech.GT_Mod;

public final class ModSelfDrawnBar {

    private static boolean loaded = false;
    private static boolean GTDurabilitybar;
    private static boolean GTChargebar;
    private static boolean EIODurabilitybar;
    private static boolean EIOChargebar;

    public static void pushDisable() {
        push(false);
    }

    public static void pushEnable() {
        push(true);
    }

    private static void push(boolean enable) {
        if (loaded) return;

        loaded = true;
        try {
            pushGT(enable);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            DuraDisplay.LOG.info("GregTech not found, skipping changing GT durabilitybar config");
        }
        try {
            pushEIO(enable);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            DuraDisplay.LOG.info("EnderIO not found, skipping changing EIO durabilitybar config");
        }
    }

    private static void pushGT(boolean enable) throws ClassNotFoundException {
        GTChargebar = GT_Mod.gregtechproxy.mRenderItemChargeBar;
        GTDurabilitybar = GT_Mod.gregtechproxy.mRenderItemDurabilityBar;

        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = enable;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = enable;
    }

    private static void pushEIO(boolean enable) throws ClassNotFoundException {
        EIOChargebar = crazypants.enderio.config.Config.renderChargeBar;
        EIODurabilitybar = crazypants.enderio.config.Config.renderDurabilityBar;

        crazypants.enderio.config.Config.renderChargeBar = enable;
        crazypants.enderio.config.Config.renderDurabilityBar = enable;
    }

    public static void pop() {
        if (!loaded) return;

        loaded = false;
        try {
            popEIO();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            DuraDisplay.LOG.info("EnderIO not found, skipping changing EIO durabilitybar config");
        }
        try {
            popGT();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            DuraDisplay.LOG.info("GregTech not found, skipping changing GT durabilitybar config");
        }
    }

    private static void popEIO() throws ClassNotFoundException {
        crazypants.enderio.config.Config.renderChargeBar = EIOChargebar;
        crazypants.enderio.config.Config.renderDurabilityBar = EIODurabilitybar;
    }

    private static void popGT() throws ClassNotFoundException {
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = GTDurabilitybar;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = GTChargebar;
    }
}
