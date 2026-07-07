package com.scythesorting.util;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * Utility methods for working with {@link ItemStack}s in the context of sorting.
 */
public final class ItemStackUtil {

    private ItemStackUtil() {}

    /**
     * Returns the display name of the item stack as a plain string, suitable for
     * alphabetical comparisons. Uses the item's default name if no custom name is set.
     *
     * @param stack the item stack
     * @return the plain-text display name
     */
    public static String getDisplayName(ItemStack stack) {
        if (stack.isEmpty()) return "";
        Text name = stack.getName();
        return name.getString().toLowerCase();
    }

    /**
     * Determines whether two item stacks are truly identical and can be merged.
     * Two stacks are considered identical if they have the same item type AND
     * the same NBT/component data (enchantments, durability, custom name, potion data, etc.).
     *
     * @param a the first stack
     * @param b the second stack
     * @return {@code true} if the stacks can be safely merged
     */
    public static boolean canMerge(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        if (a.getItem() != b.getItem()) return false;
        // ItemStack.canCombine checks item equality and NBT/component equality
        return ItemStack.canCombine(a, b);
    }

    /**
     * Returns the maximum number of items that can be added to stack {@code target}
     * from stack {@code source}, respecting the max stack size.
     *
     * @param target the stack that will receive items
     * @param source the stack providing items
     * @return the number of items that can be transferred (0 if none)
     */
    public static int getTransferableCount(ItemStack target, ItemStack source) {
        if (!canMerge(target, source)) return 0;
        int space = target.getMaxCount() - target.getCount();
        return Math.min(space, source.getCount());
    }
}
