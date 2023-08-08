package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

public abstract class ItemStackOverlay {

    public abstract String getValue();

    public abstract int getColor();

    public abstract int getLocation();

    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition, float zLevel) {
        String value = getValue();
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glTranslatef(0, 0, zLevel + 50);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int stringWidth = fontRenderer.getStringWidth(value);
        int x = getX(xPosition, stringWidth);
        int y = getY(yPosition);

        fontRenderer.drawString(value, x + 1, y, 0);
        fontRenderer.drawString(value, x - 1, y, 0);
        fontRenderer.drawString(value, x, y + 1, 0);
        fontRenderer.drawString(value, x, y - 1, 0);

        fontRenderer.drawString(value, x, y, getColor());
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    private int getX(int xPosition, int stringWidth) {
        switch (getLocation()) {
            case 1, 4, 7 -> { // left
                return (xPosition * 2) + 2;
            }
            // 2, 5, 8
            default -> { // center
                return ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
            }
            case 3, 6, 9 -> { // right
                return (xPosition + 20) * 2 - stringWidth - 10;
            }
        }
    }

    private int getY(int yPosition) {
        switch (getLocation()) {
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

}
