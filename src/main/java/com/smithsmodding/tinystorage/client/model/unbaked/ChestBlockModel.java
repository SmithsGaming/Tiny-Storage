package com.smithsmodding.tinystorage.client.model.unbaked;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.smithsmodding.smithscore.util.client.ModelHelper;
import com.smithsmodding.tinystorage.api.client.registries.IModuleModelRegistry;
import com.smithsmodding.tinystorage.api.reference.References;
import com.smithsmodding.tinystorage.client.model.baked.BakedChestBlockModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;

/**
 * Author Orion (Created on: 29.06.2016)
 */
public class ChestBlockModel implements IModel {

    private final IModuleModelRegistry modelRegistry;

    public ChestBlockModel(IModuleModelRegistry modelRegistry) {
        this.modelRegistry = modelRegistry;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return modelRegistry.getTextures();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        modelRegistry.bakeAll(state, format, bakedTextureGetter);
        return new BakedChestBlockModel(modelRegistry.getBakedModelForModule(References.Modules.ModuleNames.CORE), modelRegistry);
    }

    @Override
    public IModelState getDefaultState() {
        return ModelHelper.DEFAULT_BLOCK_STATE;
    }
}
