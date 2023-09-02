package com.caedis.duradisplay.utils;

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
        GTChargebar = GT_Mod.gregtechproxy.mRenderItemChargeBar;
        GTDurabilitybar = GT_Mod.gregtechproxy.mRenderItemDurabilityBar;
        EIOChargebar = crazypants.enderio.config.Config.renderChargeBar;
        EIODurabilitybar = crazypants.enderio.config.Config.renderDurabilityBar;

        crazypants.enderio.config.Config.renderChargeBar = enable;
        crazypants.enderio.config.Config.renderDurabilityBar = enable;
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = enable;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = enable;
    }

    public static void pop() {
        if (!loaded) return;

        loaded = false;
        crazypants.enderio.config.Config.renderChargeBar = EIOChargebar;
        crazypants.enderio.config.Config.renderDurabilityBar = EIODurabilitybar;
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = GTDurabilitybar;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = GTChargebar;
    }
}
