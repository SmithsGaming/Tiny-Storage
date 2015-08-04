package com.timthebrick.tinystorage.common.block;

import com.timthebrick.tinystorage.common.reference.Names;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.timthebrick.tinystorage.common.reference.RenderIDs;
import com.timthebrick.tinystorage.common.tileentity.implementations.sub.TileEntityWoolChestMedium;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWoolChestMedium extends BlockWoolChest {
	
	public BlockWoolChestMedium(boolean isLockable) {
		super(Material.cloth, isLockable);
		if (!this.isLockable) {
			this.setBlockName(Names.UnlocalisedBlocks.WOOL_CHEST_MEDIUM);
		} else {
			this.setBlockName(Names.UnlocalisedBlocks.WOOL_CHEST_MEDIUM_LOCKED);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		return new TileEntityWoolChestMedium();
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

	public void updateChestBounds(int meta) {
		setBlockBounds(0.125f, 0.0f, 0.125f, 1F - 0.125f, 0.72f, 1F - 0.125f);
	}

	@Override
	public int getRenderType() {
		return RenderIDs.woolChestMedium;
	}
}
