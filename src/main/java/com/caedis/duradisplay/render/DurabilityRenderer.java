package com.caedis.duradisplay.render;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import com.caedis.duradisplay.config.Config;
import com.caedis.duradisplay.overlay.Overlay;
import com.caedis.duradisplay.overlay.OverlayInfo;

public class DurabilityRenderer {

    // Used to prevent calls from outside actual inventories
    public static boolean Execute = true;

    private static ArrayList<Overlay<? extends Config>> handlers;

    public static void addHandlers(Overlay<? extends Config> handler) {
        if (handlers == null) handlers = new ArrayList<>();
        handlers.add(handler);
    }

    static {
        Arrays.stream(OverlayInfo.getOverlays())
            .forEach(DurabilityRenderer::addHandlers);
    }

    public static void Render(FontRenderer fontRenderer, ItemStack stack, int xPosition, int yPosition) {
        if (fontRenderer == null && (fontRenderer = Minecraft.getMinecraft().fontRenderer) == null) return;

        for (var f : handlers) {
            var fOverlay = f.getRenderer(stack);
            if (fOverlay != null) {
                fOverlay.Render(fontRenderer, xPosition, yPosition);
            }
        }

    }

}
