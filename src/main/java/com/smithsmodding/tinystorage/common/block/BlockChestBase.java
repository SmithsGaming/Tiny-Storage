package com.smithsmodding.tinystorage.common.block;

import com.smithsmodding.tinystorage.api.client.modules.IBlockStateDependingModelProvidingModule;
import com.smithsmodding.tinystorage.api.client.modules.IModelProvidingModule;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.reference.ModBlocks;
import com.smithsmodding.tinystorage.api.reference.ModItems;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.api.util.ItemStackHelper;
import com.smithsmodding.tinystorage.common.creativetab.TabTinyStorage;
import com.smithsmodding.tinystorage.common.registry.ModuleRegistry;
import com.smithsmodding.tinystorage.common.tileentity.TileEntityTinyStorage;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tim on 22/06/2016.
 */
public class BlockChestBase extends BlockContainer {

    private ExtendedBlockState extendedState;

    public BlockChestBase() {
        super(Material.WOOD);
        this.setUnlocalizedName(References.Blocks.BLOCKCHESTBASE);
        this.setHardness(2.5F);
        this.setCreativeTab(TabTinyStorage.creativeTab);
        this.setRegistryName(References.MOD_ID.toLowerCase(), References.Blocks.BLOCKCHESTBASE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ModBlocks.ListedProperties.FACING, EnumFacing.NORTH));

        this.extendedState = new ExtendedBlockState(this, new IProperty[]{ModBlocks.ListedProperties.FACING}, new IUnlistedProperty[]{ModBlocks.UnlistedProperties.INSTALLED_MODULES});
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTinyStorage();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        HashSet<IModule> coreModules = new HashSet<>();
        coreModules.add(ModuleRegistry.getInstance().getModule(References.Modules.ModuleNames.CORE));

        list.add(ItemStackHelper.getStackFromModuleSet(coreModules, itemIn, 1));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(ModBlocks.ListedProperties.FACING, placer.getHorizontalFacing());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     *
     * @param worldIn
     * @param pos
     * @param state
     * @param placer
     * @param stack
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity entity = worldIn.getTileEntity(pos);

        if (!(entity instanceof TileEntityTinyStorage))
            return;

        TileEntityTinyStorage chest = (TileEntityTinyStorage) entity;
        ItemStackHelper.getChestModulesFromStack(stack).forEach(chest::installModule);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        IExtendedBlockState extendedState = (IExtendedBlockState) getExtendedState(state, world, pos);
        return ItemStackHelper.getStackFromChestState(extendedState, ModItems.Blocks.blockChest, 1);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(ModBlocks.ListedProperties.FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ModBlocks.ListedProperties.FACING).getIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{ModBlocks.ListedProperties.FACING}, new IUnlistedProperty[]{ModBlocks.UnlistedProperties.INSTALLED_MODULES});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity entity = world.getTileEntity(pos);

        if (!(entity instanceof TileEntityTinyStorage))
            return state;

        TileEntityTinyStorage chestTe = (TileEntityTinyStorage) entity;
        HashSet<IModule> installedModules = new HashSet<>(chestTe.getInstalledModules().values());
        Iterator<IModule> moduleIterator = installedModules.iterator();

        state = ((IExtendedBlockState) extendedState.getBaseState().withProperty(ModBlocks.ListedProperties.FACING, state.getValue(ModBlocks.ListedProperties.FACING))).withProperty(ModBlocks.UnlistedProperties.INSTALLED_MODULES, installedModules);

        while (moduleIterator.hasNext()) {
            IModule module = moduleIterator.next();

            if (!(module instanceof IModelProvidingModule)) {
                moduleIterator.remove();
                continue;
            }

            if (module instanceof IBlockStateDependingModelProvidingModule) {
                IBlockStateDependingModelProvidingModule blockstateModule = (IBlockStateDependingModelProvidingModule) module;

                if (!blockstateModule.shouldRender(state, world, pos)) {
                    moduleIterator.remove();
                    continue;
                }

                state = blockstateModule.onGetExtendedState(state, world, pos);
            }
        }

        return state;
    }

    public void updateExtendedStateDefinition() {
        HashSet<IUnlistedProperty> requiredUnlistedProperties = new HashSet<>();
        requiredUnlistedProperties.add(ModBlocks.UnlistedProperties.INSTALLED_MODULES);

        for (IModule module : ModuleRegistry.getInstance().getAllBuildableModules()) {
            if (!(module instanceof IBlockStateDependingModelProvidingModule))
                continue;

            IBlockStateDependingModelProvidingModule blockstateModule = (IBlockStateDependingModelProvidingModule) module;

            requiredUnlistedProperties.addAll(blockstateModule.getRequiredProperties());
        }

        extendedState = new ExtendedBlockState(this, new IProperty[]{ModBlocks.ListedProperties.FACING}, requiredUnlistedProperties.toArray(new IUnlistedProperty[requiredUnlistedProperties.size()]));
    }
}
