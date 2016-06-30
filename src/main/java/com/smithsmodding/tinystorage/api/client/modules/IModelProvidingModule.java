package com.smithsmodding.tinystorage.api.client.modules;

import com.smithsmodding.tinystorage.api.common.modules.IModule;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author Orion (Created on: 26.06.2016)
 */
public interface IModelProvidingModule extends IModule {

    @SideOnly(Side.CLIENT)
    default ResourceLocation getModelLocation() {
        return new ResourceLocation(getRegisteringModId().toLowerCase(), "block/chest/" + getUniqueID());
    }
}
