package com.smithsmodding.tinystorage.common.registry;

import com.google.common.collect.ImmutableList;
import com.smithsmodding.smithscore.util.common.ItemStackHelper;
import com.smithsmodding.tinystorage.TinyStorage;
import com.smithsmodding.tinystorage.api.common.exception.ModuleConstructionException;
import com.smithsmodding.tinystorage.api.common.exception.ModuleRegistrationException;
import com.smithsmodding.tinystorage.api.common.exception.ModuleStackConstructionException;
import com.smithsmodding.tinystorage.api.common.factory.IModuleFactory;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.common.modules.IModuleProvider;
import com.smithsmodding.tinystorage.api.common.registries.IModuleRegistry;
import com.smithsmodding.tinystorage.common.modules.factory.ModuleFactoryCore;
import com.smithsmodding.tinystorage.common.modules.factory.ModuleFactoryFilter;
import com.smithsmodding.tinystorage.common.modules.factory.ModuleFactoryStorage;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Tim on 22/06/2016.
 */
public class ModuleRegistry implements IModuleRegistry {

    private static ModuleRegistry INSTANCE = new ModuleRegistry();
    LinkedHashMap<String, IModuleFactory> builders = new LinkedHashMap<>();
    LinkedHashMap<IModule, ItemStack> stacks = new LinkedHashMap<>();
    TCustomHashMap<ItemStack, IModule> reverseStackMap = new TCustomHashMap<>(new ItemStackHashingStrategy());
    ArrayList<IModule> baseModuleList = new ArrayList<>();
    private ModuleRegistry() {
        registerModuleFactory(new ModuleFactoryCore());
        registerModuleFactory(new ModuleFactoryStorage());
        registerModuleFactory(new ModuleFactoryFilter());
    }

    public static ModuleRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerModuleFactory(IModuleFactory factory) throws ModuleRegistrationException {
        for (String moduleID : factory.getBuildableModules()) {
            if (builders.containsKey(moduleID)) {
                throw new ModuleRegistrationException("Failed to register a new ModuleFactory for module: " + moduleID + " it is already Registered.", builders.get(moduleID), builders.get(moduleID).buildModule(moduleID), moduleID);
            }
            builders.put(moduleID, factory);
            try {
                IModule module = factory.buildModule(moduleID);
                if (!module.getUniqueID().equals(moduleID)) {
                    throw new ModuleRegistrationException("Pre-Build Module for ID: " + moduleID + " does not have the correct ID", factory, module, moduleID);
                }
                ItemStack stack = factory.buildItemStack(module);
                if (!(stack.getItem() instanceof IModuleProvider)) {
                    throw new ModuleRegistrationException("Item for Module: " + moduleID + " is not an instance of IModuleProvider", factory, module, moduleID);
                }
                stacks.put(module, stack);
                reverseStackMap.put(stacks.get(module).copy(), module);
                baseModuleList.add(module);
            } catch (ModuleConstructionException moduleUnknownException) {
                throw new ModuleRegistrationException("The given factory did not know a module it was supposed to be able to build: " + moduleID, moduleUnknownException, factory, null, moduleID);
            } catch (ModuleStackConstructionException moduleStackConstructionException) {
                throw new ModuleRegistrationException("The given factory could not successfully build a ItemStack for Module: " + moduleID, moduleStackConstructionException, factory, null, moduleID);
            } catch (Exception ex) {
                throw new ModuleRegistrationException("Failed to pre-build a Module for ID: " + moduleID, ex, factory, null, moduleID);
            }
        }
    }

    @Override
    public IModule getModule(String Id) {
        try {
            return builders.get(Id).buildModule(Id);
        } catch (Exception ex) {
            TinyStorage.getLogger().error(new Exception("Failed to build a Module for a given ID: " + Id, ex));
        }
        return null;
    }

    public ItemStack getStackForModule(IModule module) {
        if (!stacks.containsKey(module))
            return null;

        return stacks.get(module).copy();
    }

    public IModule getModuleFromStack(ItemStack stack) {
        if (!reverseStackMap.containsKey(stack))
            return null;

        return reverseStackMap.get(stack);
    }

    @Override
    public ImmutableList<IModule> getAllBuildableModules() {
        return ImmutableList.copyOf(baseModuleList);
    }

    private class ItemStackHashingStrategy implements HashingStrategy<ItemStack> {
        @Override
        public int computeHashCode(ItemStack object) {
            int hash = object.getItem().hashCode() ^ object.getMetadata();

            if (object.getTagCompound() != null)
                hash ^= object.getTagCompound().hashCode();

            return hash;
        }

        @Override
        public boolean equals(ItemStack o1, ItemStack o2) {
            return ItemStackHelper.equalsIgnoreStackSize(o1, o2);
        }
    }
}
