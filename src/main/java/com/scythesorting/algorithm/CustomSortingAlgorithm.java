package com.scythesorting.algorithm;

import com.scythesorting.config.ScytheSortingConfig;
import com.scythesorting.sorting.ItemCategorizer;
import com.scythesorting.sorting.ItemCategory;
import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

/**
 * Custom Order sorting algorithm: uses the player-defined category order from
 * {@link ScytheSortingConfig#customCategoryOrder}, then sorts alphabetically within each category.
 */
public class CustomSortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        List<String> order = ScytheSortingConfig.getInstance().customCategoryOrder;

        Comparator<ItemStack> base = Comparator
                .comparingInt(stack -> getCustomPriority(stack, order))
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }

    private static int getCustomPriority(ItemStack stack, List<String> order) {
        ItemCategory category = ItemCategorizer.categorize(stack.getItem());
        int index = order.indexOf(category.name());
        return index < 0 ? order.size() : index;
    }
}
