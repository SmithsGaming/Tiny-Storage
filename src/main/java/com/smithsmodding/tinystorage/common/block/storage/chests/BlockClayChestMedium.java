package com.smithsmodding.tinystorage.common.block.storage.chests;

import com.smithsmodding.tinystorage.common.tileentity.implementations.sub.TileEntityClayChestMedium;
import com.smithsmodding.tinystorage.common.reference.Names;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.smithsmodding.tinystorage.common.reference.RenderIDs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockClayChestMedium extends BlockClayChest{
	
	public BlockClayChestMedium(boolean isLockable) {
		super(isLockable);
		if (!this.isLockable) {
			this.setBlockName(Names.UnlocalisedBlocks.CLAY_CHEST_MEDIUM);
		} else {
			this.setBlockName(Names.UnlocalisedBlocks.CLAY_CHEST_MEDIUM_LOCKED);
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		return new TileEntityClayChestMedium();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		updateChestBounds(world.getBlockMetadata(x, y, z));
	}

	private void updateChestBounds(int meta) {
		setBlockBounds(0.125f, 0.0f, 0.125f, 1F - 0.125f, 0.72f, 1F - 0.125f);
	}

	@Override
	public int getRenderType() {
		return RenderIDs.clayChestMedium;
	}

}