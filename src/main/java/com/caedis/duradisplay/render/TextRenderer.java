package com.caedis.duradisplay.render;

import net.minecraft.client.gui.FontRenderer;

public class TextRenderer extends OverlayRenderer {

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
    public Mode mode() {
        return Mode.TEXT;
    }

    @Override
    public void Render(FontRenderer fontRenderer, int xPosition, int yPosition) {
        int x = getX(fontRenderer, xPosition);
        int y = getY(yPosition);

        fontRenderer.drawString(value, x + 1, y, 0);
        fontRenderer.drawString(value, x - 1, y, 0);
        fontRenderer.drawString(value, x, y + 1, 0);
        fontRenderer.drawString(value, x, y - 1, 0);

        fontRenderer.drawString(value, x, y, color);
    }

}
