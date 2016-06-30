package com.smithsmodding.tinystorage.api.util;

import com.smithsmodding.tinystorage.TinyStorage;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.HashSet;

import static com.smithsmodding.tinystorage.api.reference.ModBlocks.UnlistedProperties.INSTALLED_MODULES;
import static com.smithsmodding.tinystorage.api.reference.References.NBT.Blocks.Chests.INSTALLEDMODULES;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

/**
 * Author Orion (Created on: 30.06.2016)
 */
public class ItemStackHelper {

    public static HashSet<IModule> getChestModulesFromStack(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound == null)
            return new HashSet<>();

        HashSet<IModule> modules = new HashSet<>();

        NBTTagList moduleList = compound.getTagList(INSTALLEDMODULES, TAG_COMPOUND);
        for (int i = 0; i < moduleList.tagCount(); i++) {
            try {
                NBTTagCompound moduleTag = moduleList.getCompoundTagAt(i);
                IModule module = NBTHelper.getModuleFromCompound(moduleTag);

                if (module != null)
                    modules.add(module);
            } catch (IllegalArgumentException ex) {
                TinyStorage.getLogger().info(new Exception("Failed to load module from stack:" + com.smithsmodding.smithscore.util.common.ItemStackHelper.toString(stack) + " on index: " + i));
            }
        }

        return modules;
    }

    public static ItemStack getStackFromChestState(IExtendedBlockState blockState, ItemBlock blockItem, int count) {
        try {
            HashSet<IModule> modules = blockState.getValue(INSTALLED_MODULES);

            return getStackFromModuleSet(modules, blockItem, count);
        } catch (IllegalArgumentException ex) {
            TinyStorage.getLogger().info(new Exception("Failed to retrieve Modules installed on Block as Blockstate does not carry the modules property!", ex));
        }

        return null;
    }

    public static ItemStack getStackFromModuleSet(HashSet<IModule> modules, Item item, int count) {
        NBTTagList modulesList = new NBTTagList();

        for (IModule module : modules) {
            modulesList.appendTag(NBTHelper.getCompoundFromModule(module));
        }

        NBTTagCompound stackCompound = new NBTTagCompound();
        stackCompound.setTag(INSTALLEDMODULES, modulesList);

        ItemStack stack = new ItemStack(item, count);
        stack.setTagCompound(stackCompound);

        return stack;
    }
}
