package com.smithsmodding.tinystorage.client.model.baked;

import com.google.common.collect.ImmutableList;
import com.smithsmodding.smithscore.client.model.baked.BakedWrappedModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.List;

/**
 * Author Orion (Created on: 30.06.2016)
 */
public class BakedChestItemModel extends BakedWrappedModel {

    public final HashMap<EnumFacing, ImmutableList<BakedQuad>> quads;
    public final ImmutableList<BakedQuad> generalQuads;

    public BakedChestItemModel(IBakedModel parentModel, HashMap<EnumFacing, ImmutableList<BakedQuad>> quads, ImmutableList<BakedQuad> generalQuads) {
        super(parentModel);
        this.quads = quads;
        this.generalQuads = generalQuads;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if (side == null)
            return generalQuads;

        return quads.get(side);
    }
}
