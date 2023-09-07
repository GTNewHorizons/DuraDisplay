package com.caedis.duradisplay.utils;

import java.awt.*;

import com.caedis.duradisplay.config.ConfigDurabilityLike;

public enum ColorType {

    RYGDurability {

        private int get(double percent) {
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
                return config.threeColors[0];
            } else if (dur >= config.colorThreshold[config.colorThreshold.length - 1]) {
                return config.threeColors[1];
            } else {
                return config.threeColors[2];
            }
        }

    },
    Vanilla {

        private int get(double percent) {
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
        public int get(double percent, ConfigDurabilityLike config) {
            return config.color;
        }
    },
    Smooth {

        @Override
        public int get(double percent, ConfigDurabilityLike config) {
            int c0 = config.threeColors[0];
            int c1 = config.threeColors[1];
            int c2 = config.threeColors[2];
            int r = (int) ((c0 >> 16 & 0xFF) * (1.0 - percent) + (c1 >> 16 & 0xFF) * percent);
            int g = (int) ((c0 >> 8 & 0xFF) * (1.0 - percent) + (c1 >> 8 & 0xFF) * percent);
            int b = (int) ((c0 & 0xFF) * (1.0 - percent) + (c1 & 0xFF) * percent);
            int r2 = (int) ((c1 >> 16 & 0xFF) * (1.0 - percent) + (c2 >> 16 & 0xFF) * percent);
            int g2 = (int) ((c1 >> 8 & 0xFF) * (1.0 - percent) + (c2 >> 8 & 0xFF) * percent);
            int b2 = (int) ((c1 & 0xFF) * (1.0 - percent) + (c2 & 0xFF) * percent);
            int r3 = (int) (r * (1.0 - percent) + r2 * percent);
            int g3 = (int) (g * (1.0 - percent) + g2 * percent);
            int b3 = (int) (b * (1.0 - percent) + b2 * percent);
            return r3 << 16 | g3 << 8 | b3;
        }
    };

    public abstract int get(double percent, ConfigDurabilityLike config);

}
