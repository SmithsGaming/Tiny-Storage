package com.smithsmodding.tinystorage.common.modules;

import com.smithsmodding.tinystorage.api.common.chest.IModularChest;
import com.smithsmodding.tinystorage.api.common.modules.IStorageModule;
import com.smithsmodding.tinystorage.api.reference.ModTranslationKeys;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.api.util.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by Tim on 23/06/2016.
 */
public class ModuleStorage implements IStorageModule {

    private final String uniqueId;
    private final int size;
    private ItemStack[] inventory;
    private IModularChest hostChest;

    public ModuleStorage(String uniqueId, int size) {
        this.uniqueId = uniqueId;
        this.size = size;
        this.inventory = new ItemStack[size];
    }

    @Override
    public int getSizeInventory() {
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public void clearInventory() {
        inventory = new ItemStack[size];
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
        inventory[slotIndex] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
    public String getRegisteringModId() {
        return References.MOD_ID;
    }

    @Override
    public String getUniqueID() {
        return uniqueId;
    }

    @Override
    public String getDisplayText() {
        return I18n.format(ModTranslationKeys.Modules.STORAGE, inventory.length);
    }

    @Override
    public void onTileEntityUpdate(IModularChest tileEntityModularChest) {
    }

    @Override
    public void onInstalled(IModularChest hostChest) {
        this.hostChest = hostChest;
    }

    @Override
    public boolean canInstall(IModularChest tileEntityModularChest) {
        return !tileEntityModularChest.getInstalledModules().containsKey(getUniqueID());
    }

    @Override
    public NBTTagCompound writeToNBT() {
        return NBTHelper.writeItemStacks(inventory);
    }

    @Override
    public void loadFromNBT(NBTTagCompound tagCompound) {
        inventory = NBTHelper.readItemStacks(tagCompound, inventory.length);
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
}
