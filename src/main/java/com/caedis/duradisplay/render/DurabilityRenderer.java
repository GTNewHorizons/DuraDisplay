package com.caedis.duradisplay.render;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import com.caedis.duradisplay.overlay.Overlay;
import com.caedis.duradisplay.overlay.OverlayInfo;

public class DurabilityRenderer {

    // Used to prevent calls from outside actual inventories
    public static boolean Execute = true;

    // Cloned; sorted in place, and OverlayInfo hands out its own cached array
    private static final Overlay<?>[] handlers = OverlayInfo.getOverlays()
        .clone();

    // Groups handlers by render mode. Stable, so same-mode overlays keep their draw order.
    public static void partitionByMode() {
        Arrays.sort(handlers, Comparator.comparing(Overlay::mode));
    }

    public static void Render(FontRenderer fontRenderer, ItemStack stack, int xPosition, int yPosition) {
        if (fontRenderer == null && (fontRenderer = Minecraft.getMinecraft().fontRenderer) == null) return;

        // Set GL state once per run of same-mode renderers
        OverlayRenderer.Mode active = null;
        try {
            for (Overlay<?> handler : handlers) {
                var fOverlay = handler.getRenderer(stack);
                if (fOverlay == null) continue;

                final OverlayRenderer.Mode mode = fOverlay.mode();
                if (mode != active) {
                    if (active != null) OverlayRenderer.end(active, fontRenderer);
                    OverlayRenderer.begin(mode, fontRenderer);
                    active = mode;
                }
                fOverlay.Render(fontRenderer, xPosition, yPosition);
            }
        } finally {
            if (active != null) OverlayRenderer.end(active, fontRenderer);
        }
    }

}
