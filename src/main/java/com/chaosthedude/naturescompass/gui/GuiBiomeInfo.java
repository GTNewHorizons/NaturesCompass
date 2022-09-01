package com.chaosthedude.naturescompass.gui;

import com.chaosthedude.naturescompass.util.BiomeUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

@SideOnly(Side.CLIENT)
public class GuiBiomeInfo extends GuiScreen {

    private GuiNaturesCompass parentScreen;
    private BiomeGenBase biome;
    private GuiButton searchButton;
    private GuiButton backButton;
    private String baseHeight;
    private String heightVariation;
    private String precipitation;
    private String climate;
    private String humidity;
    private List<String> biomeTags = new ArrayList<String>();

    public GuiBiomeInfo(GuiNaturesCompass parentScreen, BiomeGenBase biome) {
        this.parentScreen = parentScreen;
        this.biome = biome;

        if (biome.rootHeight < -1.0f) {
            baseHeight = I18n.format("string.naturescompass.veryLow");
        } else if (biome.rootHeight < 0.0f) {
            baseHeight = I18n.format("string.naturescompass.low");
        } else if (biome.rootHeight < 0.4f) {
            baseHeight = I18n.format("string.naturescompass.average");
        } else if (biome.rootHeight < 1.0f) {
            baseHeight = I18n.format("string.naturescompass.high");
        } else {
            baseHeight = I18n.format("string.naturescompass.veryHigh");
        }
        baseHeight += "(" + biome.rootHeight + ")";

        if (biome.heightVariation < 0.3f) {
            heightVariation = I18n.format("string.naturescompass.average");
        } else if (biome.heightVariation < 0.6f) {
            heightVariation = I18n.format("string.naturescompass.high");
        } else {
            heightVariation = I18n.format("string.naturescompass.veryHigh");
        }
        heightVariation += "(" + biome.heightVariation + ")";

        if (biome.getEnableSnow()) {
            precipitation = I18n.format("string.naturescompass.snow");
        } else if (biome.rainfall > 0.0f) {
            precipitation = I18n.format("string.naturescompass.rain");
        } else {
            precipitation = I18n.format("string.naturescompass.none");
        }

        climate = BiomeUtils.getBiomeClimate(biome);
        climate += "(" + (int) (biome.temperature * 100) + "\u00B0F)";

        humidity = BiomeUtils.getBiomeHumidity(biome);
        humidity += "(" + (int) (biome.rainfall * 100) + "%)";

        int length = 0;
        StringBuilder tags = new StringBuilder();
        for (String tag : BiomeUtils.getListBiomeTags(biome)) {
            tag = tag.substring(0, 1).toUpperCase() + tag.substring(1);
            length += tag.length();
            if (length <= 20) {
                tags.append(tag).append(", ");
            } else {
                biomeTags.add(tags.toString());
                tags = new StringBuilder(tag + ", ");
                length = tag.length();
            }
        }
        tags = new StringBuilder(tags.substring(0, tags.length() - 2));
        biomeTags.add(tags.toString());
    }

    @Override
    public void initGui() {
        setupButtons();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button == searchButton) {
                parentScreen.searchForBiome(biome);
            } else if (button == backButton) {
                mc.displayGuiScreen(parentScreen);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, BiomeUtils.getBiomeName(biome), width / 2, 20, 0xffffff);

        drawString(fontRendererObj, I18n.format("string.naturescompass.precipitation"), width / 2 - 100, 40, 0xffffff);
        drawString(fontRendererObj, precipitation, width / 2 - 100, 50, 0x808080);

        drawString(fontRendererObj, I18n.format("string.naturescompass.baseHeight"), width / 2 - 100, 70, 0xffffff);
        drawString(fontRendererObj, baseHeight, width / 2 - 100, 80, 0x808080);

        drawString(fontRendererObj, I18n.format("string.naturescompass.humidity"), width / 2 - 100, 100, 0xffffff);
        drawString(fontRendererObj, humidity, width / 2 - 100, 110, 0x808080);

        drawString(fontRendererObj, I18n.format("string.naturescompass.climate"), width / 2 + 40, 40, 0xffffff);
        drawString(fontRendererObj, climate, width / 2 + 40, 50, 0x808080);

        drawString(fontRendererObj, I18n.format("string.naturescompass.heightVariation"), width / 2 + 40, 70, 0xffffff);
        drawString(fontRendererObj, heightVariation, width / 2 + 40, 80, 0x808080);

        drawString(fontRendererObj, I18n.format("string.naturescompass.tags"), width / 2 + 40, 100, 0xffffff);
        for (int i = 0; i < biomeTags.size(); i++) {
            drawString(fontRendererObj, biomeTags.get(i), width / 2 + 40, 110 + i * 10, 0x808080);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SuppressWarnings("unchecked")
    protected <T extends GuiButton> T addButton(T button) {
        buttonList.add(button);
        return (T) button;
    }

    private void setupButtons() {
        buttonList.clear();
        backButton = addButton(new GuiTransparentButton(
                0, width / 2 - 154, height - 52, 150, 20, I18n.format("string.naturescompass.back")));
        searchButton = addButton(new GuiTransparentButton(
                1, width / 2 + 4, height - 52, 150, 20, I18n.format("string.naturescompass.search")));
    }
}
