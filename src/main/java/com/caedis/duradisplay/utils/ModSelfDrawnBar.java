package com.caedis.duradisplay.utils;

import java.util.Optional;

import com.caedis.duradisplay.DuraDisplay;

import gregtech.GT_Mod;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class ModSelfDrawnBar {

    private static Optional<Boolean> GTDurabilitybar = Optional.empty();
    private static Optional<Boolean> GTChargebar = Optional.empty();
    private static Optional<Boolean> EIODurabilitybar = Optional.empty();
    private static Optional<Boolean> EIOChargebar = Optional.empty();

    public static void changeEIOChargebar(boolean enable) {
        try {
            if (!EIOChargebar.isPresent()) EIOChargebar = Optional.of(crazypants.enderio.config.Config.renderChargeBar);
            crazypants.enderio.config.Config.renderChargeBar = enable;
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("EIO not found, skipping Chargebar config change");
        }
    }

    public static void changeEIODurabilitybar(boolean enable) {
        try {
            if (!EIODurabilitybar.isPresent())
                EIODurabilitybar = Optional.of(crazypants.enderio.config.Config.renderDurabilityBar);
            crazypants.enderio.config.Config.renderDurabilityBar = enable;
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("EIO not found, skipping Durabilitybar config change");
        }
    }

    public static void changeGTDurabilitybar(boolean enable) {
        try {
            if (!GTDurabilitybar.isPresent())
                GTDurabilitybar = Optional.of(GT_Mod.gregtechproxy.mRenderItemDurabilityBar);
            GT_Mod.gregtechproxy.mRenderItemDurabilityBar = enable;
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("GT not found, skipping Durabilitybar config change");
        }
    }

    public static void changeGTChargebar(boolean enable) {
        try {
            if (!GTChargebar.isPresent()) GTChargebar = Optional.of(GT_Mod.gregtechproxy.mRenderItemChargeBar);
            GT_Mod.gregtechproxy.mRenderItemChargeBar = enable;
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("GT not found, skipping Charge config change");
        }
    }

    public static void restoreEIOChargebar() {
        try {
            EIOChargebar.ifPresent(aBoolean -> crazypants.enderio.config.Config.renderChargeBar = aBoolean);
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("EIO not found, skipping Chargebar config restore");
        }
    }

    public static void restoreEIODurabilitybar() {
        try {
            EIODurabilitybar.ifPresent(aBoolean -> crazypants.enderio.config.Config.renderDurabilityBar = aBoolean);
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("EIO not found, skipping Durabilitybar config restore");
        }
    }

    public static void restoreGTDurabilitybar() {
        try {
            GTDurabilitybar.ifPresent(aBoolean -> GT_Mod.gregtechproxy.mRenderItemDurabilityBar = aBoolean);
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("GT not found, skipping Durabilitybar config restore");
        }
    }

    public static void restoreGTChargebar() {
        try {
            GTChargebar.ifPresent(aBoolean -> GT_Mod.gregtechproxy.mRenderItemChargeBar = aBoolean);
        } catch (NoClassDefFoundError e) {
            DuraDisplay.LOG.info("GT not found, skipping Charge config restore");
        }
    }

    public static void changeChargebar(boolean enable) {
        changeEIOChargebar(enable);
        changeGTChargebar(enable);
    }

    public static void changeDurabilitybar(boolean enable) {
        changeEIODurabilitybar(enable);
        changeGTDurabilitybar(enable);
    }

    public static void restoreChargebar() {
        restoreEIOChargebar();
        restoreGTChargebar();
    }

    public static void restoreDurabilitybar() {
        restoreEIODurabilitybar();
        restoreGTDurabilitybar();
    }

    public static void change(boolean enable) {
        changeChargebar(enable);
        changeDurabilitybar(enable);
    }

    public static void restore() {
        restoreChargebar();
        restoreDurabilitybar();
    }
}
