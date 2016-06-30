package com.smithsmodding.tinystorage.api.common.properties;

import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.reference.References;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashSet;

/**
 * Author Orion (Created on: 29.06.2016)
 */
public class PropertyInstalledModules implements IUnlistedProperty<HashSet<IModule>> {
    @Override
    public String getName() {
        return References.Blocks.Properties.MODULES;
    }

    @Override
    public boolean isValid(HashSet<IModule> value) {
        return true;
    }

    @Override
    public Class<HashSet<IModule>> getType() {
        HashSet<IModule> typeSet = new HashSet<>();
        return (Class<HashSet<IModule>>) typeSet.getClass();
    }

    @Override
    public String valueToString(HashSet<IModule> value) {
        String string = "";

        for (IModule module : value) {
            string += module.getUniqueID() + "-";
        }

        return string;
    }
}
