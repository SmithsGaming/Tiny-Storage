package com.smithsmodding.tinystorage.client.model.baked;

import com.google.common.collect.ImmutableList;
import com.smithsmodding.smithscore.client.model.baked.BakedWrappedModel;
import com.smithsmodding.smithscore.util.client.ModelHelper;
import com.smithsmodding.tinystorage.TinyStorage;
import com.smithsmodding.tinystorage.api.client.modules.IModelProvidingModule;
import com.smithsmodding.tinystorage.api.client.registries.IModuleModelRegistry;
import com.smithsmodding.tinystorage.api.common.modules.IModule;
import com.smithsmodding.tinystorage.api.reference.ModBlocks;
import com.smithsmodding.tinystorage.api.util.ItemStackHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Author Orion (Created on: 29.06.2016)
 */
public class BakedChestBlockModel extends BakedWrappedModel.PerspectiveAware {

    private final IModuleModelRegistry modelRegistry;
    private final BakedChestModelOverrides overrides = new BakedChestModelOverrides(this);

    public BakedChestBlockModel(IBakedModel parentModel, IModuleModelRegistry modelRegistry) {
        super(parentModel, ModelHelper.DEFAULT_BLOCK_TRANSFORMS);
        this.modelRegistry = modelRegistry;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        HashSet<IModule> installedModules = new HashSet<>();

        if (state != null) {
            try {
                installedModules = ((IExtendedBlockState) state).getValue(ModBlocks.UnlistedProperties.INSTALLED_MODULES);
            } catch (IllegalArgumentException argumentEx) {
                TinyStorage.getLogger().info(new Exception("Failed to retrieve installed Modules from Block with blockstate: " + state.toString() + "!", argumentEx));
            }
        }

        return ModelHelper.getQuadsForMergedModel(getSubModelsForInstalledModules(installedModules), state, side, rand);
    }

    private ImmutableList<IBakedModel> getSubModelsForInstalledModules(HashSet<IModule> installedModules) {
        ImmutableList.Builder<IBakedModel> builder = new ImmutableList.Builder<>();

        installedModules.stream().filter(module -> module instanceof IModelProvidingModule).forEach(module -> {
            IModelProvidingModule modelProvidingModule = (IModelProvidingModule) module;
            IBakedModel model = modelRegistry.getBakedModelForModule(modelProvidingModule);

            if (model != null)
                builder.add(model);
        });

        return builder.build();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    private static class BakedChestModelOverrides extends ItemOverrideList {

        private static final Random random = new Random();
        private final BakedChestBlockModel parent;

        public BakedChestModelOverrides(BakedChestBlockModel parent) {
            super(ImmutableList.of());
            this.parent = parent;
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            HashSet<IModule> installedModules = ItemStackHelper.getChestModulesFromStack(stack);

            HashMap<EnumFacing, ImmutableList<BakedQuad>> sideQuadMap = new HashMap<>();

            for (EnumFacing facing : EnumFacing.values()) {
                sideQuadMap.put(facing, ModelHelper.getQuadsForMergedModel(parent.getSubModelsForInstalledModules(installedModules), null, facing, random.nextLong()));
            }

            return new BakedChestItemModel(parent, sideQuadMap, ModelHelper.getQuadsForMergedModel(parent.getSubModelsForInstalledModules(installedModules), null, null, random.nextLong()));
        }
    }
}
