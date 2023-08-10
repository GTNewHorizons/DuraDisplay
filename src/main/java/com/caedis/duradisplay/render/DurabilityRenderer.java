package com.caedis.duradisplay.render;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class DurabilityRenderer {

    // Used to prevent calls from outside actual inventories
    public static boolean Execute = true;

    private static ArrayList<com.caedis.duradisplay.render.ItemHandler> handlers;

    public static void addHandlers(ItemHandler handler) {
        if (handlers == null) handlers = new ArrayList<>();
        handlers.add(handler);
    }

    public static void Render(FontRenderer fontRenderer, ItemStack stack, int xPosition, int yPosition) {
        if (fontRenderer == null && (fontRenderer = Minecraft.getMinecraft().fontRenderer) == null) return;

        for (com.caedis.duradisplay.render.ItemHandler f : handlers) {
            ItemStackOverlay fOverlay = f.getOverlay(stack);
            if (fOverlay != null) {
                fOverlay.Render(fontRenderer, xPosition, yPosition);
            }
        }

    }

}
