package com.caedis.duradisplay.utils;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import com.caedis.duradisplay.DuraDisplay;
import com.caedis.duradisplay.config.DuraDisplayConfig;

public class ConfigLoad {

    public static <T extends Enum<T>> T loadEnum(Configuration config, String category, String name, T defaultValue,
        String comment) {

        Class<T> enumType = defaultValue.getDeclaringClass();
        String raw = config.getString(
            name,
            category,
            defaultValue.toString(),
            comment,
            Arrays.stream(enumType.getEnumConstants())
                .map(Enum::toString)
                .toArray(String[]::new));

        for (T constant : enumType.getEnumConstants()) {
            if (constant.name()
                .equalsIgnoreCase(raw)) {
                return constant;
            }
        }

        DuraDisplay.LOG.warn(
            String.format(
                "Invalid value '%s' for %s.%s, falling back to default '%s'",
                raw,
                category,
                name,
                defaultValue.name()));
        return defaultValue;
    }

    public static <T extends Enum<T>> T loadEnum(String category, String name, T defaultValue, String comment) {
        return loadEnum(DuraDisplayConfig.config, category, name, defaultValue, comment);
    }
}
