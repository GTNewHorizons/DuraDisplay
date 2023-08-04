package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;

import com.caedis.duradisplay.config.DuraDisplayConfig;

public class DurabilityOverlay extends ItemStackOverlay {

    @Override
    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition, float zLevel) {
        if (!DuraDisplayConfig.DurabilityConfig.ShowWhenFull && this.isFull) return;
        super.Render(fontRenderer, xPosition, yPosition, zLevel);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getLocation() {
        return DuraDisplayConfig.DurabilityConfig.Position;
    }
}
