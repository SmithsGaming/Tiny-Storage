package com.smithsmodding.tinystorage.common.item.block;

import java.util.List;

import com.smithsmodding.tinystorage.common.reference.Messages;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockQuarryChest extends ItemBlock {

	public ItemBlockQuarryChest(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
		int metaData = itemStack.getItemDamage();
		if (metaData == 0) {
			//noinspection unchecked
			list.add(StatCollector.translateToLocal(Messages.ItemTooltips.BLOCK_SMALL));
		} else if (metaData == 1) {
			//noinspection unchecked
			list.add(StatCollector.translateToLocal(Messages.ItemTooltips.BLOCK_MEDIUM));
		} else if (metaData == 2) {
			//noinspection unchecked
			list.add(StatCollector.translateToLocal(Messages.ItemTooltips.BLOCK_LARGE));
		}
		//noinspection unchecked
		list.add(StatCollector.translateToLocal(Messages.ItemTooltips.QUARRY_CHEST_DESC_1));
		//noinspection unchecked
		list.add(StatCollector.translateToLocal(Messages.ItemTooltips.QUARRY_CHEST_DESC_2));
	}

}