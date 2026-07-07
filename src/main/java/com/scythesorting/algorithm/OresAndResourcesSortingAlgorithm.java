package com.scythesorting.algorithm;

import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.Comparator;
import java.util.List;

/**
 * Ores and Resources sorting algorithm: prioritizes ores, ingots, gems, and related
 * resources in a logical progression from raw to refined materials.
 */
public class OresAndResourcesSortingAlgorithm implements SortingAlgorithm {

    /**
     * Ordered list of resource-related path keywords, from highest to lowest priority.
     * Items matching earlier entries will appear first.
     */
    private static final List<String> RESOURCE_PRIORITY = List.of(
            "_ore", "raw_", "coal", "copper", "iron", "gold", "redstone", "lapis",
            "quartz", "diamond", "emerald", "netherite", "_ingot", "_nugget",
            "_scrap", "amethyst", "prismarine"
    );

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        Comparator<ItemStack> base = Comparator
                .comparingInt(OresAndResourcesSortingAlgorithm::getResourcePriority)
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }

    private static int getResourcePriority(ItemStack stack) {
        String path = Registries.ITEM.getId(stack.getItem()).getPath();
        for (int i = 0; i < RESOURCE_PRIORITY.size(); i++) {
            if (path.contains(RESOURCE_PRIORITY.get(i))) {
                return i;
            }
        }
        return RESOURCE_PRIORITY.size(); // Non-resource items go last
    }
}
