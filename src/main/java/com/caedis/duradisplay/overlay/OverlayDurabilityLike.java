package com.caedis.duradisplay.overlay;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.DuraDisplay;
import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.render.BarRenderer;
import com.caedis.duradisplay.render.NumPadRenderer;
import com.caedis.duradisplay.render.OverlayRenderer;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;

public abstract class OverlayDurabilityLike extends Overlay<ConfigDurabilityLike> {

    public enum Style {
        NumPad,
        Bar,
        VerticalBar
    }

    @NotNull
    protected final ConfigDurabilityLike config;

    protected OverlayDurabilityLike(@NotNull ConfigDurabilityLike config) {
        this.config = config;
    }

    private final @NotNull ArrayList<Pair<@NotNull Class<?>, @NotNull Function<@NotNull ItemStack, @Nullable DurabilityLikeInfo>>> handlers = new ArrayList<>();

    private void addHandler(@Nullable Class<?> clazz,
        @NotNull Function<@NotNull ItemStack, @Nullable DurabilityLikeInfo> handler) {
        if (clazz != null) handlers.add(Pair.of(clazz, handler));
    }

    protected final void addHandler(@NotNull String className,
        @NotNull Function<@NotNull ItemStack, @Nullable DurabilityLikeInfo> handler) {
        try {
            addHandler(Class.forName(className), handler);
        } catch (ClassNotFoundException e) {
            DuraDisplay.LOG.info(String.format("Class %s not found, Overlay won't be added", className));
        }
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
        if (Objects.requireNonNull(config().style) == Style.Bar) {
            return new BarRenderer(getColor(info), info.percent(), config().smoothBar);
        }
        return new NumPadRenderer(getValue(info), getColor(info), config().numPadPosition);
    }
}
