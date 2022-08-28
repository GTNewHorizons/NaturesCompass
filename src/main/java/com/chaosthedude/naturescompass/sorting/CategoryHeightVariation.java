package com.chaosthedude.naturescompass.sorting;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

public class CategoryHeightVariation implements ISortingCategory {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2) {
        return Float.compare(biome1.heightVariation, biome2.heightVariation);
    }

    @Override
    public Float getValue(BiomeGenBase biome) {
        return biome.heightVariation;
    }

    @Override
    public ISortingCategory next() {
        return new CategoryTemperature();
    }

    @Override
    public String getLocalizedName() {
        return I18n.format("string.naturescompass.heightVariation");
    }
}
