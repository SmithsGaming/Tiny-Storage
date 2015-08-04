package com.timthebrick.tinystorage.common.block;

import com.timthebrick.tinystorage.TinyStorage;
import com.timthebrick.tinystorage.common.core.TinyStorageLog;
import com.timthebrick.tinystorage.common.creativetab.TabTinyStorage;
import com.timthebrick.tinystorage.common.reference.GUIs;
import com.timthebrick.tinystorage.common.reference.Names;
import com.timthebrick.tinystorage.common.reference.References;
import com.timthebrick.tinystorage.common.reference.RenderIDs;
import com.timthebrick.tinystorage.common.tileentity.TileEntityTinyStorage;
import com.timthebrick.tinystorage.common.tileentity.implementations.TileEntityBookCase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockBookCase extends BlockContainer implements ITileEntityProvider {

    private String textureName;

    public BlockBookCase (Material mat, String textureName) {
        super(mat);
        this.setHardness(2.5f);
        this.setBlockName(Names.UnlocalisedBlocks.BOOKCASE + textureName);
        this.setCreativeTab(TabTinyStorage.creativeTab);
        this.textureName = textureName;
    }

    @Override
    public TileEntity createNewTileEntity (World world, int metaData) {
        return new TileEntityBookCase();
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public int getRenderType () {
        return RenderIDs.bookCase;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if ((player.isSneaking() && player.getCurrentEquippedItem() != null) || world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)) {
            return true;
        } else {
            if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityBookCase) {
                TinyStorageLog.info(((TileEntityBookCase) world.getTileEntity(x, y, z)).getOrientation());
                player.openGui(TinyStorage.instance, GUIs.BOOKCASE.ordinal(), world, x, y, z);
            }
            return true;
        }
    }

    @Override
    public boolean onBlockEventReceived (World world, int x, int y, int z, int eventId, int eventData) {
        super.onBlockEventReceived(world, x, y, z, eventId, eventData);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null && tileentity.receiveClientEvent(eventId, eventData);
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int meta) {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityTinyStorage) {
            int direction = 0;
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0) {
                direction = ForgeDirection.NORTH.ordinal();
            } else if (facing == 1) {
                direction = ForgeDirection.EAST.ordinal();
            } else if (facing == 2) {
                direction = ForgeDirection.SOUTH.ordinal();
            } else if (facing == 3) {
                direction = ForgeDirection.WEST.ordinal();
            }

            if (itemStack.hasDisplayName()) {
                ((TileEntityTinyStorage) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
            }

            ((TileEntityTinyStorage) world.getTileEntity(x, y, z)).setOrientation(direction);
        }
    }

    protected void dropInventory (World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory)) {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.stackSize > 0) {
                Random rand = new Random();

                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());

                if (itemStack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool (World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        updateBlockBounds(world, x, y, z);
    }

    public void updateBlockBounds (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityTinyStorage) {
            TileEntityTinyStorage tileEntity = (TileEntityTinyStorage) te;
            if(tileEntity.getOrientation() == ForgeDirection.NORTH){
                setBlockBounds(0.0f, 0.0f, 0.65f, 1f, 1f, 1f);
                return;
            }else if(tileEntity.getOrientation() == ForgeDirection.EAST){
                setBlockBounds(0.0f, 0.0f, 0.0f, 0.35f, 1f, 1f);
                return;
            }else if(tileEntity.getOrientation() == ForgeDirection.SOUTH){
                setBlockBounds(0.0f, 0.0f, 0.0f, 1f, 1f, 0.35f);
                return;
            }else if(tileEntity.getOrientation() == ForgeDirection.WEST){
                setBlockBounds(0.65f, 0.0f, 0.0f, 1f, 1f, 1f);
                return;
            }else{
               setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                return;
            }
        }

    }

    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(References.MOD_ID.toLowerCase() + ":" + textureName);
    }

    @Override
    public String getTextureName () {
        return textureName;
    }
}
