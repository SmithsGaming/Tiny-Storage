package com.smithsmodding.tinystorage.api.client.modules;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;

/**
 * Author Orion (Created on: 29.06.2016)
 */
public interface IBlockStateDependingModelProvidingModule extends IModelProvidingModule {

    HashSet<IUnlistedProperty> getRequiredProperties();

    @SideOnly(Side.CLIENT)
    boolean shouldRender(IBlockState state, IBlockAccess world, BlockPos pos);

    @SideOnly(Side.CLIENT)
    IBlockState onGetExtendedState(IBlockState state, IBlockAccess world, BlockPos pos);
}
