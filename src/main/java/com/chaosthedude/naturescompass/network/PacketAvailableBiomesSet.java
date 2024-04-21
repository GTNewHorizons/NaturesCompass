package com.chaosthedude.naturescompass.network;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.BiomeGenBase;

import com.chaosthedude.naturescompass.gui.GuiNaturesCompass;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketAvailableBiomesSet implements IMessage {

    private Set<BiomeGenBase> biomes;

    public PacketAvailableBiomesSet() {}

    public PacketAvailableBiomesSet(Set<BiomeGenBase> biomes) {
        this.biomes = biomes;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        biomes = new HashSet<>();

        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            biomes.add(BiomeGenBase.getBiome(buf.readInt()));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(biomes.size());

        for (BiomeGenBase biome : biomes) {
            buf.writeInt(biome.biomeID);
        }
    }

    public static class Handler implements IMessageHandler<PacketAvailableBiomesSet, IMessage> {

        @Override
        public IMessage onMessage(PacketAvailableBiomesSet message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiNaturesCompass) {
                ((GuiNaturesCompass) Minecraft.getMinecraft().currentScreen).availableBiomes = message.biomes;
            }
            return null;
        }
    }
}
