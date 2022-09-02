package com.chaosthedude.naturescompass.gui;

import com.chaosthedude.naturescompass.NaturesCompass;
import com.chaosthedude.naturescompass.items.ItemNaturesCompass;
import com.chaosthedude.naturescompass.network.PacketCompassSearch;
import com.chaosthedude.naturescompass.network.PacketTeleport;
import com.chaosthedude.naturescompass.sorting.CategoryName;
import com.chaosthedude.naturescompass.sorting.ISortingCategory;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import com.chaosthedude.naturescompass.util.BiomeUtils;
import com.chaosthedude.naturescompass.util.EnumCompassState;
import com.chaosthedude.naturescompass.util.PlayerUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiNaturesCompass extends GuiScreen {

    private World world;
    private EntityPlayer player;
    private ItemStack stack;
    private ItemNaturesCompass natureCompass;
    private List<BiomeGenBase> allowedBiomes;
    private List<BiomeGenBase> biomesMatchingSearch;
    private GuiButton searchButton;
    private GuiButton teleportButton;
    private GuiButton infoButton;
    private GuiButton cancelButton;
    private GuiButton sortByButton;
    private GuiTransparentTextField searchTextField;
    private GuiListBiomes selectionList;
    private ISortingCategory sortingCategory;

    public GuiNaturesCompass(
            World world,
            EntityPlayer player,
            ItemStack stack,
            ItemNaturesCompass natureCompass,
            List<BiomeGenBase> allowedBiomes) {
        this.world = world;
        this.player = player;
        this.stack = stack;
        this.natureCompass = natureCompass;
        this.allowedBiomes = allowedBiomes;

        sortingCategory = new CategoryName();
        biomesMatchingSearch = new ArrayList<BiomeGenBase>(allowedBiomes);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        setupButtons();
        setupTextFields();
        selectionList = new GuiListBiomes(this, mc, width + 110, height, 40, height, 36);
    }

    @Override
    public void updateScreen() {
        searchTextField.updateCursorCounter();
        teleportButton.visible = NaturesCompass.canTeleport || PlayerUtils.cheatModeEnabled(player);
        teleportButton.enabled = natureCompass.getState(stack) == EnumCompassState.FOUND;
        updateBiomes();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            GuiListBiomesEntry biomesEntry = selectionList.getSelectedBiome();
            if (button == searchButton) {
                if (biomesEntry != null) {
                    biomesEntry.selectBiome();
                }
            } else if (button == teleportButton) {
                teleport();
            } else if (button == infoButton) {
                biomesEntry.viewInfo();
            } else if (button == sortByButton) {
                sortingCategory = sortingCategory.next();
                sortByButton.displayString =
                        I18n.format("string.naturescompass.sortBy") + ": " + sortingCategory.getLocalizedName();
                selectionList.refreshList();
            } else if (button == cancelButton) {
                mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        selectionList.drawScreen(mouseX, mouseY, partialTicks);
        searchTextField.drawTextBox();
        drawCenteredString(fontRendererObj, I18n.format("string.naturescompass.selectBiome"), 65, 15, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        selectionList.func_148179_a(mouseX, mouseY, mouseButton);
        searchTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (searchTextField.isFocused()) {
            searchTextField.textboxKeyTyped(typedChar, keyCode);
            processSearchTerm();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void selectBiome(GuiListBiomesEntry entry) {
        boolean enable = entry != null;
        searchButton.enabled = enable;
        infoButton.enabled = enable;
    }

    public void searchForBiome(BiomeGenBase biome) {
        NaturesCompass.network.sendToServer(
                new PacketCompassSearch(biome.biomeID, (int) player.posX, (int) player.posZ));
        mc.displayGuiScreen(null);
    }

    public void updateBiomes() {
        if ((natureCompass.getState(stack) == EnumCompassState.INACTIVE)
                || (BiomeSearchWorker.oldDimensionId != world.provider.dimensionId)) {
            BiomeSearchWorker.completedSearch = false;
            BiomeSearchWorker.availableBiomes.clear();
        }
        if (BiomeSearchWorker.completedSearch
                && (BiomeSearchWorker.oldDimensionId == world.provider.dimensionId)
                && !searchTextField.isFocused()) {
            allowedBiomes = biomesMatchingSearch = new ArrayList<BiomeGenBase>(BiomeSearchWorker.availableBiomes);
            selectionList.refreshList();
        }
    }

    public void teleport() {
        NaturesCompass.network.sendToServer(new PacketTeleport());
        mc.displayGuiScreen(null);
    }

    public ISortingCategory getSortingCategory() {
        return sortingCategory;
    }

    private Set<BiomeGenBase> findMatchedBiomes(String category, String value) {

        Set<BiomeGenBase> biomeMatching = new HashSet<BiomeGenBase>();

        if (category.equals(I18n.format("string.naturescompass.name").toLowerCase())) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (BiomeUtils.getBiomeName(biome).toLowerCase().contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(I18n.format("string.naturescompass.climate").toLowerCase())) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (BiomeUtils.getBiomeClimate(biome).toLowerCase().contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(
                        I18n.format("string.naturescompass.baseHeight").toLowerCase())
                || category.equals("bh")) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (String.valueOf(biome.rootHeight).contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(
                        I18n.format("string.naturescompass.heightVariation").toLowerCase())
                || category.equals("hv")) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (String.valueOf(biome.heightVariation).contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(I18n.format("string.naturescompass.rainfall").toLowerCase())
                || category.equals("rain")) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (String.valueOf(biome.rainfall).contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(
                        I18n.format("string.naturescompass.temperature").toLowerCase())
                || category.equals("temp")) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (String.valueOf(biome.temperature).contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(I18n.format("string.naturescompass.humidity").toLowerCase())) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (BiomeUtils.getBiomeHumidity(biome).toLowerCase().contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        } else if (category.equals(I18n.format("string.naturescompass.tags").toLowerCase()) || category.equals("tag")) {
            for (BiomeGenBase biome : allowedBiomes) {
                if (BiomeUtils.getListBiomeTags(biome).contains(value)) {
                    biomeMatching.add(biome);
                }
            }
        }
        return biomeMatching;
    }

    public void processSearchTerm() {
        String text = searchTextField.getText().toLowerCase();
        String[] arrayText = text.trim().split("\\s*,\\s*");
        Set<BiomeGenBase> biomeMatchingTemp2 = new HashSet<>(allowedBiomes);

        for (String part : arrayText) {
            String[] arrayPart = part.trim().split("\\s*:\\s*", 2);
            Set<BiomeGenBase> biomeMatchingTemp1 = new HashSet<BiomeGenBase>();

            if (arrayText.length == 1 && arrayPart.length == 1) {
                biomeMatchingTemp1 = findMatchedBiomes(
                        getSortingCategory().getLocalizedName().toLowerCase(), arrayPart[0]);
            } else if (arrayPart.length == 2) {
                biomeMatchingTemp1 = findMatchedBiomes(arrayPart[0], arrayPart[1]);
            }
            biomeMatchingTemp2.retainAll(biomeMatchingTemp1);
        }
        biomesMatchingSearch = new ArrayList<>(biomeMatchingTemp2);

        selectionList.refreshList();
    }

    public List<BiomeGenBase> sortBiomes() {
        final List<BiomeGenBase> biomes = biomesMatchingSearch;
        Collections.sort(biomes, new CategoryName());
        Collections.sort(biomes, sortingCategory);

        return biomes;
    }

    @SuppressWarnings("unchecked")
    protected <T extends GuiButton> T addButton(T button) {
        buttonList.add(button);
        return (T) button;
    }

    private void setupButtons() {
        buttonList.clear();
        cancelButton = addButton(new GuiTransparentButton(0, 10, height - 30, 110, 20, I18n.format("gui.cancel")));
        sortByButton = addButton(new GuiTransparentButton(
                1,
                10,
                90,
                110,
                20,
                I18n.format("string.naturescompass.sortBy") + ": " + sortingCategory.getLocalizedName()));
        infoButton = addButton(new GuiTransparentButton(2, 10, 65, 110, 20, I18n.format("string.naturescompass.info")));
        searchButton =
                addButton(new GuiTransparentButton(3, 10, 40, 110, 20, I18n.format("string.naturescompass.search")));
        teleportButton = addButton(
                new GuiTransparentButton(4, 10, height - 55, 110, 20, I18n.format("string.naturescompass.teleport")));

        searchButton.enabled = false;
        infoButton.enabled = false;

        teleportButton.visible = NaturesCompass.canTeleport || PlayerUtils.cheatModeEnabled(player);
    }

    private void setupTextFields() {
        searchTextField = new GuiTransparentTextField(fontRendererObj, 130, 10, width - 200, 20);
        searchTextField.setLabel(I18n.format("string.naturescompass.search"));
    }
}
