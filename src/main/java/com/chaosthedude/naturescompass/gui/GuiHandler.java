package com.chaosthedude.naturescompass.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.chaosthedude.naturescompass.NaturesCompass;
import com.chaosthedude.naturescompass.items.ItemNaturesCompass;
import com.chaosthedude.naturescompass.util.BiomeUtils;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        final ItemStack stack = player.getHeldItem();
        if (stack != null && stack.getItem() == NaturesCompass.naturesCompass) {
            return new GuiNaturesCompass(
                    world,
                    player,
                    stack,
                    (ItemNaturesCompass) stack.getItem(),
                    BiomeUtils.getAllowedBiomes());
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
