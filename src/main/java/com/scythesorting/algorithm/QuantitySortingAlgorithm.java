package com.scythesorting.algorithm;

import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * Quantity sorting algorithm: sorts by stack count (largest first), then alphabetically.
 */
public class QuantitySortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        // Largest stacks first (descending count), then alphabetical
        Comparator<ItemStack> base = Comparator
                .comparingInt(ItemStack::getCount).reversed()
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }
}
