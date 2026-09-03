package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.mixins.interfaces.FontRendererAccessor;

import cpw.mods.fml.common.Loader;

public class TextRenderer extends OverlayRenderer {

    private static final boolean ANGELICA_LOADED = Loader.isModLoaded("angelica");

    private String value;
    private int color;
    private int position;

    private static final TextRenderer reuse = new TextRenderer(null, 0, 0);

    public static TextRenderer of(String value, int color, int numpadPosition) {
        reuse.value = value;
        reuse.color = color;
        reuse.position = numpadPosition;
        return reuse;
    }

    private int getX(FontRenderer fontRenderer, int xPosition) {
        // left needs no measuring; getStringWidth walks every char
        if (position == 1 || position == 4 || position == 7) return (xPosition * 2) + 2;

        final int stringWidth = fontRenderer.getStringWidth(value);
        return switch (position) {
            case 3, 6, 9 -> // right
                (xPosition + 20) * 2 - stringWidth - 10;
            // 2, 5, 8
            default -> // center
                ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
        };
    }

    private int getY(int yPosition) {
        switch (position) {
            case 7, 8, 9 -> { // top
                return (yPosition * 2) + 2;
            }
            case 4, 5, 6 -> { // center
                return (yPosition * 2) + 11;
            }
            // 1, 2, 3
            default -> { // bottom
                return (yPosition * 2) + 22;
            }
        }
    }

    public TextRenderer(String value, int color, int NumpadPosition) {
        this.value = value;
        this.color = color;
        this.position = NumpadPosition;
    }

    @Override
    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glTranslatef(0, 0, 50);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int x = getX(fontRenderer, xPosition);
        int y = getY(yPosition);

        if (ANGELICA_LOADED) AngelicaBatch.begin(fontRenderer);
        try {
            fontRenderer.drawString(value, x + 1, y, 0);
            fontRenderer.drawString(value, x - 1, y, 0);
            fontRenderer.drawString(value, x, y + 1, 0);
            fontRenderer.drawString(value, x, y - 1, 0);

            fontRenderer.drawString(value, x, y, color);
        } finally {
            if (ANGELICA_LOADED) AngelicaBatch.end(fontRenderer);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    private static class AngelicaBatch {

        private static void begin(FontRenderer fontRenderer) {
            if (fontRenderer instanceof FontRendererAccessor accessor) accessor.angelica$getBatcher()
                .beginBatch();
        }

        private static void end(FontRenderer fontRenderer) {
            if (fontRenderer instanceof FontRendererAccessor accessor) accessor.angelica$getBatcher()
                .endBatch();
        }
    }

}
