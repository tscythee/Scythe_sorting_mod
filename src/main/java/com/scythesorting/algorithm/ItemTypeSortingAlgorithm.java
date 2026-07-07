package com.scythesorting.algorithm;

import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.Comparator;

/**
 * Item Type sorting algorithm: groups items by their registry namespace and path prefix,
 * approximating vanilla creative tab grouping, then sorts alphabetically within each group.
 */
public class ItemTypeSortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        Comparator<ItemStack> base = Comparator
                .comparing(stack -> Registries.ITEM.getId(stack.getItem()).getNamespace())
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }
}
