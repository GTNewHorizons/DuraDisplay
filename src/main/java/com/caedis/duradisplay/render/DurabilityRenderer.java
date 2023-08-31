package com.caedis.duradisplay.render;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import com.caedis.duradisplay.overlay.Overlay;
import com.caedis.duradisplay.overlay.OverlayCharge;
import com.caedis.duradisplay.overlay.OverlayDurability;

public class DurabilityRenderer {

    // Used to prevent calls from outside actual inventories
    public static boolean Execute = true;

    private static ArrayList<Overlay<?>> handlers;

    public static void addHandlers(Overlay<?> handler) {
        if (handlers == null) handlers = new ArrayList<>();
        handlers.add(handler);
    }

    static {
        addHandlers(new OverlayDurability());
        addHandlers(new OverlayCharge());
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
