package com.scythesorting.algorithm;

import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * Alphabetical sorting algorithm: sorts items A-Z by their display name.
 */
public class AlphabeticalSortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        Comparator<ItemStack> base = Comparator.comparing(ItemStackUtil::getDisplayName);
        return ascending ? base : base.reversed();
    }
}
