package com.timthebrick.tinystorage.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.timthebrick.tinystorage.common.reference.Key;

public interface IKeyBound {
	public abstract void doKeyBindingAction(EntityPlayer entityPlayer, ItemStack itemStack, Key key);
}
