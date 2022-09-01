package com.chaosthedude.naturescompass.util;

import com.chaosthedude.naturescompass.client.EnumOverlaySide;
import com.chaosthedude.naturescompass.client.RenderTickHandler;
import com.chaosthedude.naturescompass.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final FontRenderer fontRenderer = mc.fontRenderer;

    public static void drawLineOffsetStringOnHUD(String string, int xOffset, int yOffset, int color, int lineOffset) {
        final ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        if (ConfigHandler.overlaySide == EnumOverlaySide.TOPLEFT) {
            drawStringOnHUD(string, xOffset, yOffset - 4, color, ConfigHandler.lineOffset + lineOffset);
        } else if (ConfigHandler.overlaySide == EnumOverlaySide.TOPRIGHT) {
            drawStringOnHUD(
                    string,
                    resolution.getScaledWidth() - fontRenderer.getStringWidth(string) - 10,
                    yOffset - 4,
                    color,
                    ConfigHandler.lineOffset + lineOffset);
        } else if (ConfigHandler.overlaySide == EnumOverlaySide.BOTTOMLEFT) {
            drawStringOnHUD(
                    string,
                    xOffset,
                    resolution.getScaledHeight() - RenderTickHandler.maxLineNum * 9 - 28,
                    color,
                    ConfigHandler.lineOffset + lineOffset);
        } else {
            drawStringOnHUD(
                    string,
                    resolution.getScaledWidth() - fontRenderer.getStringWidth(string) - 10,
                    resolution.getScaledHeight() - RenderTickHandler.maxLineNum * 9 - 28,
                    color,
                    ConfigHandler.lineOffset + lineOffset);
        }
    }

    public static void drawStringOnHUD(String string, int xOffset, int yOffset, int color, int lineOffset) {
        yOffset += lineOffset * 9;
        fontRenderer.drawString(string, 2 + xOffset, 2 + yOffset, color, true);
    }
}
