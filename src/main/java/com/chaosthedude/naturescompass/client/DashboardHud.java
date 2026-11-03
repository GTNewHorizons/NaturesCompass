package com.chaosthedude.naturescompass.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

import com.chaosthedude.naturescompass.config.ConfigHandler;
import com.gtnewhorizons.dashboard.api.HudBounds;
import com.gtnewhorizons.dashboard.api.HudElement;
import com.gtnewhorizons.dashboard.api.HudRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Lets players move, resize and hide the biome search text in Dashboard's HUD editor. The only class using Dashboard,
 * so only call it while Dashboard is loaded.
 */
@SideOnly(Side.CLIENT)
public final class DashboardHud {

    private static final int LINE_HEIGHT = 9;
    /** Wide enough for the coordinates line, which is the longest. */
    private static final int WIDTH = 130;
    /** The last line of each state's text, like maxLineNum counts them. */
    private static final int SEARCHING_LAST_LINE = 4;
    private static final int NOT_FOUND_LAST_LINE = 7;
    private static final int FOUND_LAST_LINE = 10;
    private static final int LABEL_COLOR = 0xFFFFFF;
    private static final int VALUE_COLOR = 0xAAAAAA;

    private static final HudElement BIOME_INFO = new HudElement("naturescompass:biome_info", DashboardHud::getBounds);

    private DashboardHud() {}

    public static void register() {
        BIOME_INFO.setPreview(
                (screenWidth, screenHeight) -> HudRegistry
                        .render(BIOME_INFO, () -> drawPreview(getBounds(screenWidth, screenHeight))));
        HudRegistry.register(BIOME_INFO);
    }

    /** False means the player hid the text, so skip drawing it and do not call {@link #endRender()}. */
    public static boolean beginBiomeInfo() {
        return HudRegistry.beginRender(BIOME_INFO);
    }

    public static void endRender() {
        HudRegistry.endRender();
    }

    /**
     * Like the real text: lines start 2 pixels in from its corner, 9 pixels apart, pushed down by lineOffset lines. At
     * the bottom, the text ends above the hotbar's height, so it grows upward with more lines.
     */
    private static HudBounds getBounds(int screenWidth, int screenHeight) {
        int lines = getLastLine() + 1;
        boolean right = isRight();
        boolean top = ConfigHandler.overlaySide == EnumOverlaySide.TOPLEFT
                || ConfigHandler.overlaySide == EnumOverlaySide.TOPRIGHT;
        int x = right ? screenWidth - 8 - WIDTH : 7;
        int y = top ? -2 : screenHeight - (lines - 1) * LINE_HEIGHT - 26;
        return new HudBounds(x, y + ConfigHandler.lineOffset * LINE_HEIGHT, WIDTH, lines * LINE_HEIGHT);
    }

    private static boolean isRight() {
        return ConfigHandler.overlaySide == EnumOverlaySide.TOPRIGHT
                || ConfigHandler.overlaySide == EnumOverlaySide.BOTTOMRIGHT;
    }

    /** The line count of the text drawn last, searching until then. */
    private static int getLastLine() {
        return Math.max(RenderTickHandler.maxLineNum, SEARCHING_LAST_LINE);
    }

    /** The text of the state the box is sized for, aligned to the side of the screen like the real one. */
    private static void drawPreview(HudBounds bounds) {
        List<String> lines = getSampleLines();
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        boolean right = isRight();
        for (int line = 0; line < lines.size(); line++) {
            String text = lines.get(line);
            int x = right ? bounds.getRight() - font.getStringWidth(text) : bounds.x;
            font.drawStringWithShadow(
                    text,
                    x,
                    bounds.y + line * LINE_HEIGHT,
                    line % 3 == 0 ? LABEL_COLOR : VALUE_COLOR);
        }
    }

    /** Searching, not found with the search radius, or found with where it is. */
    private static List<String> getSampleLines() {
        int last = getLastLine();
        String status = last == FOUND_LAST_LINE ? "found" : last == NOT_FOUND_LAST_LINE ? "notFound" : "searching";
        List<String> lines = new ArrayList<>(
                Arrays.asList(translate("status"), translate(status), "", translate("biome"), "Plains"));
        if (last == NOT_FOUND_LAST_LINE) {
            lines.addAll(Arrays.asList("", translate("radius"), "2500"));
        } else if (last == FOUND_LAST_LINE) {
            lines.addAll(Arrays.asList("", translate("coordinates"), "120, -340", "", translate("distance"), "360"));
        }
        return lines;
    }

    private static String translate(String key) {
        return I18n.format("string.naturescompass." + key);
    }
}
