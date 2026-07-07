package com.scythesorting.algorithm;

import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * Blocks First sorting algorithm: places all block items before non-block items,
 * then sorts alphabetically within each group.
 */
public class BlocksFirstSortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        Comparator<ItemStack> base = Comparator
                .comparingInt((ItemStack stack) -> (stack.getItem() instanceof BlockItem) ? 0 : 1)
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }
}
