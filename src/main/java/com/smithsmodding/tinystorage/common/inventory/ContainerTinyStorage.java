package com.smithsmodding.tinystorage.common.inventory;

import com.smithsmodding.smithscore.common.inventory.ContainerSmithsCore;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.common.tileentity.TileEntityTinyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * Created by Tim on 26/06/2016.
 */
public class ContainerTinyStorage extends ContainerSmithsCore {

    private TileEntityTinyStorage tileEntity;

    public ContainerTinyStorage(TileEntityTinyStorage tileEntity, EntityPlayer playerMP) {
        super(References.TE, tileEntity, tileEntity, playerMP);
        this.tileEntity = tileEntity;
        generatePlayerInventory();
        generateStandardInventory();
    }

    private void generatePlayerInventory() {
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
                this.addSlotToContainer(new Slot(getPlayerInventory(), inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 112 + inventoryRowIndex * 18));
            }
        }
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
            this.addSlotToContainer(new Slot(getPlayerInventory(), actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 171));
        }
    }

    private void generateStandardInventory() {

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onTabChanged(String newActiveTabID) {
        super.onTabChanged(newActiveTabID);
        inventorySlots.clear();
        inventoryItemStacks.clear();
    }
}