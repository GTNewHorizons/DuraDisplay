package com.caedis.duradisplay.overlay;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.render.BarRenderer;
import com.caedis.duradisplay.render.NumPadRenderer;
import com.caedis.duradisplay.render.OverlayRenderer;
import com.caedis.duradisplay.utils.DurabilityFormatter;

public abstract class OverlayDuarbilityLike extends Overlay<ConfigDurabilityLike> {

    public static final class DurabilityLikeInfo {

        private final double current;
        private final double max;

        public DurabilityLikeInfo(double current, double max) {
            this.current = current;
            this.max = max;
        }

        public boolean isFull() {
            return current != 0 && current == max;
        }

        public boolean isEmpty() {
            return current == 0 && max != 0;
        }

        public boolean isNaN() {
            return current == 0 && max == 0;
        }

        public double percent() {
            return current / max;
        }

        public double current() {
            return current;
        }

        public double max() {
            return max;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (DurabilityLikeInfo) obj;
            return Double.doubleToLongBits(this.current) == Double.doubleToLongBits(that.current)
                && Double.doubleToLongBits(this.max) == Double.doubleToLongBits(that.max);
        }

        @Override
        public int hashCode() {
            return Objects.hash(current, max);
        }

        @Override
        public String toString() {
            return "DurabilityLikeInfo[" + "current=" + current + ", " + "max=" + max + ']';
        }

    }

    public enum Style {
        NumPad,
        Bar,
        VerticalBar
    }

    @NotNull
    protected final ConfigDurabilityLike config;

    protected OverlayDuarbilityLike(@NotNull ConfigDurabilityLike config) {
        this.config = config;
    }

    private final @NotNull ArrayList<Pair<@NotNull Class<?>, @NotNull Function<@NotNull ItemStack, @Nullable DurabilityLikeInfo>>> handlers = new ArrayList<>();

    protected final void addHandler(@Nullable Class<?> clazz,
        @NotNull Function<@NotNull ItemStack, @Nullable DurabilityLikeInfo> handler) {
        if (clazz != null) handlers.add(Pair.of(clazz, handler));
    }

    protected @NotNull DurabilityLikeInfo getDurabilityLikeInfo(@NotNull ItemStack itemStack) {
        return handlers.stream()
            .filter(
                p -> p.getLeft()
                    .isInstance(itemStack.getItem()))
            .findFirst()
            .map(
                classFunctionPair -> classFunctionPair.getRight()
                    .apply(itemStack))
            .orElse(new DurabilityLikeInfo(0, 0));
    }

    protected int getColor(DurabilityLikeInfo info) {
        return config().colorType.get(info.percent(), config());
    }

    protected String getValue(DurabilityLikeInfo info) {
        return DurabilityFormatter.format(info.current, info.max, config().textFormat);
    }

    @Override
    public @Nullable OverlayRenderer getRenderer(@NotNull ItemStack itemStack) {
        if (!config().enabled) return null;
        var info = getDurabilityLikeInfo(itemStack);
        if (info.isNaN()) return null;
        if (!config().showWhenEmpty && info.isEmpty()) return null;
        if (!config().showWhenFull && info.isFull()) return null;
        String value = DurabilityFormatter.format(info.current, info.max, config().textFormat);
        switch (config().style) {
            case Bar:
                return new BarRenderer(getColor(info), info.percent());
            // case VerticalBar:
            // return new OverlayRenderer.VerticalBarRenderer(value, getColor(info), config().verticalBarPosition);
            default:
                break;
        }
        return new NumPadRenderer(getValue(info), getColor(info), config().numPadPosition);
    }
}
