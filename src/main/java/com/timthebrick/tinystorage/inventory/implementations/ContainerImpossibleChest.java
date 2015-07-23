package com.timthebrick.tinystorage.inventory.implementations;

import com.timthebrick.tinystorage.core.TinyStorageLog;
import com.timthebrick.tinystorage.inventory.ContainerTinyStorage;
import com.timthebrick.tinystorage.inventory.slot.SlotTinyStorage;
import com.timthebrick.tinystorage.tileentity.implementations.TileEntityImpossibleChest;
import com.timthebrick.tinystorage.tileentity.implementations.TileEntityTinyChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerImpossibleChest extends ContainerTinyStorage {

    public static final int INVENTORY_ROWS = 29;
    public static final int INVENTORY_COLUMNS = 9;
    public static final int INVENTORY_SIZE = INVENTORY_ROWS * INVENTORY_COLUMNS;
    public static final int DISPLAYABLE_ROW_SIZE = 6;

    private TileEntityImpossibleChest tileEntity;
    private InventoryPlayer inventory;
    private int chestInventoryRows;
    private int chestInventoryColumns;

    public ContainerImpossibleChest(InventoryPlayer inventoryPlayer, TileEntityImpossibleChest tileEntity) {
        this.tileEntity = tileEntity;
        tileEntity.openInventory();
        chestInventoryRows = INVENTORY_ROWS;
        chestInventoryColumns = INVENTORY_COLUMNS;

        // Add chest slots to inventory
        for (int chestRowIndex = 0; chestRowIndex < DISPLAYABLE_ROW_SIZE; chestRowIndex++) {
            for (int chestColumnIndex = 0; chestColumnIndex < chestInventoryColumns; chestColumnIndex++) {
                this.addSlotToContainer(new SlotTinyStorage(tileEntity, chestColumnIndex + chestRowIndex * chestInventoryColumns, 8 + chestColumnIndex * 18, 18 + chestRowIndex * 18));
            }
        }

        // Add player inventory to inventory
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
                this.addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 140 + inventoryRowIndex * 18));
            }
        }

        // Add player hotbar to inventory
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
            this.addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 198));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        tileEntity.closeInventory();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
        ItemStack newItemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            newItemStack = itemStack.copy();
            if (slotIndex < chestInventoryRows * chestInventoryColumns) {
                if (!this.mergeItemStack(itemStack, chestInventoryRows * chestInventoryColumns, inventorySlots.size(), false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack, 0, chestInventoryRows * chestInventoryColumns, false)) {
                return null;
            }
            if (itemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return newItemStack;
    }

    @Override
    public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
        TinyStorageLog.info(slotNum);
        return super.slotClick(slotNum, mouseButton, modifier, player);
    }

    public void doStuffTest(int scrollPos) {
        int startRow = scrollPos / 4;
        this.inventoryItemStacks.clear();
        this.inventorySlots.clear();
        // Add chest slots to inventory
        for (int chestRowIndex = 0; chestRowIndex < DISPLAYABLE_ROW_SIZE; chestRowIndex++) {
            for (int chestColumnIndex = 0; chestColumnIndex < chestInventoryColumns; chestColumnIndex++) {
                this.addSlotToContainer(new SlotTinyStorage(tileEntity, (startRow * INVENTORY_COLUMNS) + chestColumnIndex + chestRowIndex * chestInventoryColumns, 8 + chestColumnIndex * 18, 18 + chestRowIndex * 18));
            }
        }
        // Add player inventory to inventory
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
                this.addSlotToContainer(new Slot(inventory, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 140 + inventoryRowIndex * 18));
            }
        }
        // Add player hotbar to inventory
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
            this.addSlotToContainer(new Slot(inventory, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 198));
        }
    }
}
