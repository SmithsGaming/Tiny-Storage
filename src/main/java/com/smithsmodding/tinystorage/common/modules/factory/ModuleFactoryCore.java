package com.smithsmodding.tinystorage.common.modules.factory;

import com.google.common.collect.ImmutableList;
import com.smithsmodding.tinystorage.api.common.exception.ModuleConstructionException;
import com.smithsmodding.tinystorage.api.common.factory.IModuleFactory;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.common.modules.ModuleTinyStorageCore;

/**
 * Author Orion (Created on: 30.06.2016)
 */
public class ModuleFactoryCore implements IModuleFactory {
    @Override
    public ImmutableList<String> getBuildableModules() {
        return ImmutableList.of(References.Modules.ModuleNames.CORE);
    }

    @Override
    public IModule buildModule(String uniqueId) throws ModuleConstructionException {
        switch (uniqueId) {
            case References.Modules.ModuleNames.CORE:
                return new ModuleTinyStorageCore();
            default:
                throw new ModuleConstructionException("The given module is unknown to this Factory.");
        }
    }
}
