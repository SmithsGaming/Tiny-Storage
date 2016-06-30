package com.smithsmodding.tinystorage.api.reference;

import com.smithsmodding.tinystorage.api.common.properties.PropertyInstalledModules;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Tim on 22/06/2016.
 */
@GameRegistry.ObjectHolder(References.MOD_ID)
public class ModBlocks {

    public static Block blockChest;

    public static class UnlistedProperties {

        public static final PropertyInstalledModules INSTALLED_MODULES = new PropertyInstalledModules();
    }

    public static class ListedProperties {

        public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
}
