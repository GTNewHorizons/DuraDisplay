package com.caedis.duradisplay.utils;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import com.caedis.duradisplay.config.DuraDisplayConfig;
import com.caedis.duradisplay.overlay.OverlayDuarbilityLike;

public class ConfigLoad {

    public static <T extends Enum<T>> T loadEnum(Configuration config, String category, String name, T defaultValue,
        String comment) {

        Class<T> enumType = defaultValue.getDeclaringClass();
        return T.valueOf(
            enumType,
            config.getString(
                name,
                category,
                OverlayDuarbilityLike.Style.NumPad.toString(),
                comment,
                Arrays.stream(
                    defaultValue.getClass()
                        .getEnumConstants())
                    .map(Enum::toString)
                    .toArray(String[]::new)));

    }

    public static <T extends Enum<T>> T loadEnum(String category, String name, T defaultValue, String comment) {
        return loadEnum(DuraDisplayConfig.config, category, name, defaultValue, comment);
    }
}
