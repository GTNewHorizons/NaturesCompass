package com.chaosthedude.naturescompass.sorting;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.BiomeGenBase;

import com.chaosthedude.naturescompass.util.BiomeUtils;

public class CategoryTags implements ISortingCategory {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2) {
        return getValue(biome1).compareTo(getValue(biome2));
    }

    @Override
    public String getValue(BiomeGenBase biome) {
        return BiomeUtils.getBiomeTags(biome);
    }

    @Override
    public ISortingCategory next() {
        return new CategoryName();
    }

    @Override
    public String getLocalizedName() {
        return I18n.format("string.naturescompass.tags");
    }
}
