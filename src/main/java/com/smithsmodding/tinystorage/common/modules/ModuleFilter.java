package com.smithsmodding.tinystorage.common.modules;

import com.smithsmodding.smithscore.util.common.ItemStackHelper;
import com.smithsmodding.tinystorage.api.common.chest.IModularChest;
import com.smithsmodding.tinystorage.api.common.modules.ICustomFilterModule;
import com.smithsmodding.tinystorage.api.common.modules.IStorageModule;
import com.smithsmodding.tinystorage.api.reference.ModTranslationKeys;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.api.util.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by Tim on 24/06/2016.
 */
public class ModuleFilter implements ICustomFilterModule, IStorageModule {

    private String uniqueID;
    private int size;
    private ItemStack[] filterItems;
    private IModularChest hostChest;

    public ModuleFilter(String uniqueID, int size) {
        this.uniqueID = uniqueID;
        this.size = size;
        this.filterItems = new ItemStack[size];
    }

    @Override
    public int getSizeInventory() {
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return filterItems[index];
    }

    @Override
    public void clearInventory() {
        filterItems = new ItemStack[size];
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
        filterItems[slotIndex] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        hostChest.markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public boolean allowItemStack(int inventoryStackIndex, ItemStack stack) {
        for (ItemStack filter : filterItems) {
            if (ItemStackHelper.isMatchingItem(stack, filter, true, true)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRegisteringModId() {
        return References.MOD_ID;
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public String getDisplayText() {
        return I18n.format(ModTranslationKeys.Modules.FILTER, filterItems.length);
    }

    @Override
    public void onTileEntityUpdate(IModularChest tileEntityModularChest) {
    }

    @Override
    public void onInstalled(IModularChest tileEntityModularChest) {
        this.hostChest = tileEntityModularChest;
    }

    @Override
    public boolean canInstall(IModularChest tileEntityModularChest) {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        return NBTHelper.writeItemStacks(filterItems);
    }

    @Override
    public void loadFromNBT(NBTTagCompound tagCompound) {
        filterItems = NBTHelper.readItemStacks(tagCompound, filterItems.length);
    }
}
