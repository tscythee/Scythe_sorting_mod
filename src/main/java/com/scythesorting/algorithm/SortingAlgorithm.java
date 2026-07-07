package com.scythesorting.algorithm;

import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

/**
 * Base interface for all sorting algorithms used by Scythe Sorting.
 * Each implementation defines a specific strategy for ordering a list of {@link ItemStack}s.
 *
 * <p>Implementations must be stateless and thread-safe. They receive a snapshot of the
 * inventory contents and return a sorted copy; the actual slot manipulation is handled
 * separately by the {@link com.scythesorting.sorting.SortingExecutor}.</p>
 */
public interface SortingAlgorithm {

    /**
     * Returns the {@link Comparator} that defines the sort order for this algorithm.
     *
     * @param ascending {@code true} for ascending order, {@code false} for descending
     * @return a comparator for {@link ItemStack}
     */
    Comparator<ItemStack> getComparator(boolean ascending);

    /**
     * Sorts the given list of item stacks in-place using this algorithm's comparator.
     *
     * @param stacks    the mutable list of item stacks to sort (may contain empty stacks)
     * @param ascending {@code true} for ascending order, {@code false} for descending
     */
    default void sort(List<ItemStack> stacks, boolean ascending) {
        Comparator<ItemStack> comparator = getComparator(ascending);
        // Move empty stacks to the end regardless of sort direction
        stacks.sort((a, b) -> {
            boolean aEmpty = a.isEmpty();
            boolean bEmpty = b.isEmpty();
            if (aEmpty && bEmpty) return 0;
            if (aEmpty) return 1;
            if (bEmpty) return -1;
            return comparator.compare(a, b);
        });
    }
}
