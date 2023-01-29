package com.chaosthedude.naturescompass.sorting;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

import com.chaosthedude.naturescompass.util.BiomeUtils;

public class CategoryName implements ISortingCategory {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2) {
        return BiomeUtils.getBiomeName(biome1).compareTo(BiomeUtils.getBiomeName(biome2));
    }

    @Override
    public Object getValue(BiomeGenBase biome) {
        return null;
    }

    @Override
    public ISortingCategory next() {
        return new CategoryBaseHeight();
    }

    @Override
    public String getLocalizedName() {
        return I18n.format("string.naturescompass.name");
    }
}
