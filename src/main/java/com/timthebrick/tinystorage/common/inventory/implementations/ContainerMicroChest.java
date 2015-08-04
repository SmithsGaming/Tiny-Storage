package com.timthebrick.tinystorage.common.inventory.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.timthebrick.tinystorage.common.inventory.ContainerTinyStorage;
import com.timthebrick.tinystorage.common.inventory.slot.SlotTinyStorage;
import com.timthebrick.tinystorage.common.tileentity.implementations.TileEntityMicroChest;

public class ContainerMicroChest extends ContainerTinyStorage {

	public static final int CHEST_INVENTORY_ROWS = 1;
	public static final int CHEST_INVENTORY_COLUMNS = 9;
	public static final int INVENTORY_SIZE = CHEST_INVENTORY_ROWS * CHEST_INVENTORY_COLUMNS;

	private TileEntityMicroChest tileEntity;
	private InventoryPlayer inventory;
	private int chestInventoryRows;
	private int chestInventoryColumns;

	public ContainerMicroChest(InventoryPlayer inventoryPlayer, TileEntityMicroChest tileEntity) {
		this.tileEntity = tileEntity;
		tileEntity.openInventory();

		chestInventoryRows = CHEST_INVENTORY_ROWS;
		chestInventoryColumns = CHEST_INVENTORY_COLUMNS;

		// Add chest slots to inventory
		this.addSlotToContainer(new SlotTinyStorage(tileEntity, 0, 80, 38));

		// Add player inventory to inventory
		for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
			for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
				this.addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 87 + inventoryRowIndex * 18));
			}
		}

		// Add player hotbar to inventory
		for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
			this.addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 145));
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

}
