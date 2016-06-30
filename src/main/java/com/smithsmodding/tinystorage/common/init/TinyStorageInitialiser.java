package com.smithsmodding.tinystorage.common.init;

import com.smithsmodding.tinystorage.TinyStorage;
import com.smithsmodding.tinystorage.api.reference.ModBlocks;
import com.smithsmodding.tinystorage.api.reference.ModItems;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.common.block.BlockChestBase;
import com.smithsmodding.tinystorage.common.handler.GuiHandler;
import com.smithsmodding.tinystorage.common.item.ItemModule;
import com.smithsmodding.tinystorage.common.item.block.ItemBlockChestBase;
import com.smithsmodding.tinystorage.common.tileentity.TileEntityTinyStorage;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Tim on 22/06/2016.
 */
public class TinyStorageInitialiser {

    public static void serverStarting(FMLServerStartingEvent event) {
    }

    public static void serverStopping(FMLServerStoppingEvent event) {
    }

    public static void preInit(FMLPreInitializationEvent event) {
        TinyStorage.instance.developmentEnvironment = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        if (TinyStorage.instance.developmentEnvironment) {
            TinyStorage.getLogger().info("Development Environment detected; some features may not work the same as in a normal game");
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(TinyStorage.instance, new GuiHandler());
        TinyStorage.instance.proxy.registerEventHandlers();
        TinyStorage.instance.proxy.registerKeyBindings();
        initBlocks();
        initItems();
        initTileEntities();
        TinyStorage.instance.proxy.initItemRendering();
        TinyStorage.instance.proxy.initIileRendering();
    }

    public static void init(FMLInitializationEvent event) {
    }

    public static void postInit(FMLPostInitializationEvent event) {
    }

    public static void initItems() {
        ModItems.itemModule = GameRegistry.register(new ItemModule());
    }

    public static void initBlocks() {
        ModBlocks.blockChest = GameRegistry.register(new BlockChestBase());
        ModItems.Blocks.blockChest = GameRegistry.register(new ItemBlockChestBase(ModBlocks.blockChest));
    }

    public static void initTileEntities () {
        GameRegistry.registerTileEntity(TileEntityTinyStorage.class, References.TE);
    }
}
