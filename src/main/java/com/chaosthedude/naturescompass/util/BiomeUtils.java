package com.chaosthedude.naturescompass.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import com.chaosthedude.naturescompass.config.ConfigHandler;

public class BiomeUtils {

    public static List<BiomeGenBase> getAllowedBiomes() {
        final List<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null && !biomeIsBlacklisted(biome)) {
                biomes.add(biome);
            }
        }

        return biomes;
    }

    public static int getDistanceToBiome(EntityPlayer player, int x, int z) {
        return (int) player.getDistance(x, player.posY, z);
    }

    public static String getBiomeName(BiomeGenBase biome) {
        if (biome != null && biome.biomeName != null) {
            if (ConfigHandler.fixBiomeNames) {
                final String original = biome.biomeName;
                String fixed = "";
                char pre = ' ';
                for (int i = 0; i < original.length(); i++) {
                    final char c = original.charAt(i);
                    if (Character.isUpperCase(c) && Character.isLowerCase(pre) && Character.isAlphabetic(pre)) {
                        fixed = fixed + " ";
                    }
                    fixed = fixed + String.valueOf(c);
                    pre = c;
                }

                return fixed;
            }

            return biome.biomeName;
        }

        return "";
    }

    public static String getBiomeName(int biomeID) {
        return getBiomeName(BiomeGenBase.getBiome(biomeID));
    }

    public static List<String> getListBiomeTags(BiomeGenBase biome) {
        return Stream.of(BiomeDictionary.getTypesForBiome(biome)).map(BiomeDictionary.Type::name)
                .map(String::toLowerCase).sorted().collect(Collectors.toList());
    }

    public static String getBiomeTags(BiomeGenBase biome) {
        return Stream.of(BiomeDictionary.getTypesForBiome(biome)).map(BiomeDictionary.Type::name)
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()).sorted()
                .collect(Collectors.joining(", "));
    }

    public static String getBiomeClimate(BiomeGenBase biome) {

        if (getListBiomeTags(biome).contains("nether")) {
            return I18n.format("string.naturescompass.hellish");
        } else if (biome.temperature > 1.0f) {
            return I18n.format("string.naturescompass.hot");
        } else if (biome.temperature > 0.85f) {
            return I18n.format("string.naturescompass.warm");
        } else if (biome.temperature > 0.35f) {
            return I18n.format("string.naturescompass.normal");
        } else if (biome.temperature > 0.0f) {
            return I18n.format("string.naturescompass.cold");
        }
        return I18n.format("string.naturescompass.icy");
    }

    public static String getBiomeHumidity(BiomeGenBase biome) {
        if (biome.rainfall > 0.85f) {
            return I18n.format("string.naturescompass.damp");
        } else if (biome.rainfall >= 0.3f) {
            return I18n.format("string.naturescompass.normal");
        }
        return I18n.format("string.naturescompass.arid");
    }

    public static boolean biomeIsBlacklisted(BiomeGenBase biome) {
        final List<String> biomeBlacklist = ConfigHandler.getBiomeBlacklist();
        return biomeBlacklist.contains(String.valueOf(biome.biomeID)) || biomeBlacklist.contains(getBiomeName(biome))
                || biomeBlacklist.contains(biome.biomeName);
    }
}
