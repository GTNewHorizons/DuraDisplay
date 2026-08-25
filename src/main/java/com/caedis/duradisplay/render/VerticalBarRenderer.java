package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class VerticalBarRenderer extends OverlayRenderer {

    private int color;
    private double durabilityPercent;
    private boolean smoothBar;
    private int offset;
    private boolean showBackground;

    public VerticalBarRenderer(int color, double durabilityPercent, boolean smoothBar, int offset,
        boolean showBackground) {
        this.color = color;
        this.durabilityPercent = durabilityPercent;
        this.smoothBar = smoothBar;
        this.offset = offset;
        this.showBackground = showBackground;
    }

    private static final VerticalBarRenderer reuse = new VerticalBarRenderer(0, 0, false, 0, false);

    public static VerticalBarRenderer of(int color, double durabilityPercent, boolean smoothBar, int offset,
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
        double height;
        if (smoothBar) height = durabilityPercent * 13.0;
        else height = Math.round(durabilityPercent * 13.0);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        // All quads share the vertex format, so one batch instead of a draw call each
        tessellator.startDrawingQuads();
        if (showBackground) {
            final int k = (int) Math.round(durabilityPercent * 255.0);
            final int i1 = (255 - k) / 4 << 16 | 0x3F00;
            addQuad(xPosition + offset, yPosition + 2, 2, 13, 0);
            addQuad(xPosition + offset, yPosition + 2, 1, 12, i1);
        }
        addQuad(xPosition + offset, yPosition + 2, 1, height, color);
        tessellator.draw();

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    }

    private static void addQuad(final double xPosition, final double yPosition, final double width, final double height,
        final int color) {
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex(xPosition, yPosition + 13 - height, 0.0D);
        tessellator.addVertex(xPosition, yPosition + 13, 0.0D);
        tessellator.addVertex(xPosition + width, yPosition + 13, 0.0D);
        tessellator.addVertex(xPosition + width, yPosition + 13 - height, 0.0D);
    }
}
