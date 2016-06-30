package com.smithsmodding.tinystorage.common.item;

import com.smithsmodding.smithscore.util.CoreReferences;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.common.modules.IModuleProvider;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.common.creativetab.TabTinyStorage;
import com.smithsmodding.tinystorage.common.modules.ModuleTinyStorageCore;
import com.smithsmodding.tinystorage.common.registry.GeneralRegistry;
import com.smithsmodding.tinystorage.common.registry.ModuleRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Tim on 23/06/2016.
 */
public class ItemModule extends Item implements IModuleProvider {

    public ItemModule() {
        this.setCreativeTab(TabTinyStorage.creativeTab);
        this.setUnlocalizedName(References.Items.ITEMMODULE);
        this.setRegistryName(References.MOD_ID.toLowerCase(), References.Items.ITEMMODULE);
        this.addPropertyOverride(CoreReferences.IItemProperties.MODELTYPE, new IItemPropertyGetter() {
            @Override
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                return stack.getTagCompound().getString(CoreReferences.NBT.IItemProperties.TARGET).equals(getModuleID(stack)) ? 1f : 0f;
            }
        });
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (IModule module : GeneralRegistry.instance().getModuleRegistry().getAllBuildableModules()) {
            if (module instanceof ModuleTinyStorageCore)
                continue;

            ItemStack stack = ModuleRegistry.getInstance().getStackForModule(module);
            if (stack == null) {
                continue;
            }
            if (!(stack.getItem() instanceof ItemModule)) {
                continue;
            }
            subItems.add(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(getModule(stack).getDisplayText());
    }

    @Override
    public IModule getModule(ItemStack stack) {
        return GeneralRegistry.instance().getModuleRegistry().getModule(getModuleID(stack));
    }

    @Override
    public String getModuleID(ItemStack stack) {
        if (stack.getItem() instanceof ItemModule) {
            if (stack.hasTagCompound()) {
                NBTTagCompound tag = stack.getTagCompound();
                if (tag.hasKey("moduleID")) {
                    return tag.getString("moduleID");
                }
            }
        }
        return "";
    }
}
