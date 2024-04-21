package com.chaosthedude.naturescompass.util;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.chaosthedude.naturescompass.NaturesCompass;
import com.chaosthedude.naturescompass.config.ConfigHandler;
import com.chaosthedude.naturescompass.items.ItemNaturesCompass;
import com.chaosthedude.naturescompass.network.PacketAvailableBiomesSet;

public class BiomeSearchWorker implements WorldWorkerManager.IWorker {

    public final int sampleSpace;
    public final int maxDistance;
    public World world;
    public BiomeGenBase biome;
    public int samples;
    public int nextLength;
    public EnumFacing direction;
    public ItemStack stack;
    public EntityPlayerMP player;
    public int x;
    public int z;
    public int length;
    public boolean finished;
    public int lastRadiusThreshold;
    public Set<BiomeGenBase> availableBiomes = new HashSet<>();

    public BiomeSearchWorker(World world, EntityPlayerMP player, ItemStack stack, int biomeID, int startX, int startZ) {
        this.world = world;
        this.player = player;
        this.stack = stack;
        this.biome = biomeID == -1 ? null : BiomeGenBase.getBiome(biomeID);
        x = startX;
        z = startZ;
        sampleSpace = ConfigHandler.sampleSpace;
        maxDistance = ConfigHandler.maxSearchDistance;
        nextLength = sampleSpace;
        length = 0;
        samples = 0;
        direction = EnumFacing.UP;
        finished = false;
        lastRadiusThreshold = 0;
    }

    public void start() {
        if (maxDistance > 0 && sampleSpace > 0) {
            NaturesCompass.logger
                    .info("Starting search: " + sampleSpace + " sample space, " + maxDistance + " max distance");
            WorldWorkerManager.addWorker(this);
        } else {
            finish(false);
        }
    }

    @Override
    public boolean hasWork() {
        return !finished && getRadius() <= maxDistance && samples <= ConfigHandler.maxSamples;
    }

    @Override
    public boolean doWork() {
        if (hasWork()) {
            if (direction == EnumFacing.NORTH) {
                z -= sampleSpace;
            } else if (direction == EnumFacing.EAST) {
                x += sampleSpace;
            } else if (direction == EnumFacing.SOUTH) {
                z += sampleSpace;
            } else if (direction == EnumFacing.WEST) {
                x -= sampleSpace;
            }

            final BiomeGenBase biomeAtPos = world.getBiomeGenForCoordsBody(x, z);
            availableBiomes.add(biomeAtPos);
            if (biomeAtPos == biome) {
                finish(true);
            }

            samples++;
            length += sampleSpace;
            if (length >= nextLength) {
                if (direction != EnumFacing.UP) {
                    nextLength += sampleSpace;
                    direction = getClockWise(direction);
                } else {
                    direction = EnumFacing.NORTH;
                }
                length = 0;
            }
            int radius = getRadius();
            if (radius > 500 && radius / 500 > lastRadiusThreshold) {
                if (stack != null && stack.getItem() == NaturesCompass.naturesCompass) {
                    ((ItemNaturesCompass) stack.getItem()).setSearchRadius(stack, roundRadius(radius, 500), player);
                }
                lastRadiusThreshold = radius / 500;
            }
        }
        if (hasWork()) {
            return true;
        }
        finish(false);
        return false;
    }

    private void finish(boolean found) {
        if (stack != null && stack.getItem() == NaturesCompass.naturesCompass) {
            if (found) {
                NaturesCompass.logger.info("Search succeeded: " + getRadius() + " radius, " + samples + " samples");
                ((ItemNaturesCompass) stack.getItem()).setFound(stack, x, z, player);
            } else {
                NaturesCompass.logger.info("Search failed: " + getRadius() + " radius, " + samples + " samples");
                ((ItemNaturesCompass) stack.getItem()).setNotFound(stack, player, roundRadius(getRadius(), 500));
            }
        } else {
            NaturesCompass.network.sendTo(new PacketAvailableBiomesSet(availableBiomes), player);
        }

        finished = true;
    }

    private int getRadius() {
        return BiomeUtils.getDistanceToBiome(player, x, z);
    }

    private int roundRadius(int radius, int roundTo) {
        return ((int) radius / roundTo) * roundTo;
    }

    public EnumFacing getClockWise(EnumFacing direction) {
        switch (direction) {
            case NORTH:
                return EnumFacing.EAST;
            case EAST:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.NORTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + direction);
        }
    }
}
