package com.chaosthedude.naturescompass.sorting;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

public class CategoryBaseHeight implements ISortingCategory {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2) {
        return Float.compare(biome1.rootHeight, biome2.rootHeight);
    }

    @Override
    public Float getValue(BiomeGenBase biome) {
        return biome.rootHeight;
    }

    @Override
    public ISortingCategory next() {
        return new CategoryHeightVariation();
    }

    @Override
    public String getLocalizedName() {
        return I18n.format("string.naturescompass.baseHeight");
    }
}
