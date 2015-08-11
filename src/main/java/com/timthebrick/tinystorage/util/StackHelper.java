package com.timthebrick.tinystorage.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;

/**
 * @author TimtheBrick
 *         Taken and adjusted from the Buildcraft Source.
 */
public class StackHelper {

    protected StackHelper () {
    }

    /**
     * Checks if two ItemStacks are identical enough to be merged
     *
     * @param stack1 - The first stack
     * @param stack2 - The second stack
     * @return true if stacks can be merged, false otherwise
     */
    public static boolean canStacksMerge (ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {
            return false;
        }
        if (!stack1.isItemEqual(stack2)) {
            return false;
        }
        if (!ItemStack.areItemStackTagsEqual(stack1, stack2)) {
            return false;
        }
        return true;

    }

    /**
     * Merges mergeSource into mergeTarget
     *
     * @param mergeSource - The stack to merge into mergeTarget, this stack is not
     *                    modified
     * @param mergeTarget - The target merge, this stack is modified if doMerge is set
     * @param doMerge     - To actually do the merge
     * @return The number of items that was successfully merged.
     */
    public static int mergeStacks (ItemStack mergeSource, ItemStack mergeTarget, boolean doMerge) {
        if (!canStacksMerge(mergeSource, mergeTarget)) {
            return 0;
        }
        int mergeCount = Math.min(mergeTarget.getMaxStackSize() - mergeTarget.stackSize, mergeSource.stackSize);
        if (mergeCount < 1) {
            return 0;
        }
        if (doMerge) {
            mergeTarget.stackSize += mergeCount;
        }
        return mergeCount;
    }

	/* ITEM COMPARISONS */

    /**
     * Determines whether the given ItemStack should be considered equivalent
     * for crafting purposes.
     *
     * @param base          The stack to compare to.
     * @param comparison    The stack to compare.
     * @param oreDictionary true to take the Forge OreDictionary into account.
     * @return true if comparison should be considered a crafting equivalent for
     * base.
     */
    public static boolean isCraftingEquivalent (ItemStack base, ItemStack comparison, boolean oreDictionary) {
        if (isMatchingItem(base, comparison, true, false)) {
            return true;
        }

        if (oreDictionary) {
            int[] idBase = OreDictionary.getOreIDs(base);
            if (idBase.length > 0) {
                for (int id : idBase) {
                    for (ItemStack itemstack : OreDictionary.getOres(OreDictionary.getOreName(id))) {
                        if (comparison.getItem() == itemstack.getItem() && (itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE || comparison.getItemDamage() == itemstack.getItemDamage())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isCraftingEquivalent (int[] oreIDs, ItemStack comparison) {
        if (oreIDs.length > 0) {
            for (int id : oreIDs) {
                for (ItemStack itemstack : OreDictionary.getOres(OreDictionary.getOreName(id))) {
                    if (comparison.getItem() == itemstack.getItem() && (itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE || comparison.getItemDamage() == itemstack.getItemDamage())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Compares item id, damage and NBT. Accepts wildcard damage. Ignores damage
     * entirely if the item doesn't have subtypes.
     *
     * @param base       The stack to compare to.
     * @param comparison The stack to compare.
     * @return true if id, damage and NBT match.
     */
    public static boolean isMatchingItem (ItemStack base, ItemStack comparison) {
        return isMatchingItem(base, comparison, true, true);
    }

    /**
     * Compares item id, and optionally damage and NBT. Accepts wildcard damage.
     * Ignores damage entirely if the item doesn't have subtypes.
     *
     * @param a           ItemStack
     * @param b           ItemStack
     * @param matchDamage
     * @param matchNBT
     * @return true if matches
     */
    public static boolean isMatchingItem (final ItemStack a, final ItemStack b, final boolean matchDamage, final boolean matchNBT) {
        if (a == null || b == null) {
            return false;
        }

        if (a.getItem() != b.getItem()) {
            return false;
        }
        if (matchDamage && a.getHasSubtypes()) {
            if (!isWildcard(a) && !isWildcard(b)) {
                if (a.getItemDamage() != b.getItemDamage()) {
                    return false;
                }
            }
        }
        if (matchNBT) {
            if (a.stackTagCompound != null && !a.stackTagCompound.equals(b.stackTagCompound)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasOreDictEntry (final ItemStack a) {
        int[] oreIDs = OreDictionary.getOreIDs(a);
        if (oreIDs == null) {
            return false;
        }
        return true;
    }


    public static boolean isMatchingOreDict (final ItemStack a, final ItemStack b) {
        if (hasOreDictEntry(a) && hasOreDictEntry(b)) {
            int[] idA = OreDictionary.getOreIDs(a);
            int[] idB = OreDictionary.getOreIDs(b);
            return Arrays.equals(idA, idB);
        }
        return false;
    }

    public static boolean isWildcard (ItemStack stack) {
        return isWildcard(stack.getItemDamage());
    }

    public static boolean isWildcard (int damage) {
        return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
    }

}
