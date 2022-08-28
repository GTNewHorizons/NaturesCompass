package com.chaosthedude.naturescompass.sorting;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

public class CategoryTemperature implements ISortingCategory {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2) {
        return Float.compare(biome1.temperature, biome2.temperature);
    }

    @Override
    public Float getValue(BiomeGenBase biome) {
        return biome.temperature;
    }

    @Override
    public ISortingCategory next() {
        return new CategoryRainfall();
    }

    @Override
    public String getLocalizedName() {
        return I18n.format("string.naturescompass.temperature");
    }
}
