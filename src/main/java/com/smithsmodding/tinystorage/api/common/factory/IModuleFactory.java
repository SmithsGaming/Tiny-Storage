package com.smithsmodding.tinystorage.api.common.factory;

import com.google.common.collect.ImmutableList;
import com.smithsmodding.tinystorage.api.common.exception.ModuleConstructionException;
import com.smithsmodding.tinystorage.api.common.exception.ModuleStackContructionException;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.common.modules.ModuleStorage;
import net.minecraft.item.ItemStack;

/**
 * Author Orion (Created on: 22.06.2016)
 */
public interface IModuleFactory {

    /**
     * Method to get all ID's of the Modules that this Factory can build.
     *
     * @return A list containing all the ID's of the Modules that this Factory can build.
     */
    ImmutableList<String> getBuildableModules();

    /**
     * Method used to construct a new Instance of a Module with the given ID.
     *
     * @param uniqueId The ID of the Module that this Factory should construct.
     * @return A IModule instance for the given ID.
     * @throws ModuleConstructionException Thrown when the given ID is not registered to this Factory.
     */
    IModule buildModule(String uniqueId) throws ModuleConstructionException;

    /**
     * Method to get the ItemStack representation of a given module
     *
     * @param module The module to create an ItemStack for
     * @return The ItemStack representation of the module
     * @throws ModuleStackContructionException Thrown when the given module is not registered to this Factory
     */
    ItemStack buildItemStack(IModule module) throws ModuleStackContructionException;
}
