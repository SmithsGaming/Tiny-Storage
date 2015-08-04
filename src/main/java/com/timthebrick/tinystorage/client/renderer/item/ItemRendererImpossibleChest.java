package com.timthebrick.tinystorage.client.renderer.item;

import com.timthebrick.tinystorage.common.reference.References;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRendererImpossibleChest implements IItemRenderer {

    private final ModelChest modelChest;
    private boolean isLocked;

    public ItemRendererImpossibleChest(boolean isLocked) {
        modelChest = new ModelChest();
        this.isLocked = isLocked;
    }

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType itemRenderType) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType itemRenderType, ItemStack itemStack, ItemRendererHelper itemRendererHelper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType itemRenderType, ItemStack itemStack, Object... data) {
        // Scaling
        float scale = 1F;
        // Translating
        float transX = 0F, transY = 0F, transZ = 0F;

        // Bind texture, scale
        /*
        if (itemStack.getItemDamage() == 0) {
            scale = 0.5F;
        } else if (itemStack.getItemDamage() == 1) {
            scale = 0.75F;
        } else if (itemStack.getItemDamage() == 2) {*/
        scale = 1F;
        // }

        if (!isLocked) {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID.toLowerCase() + ":textures/models/chests/unlocked/chest/impossibleChest.png"));
        } else {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID.toLowerCase() + ":textures/models/chests/locked/chest/impossibleChest.png"));
        }

        // Translate and render
        switch (itemRenderType) {
            case ENTITY: {
                renderImpossibleChest(0.5F + transX, 0.5F + transY, 0.5F + transZ, scale);
                break;
            }
            case EQUIPPED: {
                /*
                if (itemStack.getItemDamage() == 0) {
                    transX = 0.3F;
                    transY = -0.6F;
                    transZ = -0.9F;
                }else if (itemStack.getItemDamage() == 1) {
                    transX = 0.3F;
                    transY = -0.6F;
                    transZ = -0.9F;
                } else if (itemStack.getItemDamage() == 2) {*/
                transX = 0.3F;
                transY = -0.6F;
                transZ = -0.9F;
                //}
                renderImpossibleChest(transX, transY, transZ, scale);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                /*if (itemStack.getItemDamage() == 0) {
                    transX = -0.15F;
                    transY = -3F;
                    transZ = -2.5F;
                } else if (itemStack.getItemDamage() == 1) {
                    transX = -0.15F;
                    transY = -2.4F;
                    transZ = -2F;
                } else if (itemStack.getItemDamage() == 2) {*/
                transX = -0.15F;
                transY = -2.2F;
                transZ = -2F;
                //}
                renderImpossibleChest(1.0F + transX, 1.0F + transY, 1.0F + transZ, scale);
                break;
            }
            case INVENTORY: {
                /*
                if (itemStack.getItemDamage() == 0) {
                    transY = 1F;
                } else if (itemStack.getItemDamage() == 1) {
                    transY = 0.35F;
                }
                */
                renderImpossibleChest(0.0F + transX, -0.075F + transY, 0.0F + transZ, scale);
                break;
            }
            default:
                break;
        }
    }

    private void renderImpossibleChest(float x, float y, float z, float scale) {
        GL11.glPushMatrix(); // start
        GL11.glScalef(scale, -scale, -scale);
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(-90, 0, 1, 0);
        modelChest.renderAll();
        GL11.glPopMatrix(); // end
    }
}
