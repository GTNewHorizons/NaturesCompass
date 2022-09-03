package com.chaosthedude.naturescompass.network;

import com.chaosthedude.naturescompass.NaturesCompass;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.biome.BiomeGenBase;

public class PacketSync implements IMessage {

    private boolean canTeleport;
    private Set<BiomeGenBase> availableBiomes;
    private int oldDimensionId;
    private boolean completedSearch;

    public PacketSync() {}

    public PacketSync(
            boolean canTeleport, Set<BiomeGenBase> availableBiomes, int oldDimensionId, boolean completedSearch) {
        this.canTeleport = canTeleport;
        this.availableBiomes = availableBiomes;
        this.oldDimensionId = oldDimensionId;
        this.completedSearch = completedSearch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        canTeleport = buf.readBoolean();
        availableBiomes = new HashSet<BiomeGenBase>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            availableBiomes.add(BiomeGenBase.getBiome(buf.readInt()));
        }
        oldDimensionId = buf.readInt();
        completedSearch = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(canTeleport);
        buf.writeInt(availableBiomes.size());
        for (BiomeGenBase biome : availableBiomes) {
            buf.writeInt(biome.biomeID);
        }
        buf.writeInt(oldDimensionId);
        buf.writeBoolean(completedSearch);
    }

    public static class Handler implements IMessageHandler<PacketSync, IMessage> {
        @Override
        public IMessage onMessage(PacketSync packet, MessageContext ctx) {
            NaturesCompass.canTeleport = packet.canTeleport;
            BiomeSearchWorker.availableBiomes = packet.availableBiomes;
            BiomeSearchWorker.oldDimensionId = packet.oldDimensionId;
            BiomeSearchWorker.completedSearch = packet.completedSearch;

            return null;
        }
    }
}
