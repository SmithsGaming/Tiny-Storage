package com.smithsmodding.tinystorage.api.reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Tim on 22/06/2016.
 */
@GameRegistry.ObjectHolder(References.MOD_ID)
public class ModItems {

    public static Item itemModule;

    public static class Blocks {
        public static ItemBlock blockChest;
    }
}
