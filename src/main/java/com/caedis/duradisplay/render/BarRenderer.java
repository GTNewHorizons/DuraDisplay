package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

public class BarRenderer extends OverlayRenderer {

    private int color;
    private double durabilityPercent;
    private boolean smoothBar;
    private int offset;
    private boolean showBackground;

    public BarRenderer(int color, double durabilityPercent, boolean smoothBar, int offset, boolean showBackground) {
        this.color = color;
        this.durabilityPercent = durabilityPercent;
        this.smoothBar = smoothBar;
        this.offset = offset;
        this.showBackground = showBackground;
    }

    private static final BarRenderer reuse = new BarRenderer(0, 0, false, 0, false);

    public static BarRenderer of(int color, double durabilityPercent, boolean smoothBar, int offset,
        boolean showBackground) {
        reuse.color = color;
        reuse.durabilityPercent = durabilityPercent;
        reuse.smoothBar = smoothBar;
        reuse.offset = offset;
        reuse.showBackground = showBackground;
        return reuse;
    }

    private static final Tessellator tessellator = Tessellator.instance;

    @Override
    public Mode mode() {
        return Mode.QUAD;
    }

    @Override
    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition) {
        double length;
        if (smoothBar) length = durabilityPercent * 13.0;
        else length = Math.round(durabilityPercent * 13.0);
        if (showBackground) {
            final int k = (int) Math.round(durabilityPercent * 255.0);
            final int i1 = (255 - k) / 4 << 16 | 16128;
            addQuad(xPosition + 2, yPosition + 14 - offset, 13, 2, 0);
            addQuad(xPosition + 2, yPosition + 14 - offset, 12, 1, i1);
        }
        addQuad(xPosition + 2, yPosition + 14 - offset, length, 1, color);
    }

    private static void addQuad(final double xPosition, final double yPosition, final double width, final double height,
        final int color) {
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex(xPosition, yPosition, 0.0D);
        tessellator.addVertex(xPosition, yPosition + height, 0.0D);
        tessellator.addVertex(xPosition + width, yPosition + height, 0.0D);
        tessellator.addVertex(xPosition + width, yPosition, 0.0D);
    }
}
