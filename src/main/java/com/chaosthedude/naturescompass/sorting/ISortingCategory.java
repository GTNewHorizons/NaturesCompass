package com.chaosthedude.naturescompass.sorting;

import java.util.Comparator;

import net.minecraft.world.biome.BiomeGenBase;

public interface ISortingCategory extends Comparator<BiomeGenBase> {

    @Override
    int compare(BiomeGenBase biome1, BiomeGenBase biome2);

    Object getValue(BiomeGenBase biome);

    ISortingCategory next();

    String getLocalizedName();
}
