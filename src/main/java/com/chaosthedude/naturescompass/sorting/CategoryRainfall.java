package com.chaosthedude.naturescompass.sorting;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

public class CategoryRainfall implements ISortingCategory {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2) {
        return Float.compare(biome1.rainfall, biome2.rainfall);
    }

    @Override
    public Float getValue(BiomeGenBase biome) {
        return biome.rainfall;
    }

    @Override
    public ISortingCategory next() {
        return new CategoryTags();
    }

    @Override
    public String getLocalizedName() {
        return I18n.format("string.naturescompass.rainfall");
    }
}
