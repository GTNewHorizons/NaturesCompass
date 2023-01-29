package com.chaosthedude.naturescompass.sorting;

import java.util.Comparator;

import net.minecraft.world.biome.BiomeGenBase;

public interface ISortingCategory extends Comparator<BiomeGenBase> {

    @Override
    public int compare(BiomeGenBase biome1, BiomeGenBase biome2);

    public Object getValue(BiomeGenBase biome);

    public ISortingCategory next();

    public String getLocalizedName();
}
