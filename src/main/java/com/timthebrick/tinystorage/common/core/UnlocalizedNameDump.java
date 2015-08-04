package com.timthebrick.tinystorage.common.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.timthebrick.tinystorage.common.block.BlockClayChest;
import com.timthebrick.tinystorage.common.block.BlockWoolChest;
import com.timthebrick.tinystorage.common.init.ModBlocks;
import com.timthebrick.tinystorage.common.init.ModItems;

public class UnlocalizedNameDump {

	public static void dumpBlockNames(File file) {
		TinyStorageLog.info("Attempting to dump unlocalized block names");
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			for (Block b : ModBlocks.tinyStorageBlocks) {
				if (b instanceof BlockWoolChest) {
					String[] textureNames = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Silver", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
					for (int i = 0; i < 16; i++) {
						writer.println(b.getUnlocalizedName() + textureNames[i] + ".name=");
					}
				} else if (b instanceof BlockClayChest) {
					String[] textureNames = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Silver", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
					for (int i = 0; i < 16; i++) {
						writer.println(b.getUnlocalizedName() + textureNames[i] + ".name=");
					}
				}else {
					writer.println(b.getUnlocalizedName() + ".name=");
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			TinyStorageLog.error(e);
		} catch (UnsupportedEncodingException e) {
			TinyStorageLog.error(e);
		}
	}

	public static void dumpItemNames(File file) {
		TinyStorageLog.info("Attempting to dump unlocalized item names");
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			for (Item i : ModItems.tinyStorageItems) {
				writer.println(i.getUnlocalizedName() + ".name=");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			TinyStorageLog.error(e);
		} catch (UnsupportedEncodingException e) {
			TinyStorageLog.error(e);
		}
	}

}
