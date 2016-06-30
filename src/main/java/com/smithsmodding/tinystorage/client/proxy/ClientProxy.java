package com.smithsmodding.tinystorage.client.proxy;

import com.smithsmodding.smithscore.SmithsCore;
import com.smithsmodding.smithscore.client.model.loader.MultiComponentModelLoader;
import com.smithsmodding.smithscore.util.client.ResourceHelper;
import com.smithsmodding.tinystorage.TinyStorage;
import com.smithsmodding.tinystorage.api.reference.ModBlocks;
import com.smithsmodding.tinystorage.api.reference.ModItems;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.client.model.loader.ChestModelLoader;
import com.smithsmodding.tinystorage.client.model.loader.ModuleItemModelLoader;
import com.smithsmodding.tinystorage.common.block.BlockChestBase;
import com.smithsmodding.tinystorage.common.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Tim on 19/06/2016.
 */
public class ClientProxy extends CommonProxy {

    private static ModuleItemModelLoader moduleItemModelLoader = ModuleItemModelLoader.instance;
    private static ChestModelLoader chestModelLoader = ChestModelLoader.instance;

    public static void registerBlockModel(Block block) {
        Item blockItem = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }

    public static ResourceLocation registerModuleItemModel(Item item) {
        ResourceLocation itemLocation = ResourceHelper.getItemLocation(item);
        if (itemLocation == null) {
            return null;
        }
        String path = "module/" + itemLocation.getResourcePath() + "." + moduleItemModelLoader.EXTENSION;
        return registerModuleItemModel(item, new ResourceLocation(itemLocation.getResourceDomain(), path));
    }

    public static ResourceLocation registerModuleItemModel(Item item, final ResourceLocation location) {
        if (!location.getResourcePath().endsWith(moduleItemModelLoader.EXTENSION)) {
            SmithsCore.getLogger().error("The ModuleItem-model " + location.toString() + " does not end with '"
                    + MultiComponentModelLoader.EXTENSION
                    + "' and will therefore not be loaded by the custom model loader!");
        }
        return registerItemModelDefinition(item, location, moduleItemModelLoader.EXTENSION);
    }

    public static ResourceLocation registerItemModelDefinition(Item item, final ResourceLocation location, String requiredExtension) {
        if (!location.getResourcePath().endsWith(requiredExtension)) {
            SmithsCore.getLogger().error("The item-model " + location.toString() + " does not end with '"
                    + requiredExtension
                    + "' and will therefore not be loaded by the custom model loader!");
        }
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(location, "inventory");
            }
        });
        // We have to read the default variant if we have custom variants, since it wont be added otherwise and therefore not loaded
        ModelBakery.registerItemVariants(item, location);
        TinyStorage.getLogger().info("Added model definition for: " + item.getUnlocalizedName() + " add: " + location.getResourcePath() + " in the Domain: " + location.getResourceDomain());
        return location;
    }
    
    @Override
    public void initItemRendering() {
        moduleItemModelLoader.registerDomain(References.MOD_ID);

        ModelLoaderRegistry.registerLoader(moduleItemModelLoader);
        ModelLoaderRegistry.registerLoader(chestModelLoader);

        registerModuleItemModel(ModItems.itemModule);
    }

    @Override
    public void initIileRendering() {
        registerBlockModel(ModBlocks.blockChest);
    }

    @Override
    public void preInit() {
        TinyStorage.side = Side.CLIENT;
    }

    @Override
    public void init() {
        TinyStorage.side = Side.CLIENT;
    }

    @Override
    public void postInit() {
        TinyStorage.side = Side.CLIENT;
    }

    @Override
    public void onLoadComplete() {
        ((BlockChestBase) ModBlocks.blockChest).updateExtendedStateDefinition();
    }

    @Override
    public void registerEventHandlers() {
    }

    @Override
    public void registerKeyBindings() {
    }

    @Override
    public ClientProxy getClientProxy() {
        return this;
    }

}
