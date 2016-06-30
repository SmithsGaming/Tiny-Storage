package com.smithsmodding.tinystorage.common.tileentity;

import com.smithsmodding.smithscore.common.tileentity.TileEntitySmithsCore;
import com.smithsmodding.tinystorage.api.common.chest.IModularChest;
import com.smithsmodding.tinystorage.api.common.modules.ICustomFilterModule;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.common.modules.IStorageModule;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.common.modules.ModuleTinyStorageCore;
import com.smithsmodding.tinystorage.common.tileentity.guimanager.GuiManagerTinyStorage;
import com.smithsmodding.tinystorage.common.tileentity.state.TileEntityTinyStorageState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Tim on 22/06/2016.
 */
public class TileEntityTinyStorage extends TileEntitySmithsCore<TileEntityTinyStorageState, GuiManagerTinyStorage> implements IModularChest, ITickable {

    public TileEntityTinyStorage() {
        getState().setModuleLimit(References.Modules.Limits.SMALL_SIZE + 1);
        installModule(new ModuleTinyStorageCore());
    }

    @Override
    protected GuiManagerTinyStorage getInitialGuiManager() {
        return new GuiManagerTinyStorage();
    }

    @Override
    protected TileEntityTinyStorageState getInitialState() {
        return new TileEntityTinyStorageState();
    }

    @Override
    public LinkedHashMap<String, IModule> getInstalledModules() {
        return getState().getInstalledModules();
    }

    @Override
    public void installModule(IModule module) {
        if (!getState().getInstalledModules().containsKey(module.getUniqueID())
                && getModuleCount() + 1 <= getModuleLimit() && module.canInstall(this)) {
            getState().getInstalledModules().put(module.getUniqueID(), module);
            module.onInstalled(this);
        }
    }

    @Override
    public void removeModule(IModule module) {
        if (!getInstalledModules().containsKey(module.getUniqueID()))
            return;

        getInstalledModules().remove(module.getUniqueID());
    }

    @Override
    public int getModuleLimit() {
        return getState().getModuleLimit();
    }

    @Override
    public int getModuleCount() {
        return getState().getInstalledModules().size();
    }

    @Override
    public IModule getModuleOnPosition(int index) {
        return this.getInstalledModules().get(new ArrayList<>(this.getInstalledModules().keySet()).get(index + 1));
    }

    @Override
    public boolean containsModule(String uniqueID) {
        return getInstalledModules().containsKey(uniqueID);
    }

    @Override
    public boolean containsModuleType(Class<? extends IModule> module) {
        for (IModule installedModule : getState().getInstalledModules().values()) {
            if (installedModule.getClass().isAssignableFrom(module)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getSizeInventory() {
        int invSize = 0;
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                invSize += ((IStorageModule) module).getSizeInventory();
            }
        }
        return invSize;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                IStorageModule storageModule = (IStorageModule) module;
                if (index < storageModule.getSizeInventory()) {
                    return storageModule.getStackInSlot(index);
                }
                index -= storageModule.getSizeInventory();
            }
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                IStorageModule storageModule = (IStorageModule) module;
                if (index < storageModule.getSizeInventory()) {
                    return storageModule.decrStackSize(index, count);
                }
                index -= storageModule.getSizeInventory();
            }
        }
        return null;
    }

    @Override
    public void clearInventory() {
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                IStorageModule storageModule = (IStorageModule) module;
                storageModule.clearInventory();
            }
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                IStorageModule storageModule = (IStorageModule) module;
                if (index < storageModule.getSizeInventory()) {
                    storageModule.setInventorySlotContents(index, stack);
                }
                index -= storageModule.getSizeInventory();
            }
        }
    }

    @Override
    public int getInventoryStackLimit() {
        int sum = 0;
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                IStorageModule storageModule = (IStorageModule) module;
                sum += storageModule.getInventoryStackLimit();
            }
        }
        return sum;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (this.containsModuleType(ICustomFilterModule.class)) {
            boolean matchesFilter = false;
            for (IModule module : getState().getInstalledModules().values()) {
                if (module instanceof ICustomFilterModule) {
                    ICustomFilterModule filterModule = (ICustomFilterModule) module;
                    if (filterModule.allowItemStack(-1, stack)) {
                        matchesFilter = true;
                    }
                }
            }
            if (!matchesFilter) {
                return false;
            }
        }
        for (IModule module : getState().getInstalledModules().values()) {
            if (module instanceof IStorageModule) {
                IStorageModule storageModule = (IStorageModule) module;
                if (index < storageModule.getSizeInventory()) {
                    return storageModule.isItemValidForSlot(index, stack);
                }
                index -= storageModule.getSizeInventory();
            }
        }
        return false;
    }

    @Override
    public void update() {
        for (IModule module : getState().getInstalledModules().values()) {
            module.onTileEntityUpdate(this);
        }
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
    public String getContainerID() {
        return References.TE + "-" + getLocation().toString();
    }
}
