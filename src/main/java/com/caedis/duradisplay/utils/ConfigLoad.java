package com.caedis.duradisplay.utils;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import com.caedis.duradisplay.config.DuraDisplayConfig;

public class ConfigLoad {

    public static <T extends Enum<T>> T loadEnum(Configuration config, String category, String name, T defaultValue,
        String comment) {

        Class<T> enumType = defaultValue.getDeclaringClass();
        return T.valueOf(
            enumType,
            config.getString(
                name,
                category,
                defaultValue.toString(),
                comment,
                Arrays.stream(enumType.getEnumConstants())
                    .map(Enum::toString)
                    .toArray(String[]::new)));

    }

    public static <T extends Enum<T>> T loadEnum(String category, String name, T defaultValue, String comment) {
        return loadEnum(DuraDisplayConfig.config, category, name, defaultValue, comment);
    }
}
