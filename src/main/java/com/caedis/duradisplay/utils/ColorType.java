package com.caedis.duradisplay.utils;

import java.awt.*;

import com.caedis.duradisplay.config.ConfigDurabilityLike;

public enum ColorType {

    RYGDurability {

        @Override
        public int get(double percent) {
            return Color.HSBtoRGB(Math.max(0.0F, (float) percent) / 3.0F, 1.0F, 1.0F);
        }

        @Override
        public int get(double percent, ConfigDurabilityLike config) {
            return get(percent);
        }
    },
    Threshold {

        public int get(double percent, ConfigDurabilityLike config) {
            double dur = percent * 100;
            if (dur <= config.colorThreshold[0]) {
                return 0xFF0000;
            } else if (dur >= config.colorThreshold[config.colorThreshold.length - 1]) {
                return 0x55FF00;
            } else {
                return 0XFFD500;
            }
        }

        @Override
        public int get(double percent) {
            double dur = percent * 100;
            if (dur <= 30) {
                return 0xFF0000;
            } else if (dur >= 70) {
                return 0x55FF00;
            } else {
                return 0XFFD500;
            }
        }
    },
    Vanilla {

        @Override
        public int get(double percent) {
            final int k = (int) Math.round(percent * 255.0D);
            return 255 - k << 16 | k << 8;
        }

        @Override
        public int get(double percent, ConfigDurabilityLike config) {
            return get(percent);
        }

    },
    Single {

        @Override
        public int get(double percent) {
            return 0xFF0000;
        }

        @Override
        public int get(double percent, ConfigDurabilityLike config) {
            return config.color;
        }
    };

    public abstract int get(double percent);

    public abstract int get(double percent, ConfigDurabilityLike config);

}
