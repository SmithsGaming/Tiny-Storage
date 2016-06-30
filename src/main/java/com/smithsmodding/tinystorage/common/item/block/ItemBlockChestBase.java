package com.smithsmodding.tinystorage.common.item.block;

import com.smithsmodding.tinystorage.api.reference.ModTranslationKeys;
import com.smithsmodding.tinystorage.api.util.ItemStackHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author Orion (Created on: 30.06.2016)
 */
public class ItemBlockChestBase extends ItemBlock {
    public ItemBlockChestBase(Block block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);

        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add("");
            tooltip.add(I18n.format(ModTranslationKeys.StackDetails.Blocks.INSTALLEDMODULES) + ": ");
            tooltip.addAll(ItemStackHelper.getChestModulesFromStack(stack).stream().map(module -> "  * " + module.getDisplayText()).collect(Collectors.toList()));
        } else {
            tooltip.add("");
            tooltip.add(I18n.format(ModTranslationKeys.StackDetails.Blocks.INSTALLEDMODULES) + ": " + ItemStackHelper.getChestModulesFromStack(stack).size());
            tooltip.add(I18n.format(ModTranslationKeys.StackDetails.PRESSSHIFHT));
        }
    }
}
