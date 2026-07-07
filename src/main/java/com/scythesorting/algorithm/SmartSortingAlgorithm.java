package com.scythesorting.algorithm;

import com.scythesorting.sorting.ItemCategorizer;
import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * Smart sorting algorithm: groups items into logical categories, then sorts
 * alphabetically within each category. This is the default sorting mode.
 */
public class SmartSortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        Comparator<ItemStack> base = Comparator
                .comparingInt(stack -> ItemCategorizer.categorize(stack.getItem()).getPriority())
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }
}
