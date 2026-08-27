package com.caedis.duradisplay.overlay;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.caedis.duradisplay.DuraDisplay;
import com.caedis.duradisplay.config.ConfigDurabilityLike;
import com.caedis.duradisplay.render.BarRenderer;
import com.caedis.duradisplay.render.OverlayRenderer;
import com.caedis.duradisplay.render.TextRenderer;
import com.caedis.duradisplay.render.VerticalBarRenderer;
import com.caedis.duradisplay.utils.DurabilityFormatter;
import com.caedis.duradisplay.utils.DurabilityLikeInfo;

public abstract class OverlayDurabilityLike extends Overlay<ConfigDurabilityLike> {

    public enum Style {
        Text,
        Bar,
        VerticalBar
    }

    @NotNull
    protected final ConfigDurabilityLike config;

    protected OverlayDurabilityLike(@NotNull ConfigDurabilityLike config) {
        this.config = config;
    }

    private final @NotNull ArrayList<Pair<@NotNull Class<?>, @NotNull Function<@NotNull ItemStack, @Nullable DurabilityLikeInfo>>> handlers = new ArrayList<>();

    // Cache resolved handler per Item class; skips the isInstance scan every frame
    private final @NotNull IdentityHashMap<Class<?>, Function<ItemStack, DurabilityLikeInfo>> handlerCache = new IdentityHashMap<>();

    private static final Function<ItemStack, DurabilityLikeInfo> NO_HANDLER = s -> null;

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
        var item = itemStack.getItem();
        if (item == null) return DurabilityLikeInfo.empty;

        Class<?> key = item.getClass();
        Function<ItemStack, DurabilityLikeInfo> handler = handlerCache.get(key);
        if (handler == null) {
            handler = NO_HANDLER;
            int handlerSize = handlers.size();
            for (int i = 0; i < handlerSize; i++) {
                var pair = handlers.get(i);
                if (pair.getLeft()
                    .isInstance(item)) {
                    handler = pair.getRight();
                    break;
                }
            }
            handlerCache.put(key, handler);
        }

        DurabilityLikeInfo info = handler.apply(itemStack);
        return info != null ? info : DurabilityLikeInfo.empty;
    }

    @Override
    public @Nullable OverlayRenderer getRenderer(@NotNull ItemStack itemStack) {
        final ConfigDurabilityLike cfg = config();
        if (!cfg.enabled) return null;
        var info = getDurabilityLikeInfo(itemStack);
        if (info.isNaN()) return null;
        if (!cfg.showWhenEmpty && info.isEmpty()) return null;
        if (!cfg.showWhenFull && info.isFull()) return null;

        final double percent = info.percent();
        final int color = cfg.colorType.get(percent, cfg);

        return switch (cfg.style) {
            case Bar -> BarRenderer.of(color, percent, cfg.smoothBar, cfg.barOffset, cfg.showBackground);
            case VerticalBar -> VerticalBarRenderer
                .of(color, percent, cfg.smoothBar, cfg.barOffset, cfg.showBackground);
            default -> TextRenderer
                .of(DurabilityFormatter.format(info.current, info.max, cfg.textFormat), color, cfg.numPadPosition);
        };
    }
}
