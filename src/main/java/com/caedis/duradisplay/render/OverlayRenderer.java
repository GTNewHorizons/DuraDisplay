package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.mixins.interfaces.FontRendererAccessor;

import cpw.mods.fml.common.Loader;

public abstract class OverlayRenderer {

    /**
     * GL setup is identical for every renderer of a mode, so {@link DurabilityRenderer} sets it up once per run of
     * same-mode renderers instead of once per renderer. Declaration order is the draw order handlers get sorted into,
     * so bars land under text.
     */
    public enum Mode {
        QUAD,
        TEXT
    }

    private static final boolean ANGELICA_LOADED = Loader.isModLoaded("angelica");

    public abstract Mode mode();

    public abstract void Render(FontRenderer fontRenderer, int xPosition, int yPosition);

    public static void begin(Mode mode, FontRenderer fontRenderer) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        switch (mode) {
            case TEXT -> {
                GL11.glPushMatrix();
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glTranslatef(0, 0, 50);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                if (ANGELICA_LOADED && fontRenderer instanceof FontRendererAccessor accessor)
                    accessor.angelica$getBatcher()
                        .beginBatch();
            }
            case QUAD -> {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                // All quads share the vertex format, so one batch covers every bar on this item
                Tessellator.instance.startDrawingQuads();
            }
        }
    }

    public static void end(Mode mode, FontRenderer fontRenderer) {
        switch (mode) {
            case TEXT -> {
                if (ANGELICA_LOADED && fontRenderer instanceof FontRendererAccessor accessor)
                    accessor.angelica$getBatcher()
                        .endBatch();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
            case QUAD -> {
                Tessellator.instance.draw();
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
