package com.smithsmodding.tinystorage.api.util;

import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.common.registry.ModuleRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import static com.smithsmodding.tinystorage.api.reference.References.NBT.Blocks.Chests.MODULEDATA;
import static com.smithsmodding.tinystorage.api.reference.References.NBT.Blocks.Chests.MODULEID;
import static com.smithsmodding.tinystorage.api.reference.References.NBT.Inventory.INVENTORY;
import static com.smithsmodding.tinystorage.api.reference.References.NBT.Inventory.SLOT;

/**
 * Author Orion (Created on: 30.06.2016)
 */
public class NBTHelper {
    public static IModule getModuleFromCompound(NBTTagCompound compound) throws IllegalArgumentException {
        if (compound == null || !compound.hasKey(MODULEID))
            throw new IllegalArgumentException("Given Compound has no module ID or is empty!");

        String moduleId = compound.getString(MODULEID);
        IModule module = ModuleRegistry.getInstance().getModule(moduleId);

        if (module == null)
            throw new IllegalArgumentException("Given Compound does not produce a known Module!");

        if (compound.hasKey(MODULEDATA))
            module.loadFromNBT(compound.getCompoundTag(MODULEDATA));

        return module;
    }

    public static NBTTagCompound getCompoundFromModule(IModule module) {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setString(MODULEID, module.getUniqueID());
        compound.setTag(MODULEDATA, module.writeToNBT());

        return compound;
    }

    public static NBTTagCompound writeItemStacks(ItemStack[] stacks) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < stacks.length; i++) {
            ItemStack stack = stacks[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte(SLOT, (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag(INVENTORY, itemList);
        return tagCompound;
    }

    public static ItemStack[] readItemStacks(NBTTagCompound compound, int size) {
        ItemStack[] stacks = new ItemStack[size];

        NBTTagList tagList = compound.getTagList(INVENTORY, 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte(SLOT);
            if (slot >= 0 && slot < stacks.length) {
                stacks[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        return stacks;
    }
}
