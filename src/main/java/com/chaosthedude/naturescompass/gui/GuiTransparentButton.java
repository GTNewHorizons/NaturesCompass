package com.chaosthedude.naturescompass.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiTransparentButton extends GuiButton {

    public GuiTransparentButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            field_146123_n = mouseX >= xPosition && mouseY >= yPosition
                    && mouseX < xPosition + width
                    && mouseY < yPosition + height;
            final float state = getHoverState(field_146123_n);
            final float f = state / 2 * 0.9F + 0.1F;
            final int color = (int) (255.0F * f);

            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, color / 2 << 24);
            drawCenteredString(
                    mc.fontRenderer,
                    displayString,
                    xPosition + width / 2,
                    yPosition + (height - 8) / 2,
                    0xffffff);
        }
    }

    @Override
    public int getHoverState(boolean mouseOver) {
        int state = 2;
        if (!enabled) {
            state = 5;
        } else if (mouseOver) {
            state = 4;
        }

        return state;
    }
}
