package com.caedis.duradisplay.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import com.caedis.duradisplay.overlay.Overlay;
import com.caedis.duradisplay.overlay.OverlayInfo;

public class DurabilityRenderer {

    // Used to prevent calls from outside actual inventories
    public static boolean Execute = true;

    private static final Overlay<?>[] handlers = OverlayInfo.getOverlays();

    public static void Render(FontRenderer fontRenderer, ItemStack stack, int xPosition, int yPosition) {
        if (fontRenderer == null && (fontRenderer = Minecraft.getMinecraft().fontRenderer) == null) return;

        for (Overlay<?> handler : handlers) {
            var fOverlay = handler.getRenderer(stack);
            if (fOverlay != null) {
                fOverlay.Render(fontRenderer, xPosition, yPosition);
            }
        }
    }

}
