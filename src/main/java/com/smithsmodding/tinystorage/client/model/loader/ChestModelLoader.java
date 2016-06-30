package com.smithsmodding.tinystorage.client.model.loader;

import com.smithsmodding.smithscore.client.model.unbaked.DummyModel;
import com.smithsmodding.tinystorage.api.client.modules.IModelProvidingModule;
import com.smithsmodding.tinystorage.api.client.registries.IModuleModelRegistry;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.client.model.unbaked.ChestBlockModel;
import com.smithsmodding.tinystorage.client.registries.HashedModuleModelRegistry;
import com.smithsmodding.tinystorage.common.registry.ModuleRegistry;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

/**
 * Author Orion (Created on: 30.06.2016)
 */
public class ChestModelLoader implements ICustomModelLoader {
    public static final String EXTENSION = "CM-TinyStorage";

    public static final ChestModelLoader instance = new ChestModelLoader();

    private ChestModelLoader() {
        super();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourcePath().endsWith(EXTENSION);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        if (!Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION)) {
            return DummyModel.INSTANCE;
        }

        IModuleModelRegistry registry = new HashedModuleModelRegistry();

        for (IModule module : ModuleRegistry.getInstance().getAllBuildableModules()) {
            if (!(module instanceof IModelProvidingModule))
                continue;

            IModelProvidingModule modelProvidingModule = (IModelProvidingModule) module;
            IModel moduleModel = ModelLoaderRegistry.getModelOrLogError(modelProvidingModule.getModelLocation(), "Failed to load Module-Model for module: " + module.getRegisteringModId());

            registry.registerModuleModel(modelProvidingModule, moduleModel);
        }

        return new ChestBlockModel(registry);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
