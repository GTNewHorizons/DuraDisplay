package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

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
    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition) {
        double length;
        if (smoothBar) length = durabilityPercent * 13.0;
        else length = Math.round(durabilityPercent * 13.0);
        final int k = (int) Math.round(durabilityPercent * 255.0);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        final int i1 = (255 - k) / 4 << 16 | 16128;

        if (showBackground) {
            renderQuad(xPosition + 2, yPosition + 14 - offset, 13, 2, 0);
            renderQuad(xPosition + 2, yPosition + 14 - offset, 12, 1, i1);
        }
        renderQuad(xPosition + 2, yPosition + 14 - offset, length, 1, color);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    }

    private static void renderQuad(final double xPosition, final double yPosition, final double width,
        final double height, final int color) {
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex(xPosition, yPosition, 0.0D);
        tessellator.addVertex(xPosition, yPosition + height, 0.0D);
        tessellator.addVertex(xPosition + width, yPosition + height, 0.0D);
        tessellator.addVertex(xPosition + width, yPosition, 0.0D);
        tessellator.draw();
    }
}
