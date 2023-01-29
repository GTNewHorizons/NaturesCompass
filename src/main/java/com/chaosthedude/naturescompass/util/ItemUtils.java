package com.chaosthedude.naturescompass.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.chaosthedude.naturescompass.NaturesCompass;

public class ItemUtils {

    public static boolean verifyNBT(ItemStack stack) {
        if (stack == null || stack.getItem() != NaturesCompass.naturesCompass) {
            return false;
        } else if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return true;
    }
}
