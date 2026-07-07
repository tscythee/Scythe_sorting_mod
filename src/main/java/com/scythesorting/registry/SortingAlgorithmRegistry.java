package com.scythesorting.registry;

import com.scythesorting.algorithm.*;
import com.scythesorting.sorting.SortingMode;

import java.util.EnumMap;
import java.util.Map;

/**
 * Registry that maps each {@link SortingMode} to its corresponding {@link SortingAlgorithm} instance.
 * This enables modular addition of new sorting modes without modifying existing code.
 */
public class SortingAlgorithmRegistry {

    private static final Map<SortingMode, SortingAlgorithm> REGISTRY = new EnumMap<>(SortingMode.class);

    static {
        REGISTRY.put(SortingMode.SMART, new SmartSortingAlgorithm());
        REGISTRY.put(SortingMode.QUANTITY, new QuantitySortingAlgorithm());
        REGISTRY.put(SortingMode.ALPHABETICAL, new AlphabeticalSortingAlgorithm());
        REGISTRY.put(SortingMode.ITEM_TYPE, new ItemTypeSortingAlgorithm());
        REGISTRY.put(SortingMode.BLOCKS_FIRST, new BlocksFirstSortingAlgorithm());
        REGISTRY.put(SortingMode.EQUIPMENT_FIRST, new EquipmentFirstSortingAlgorithm());
        REGISTRY.put(SortingMode.ORES_AND_RESOURCES, new OresAndResourcesSortingAlgorithm());
        REGISTRY.put(SortingMode.CUSTOM, new CustomSortingAlgorithm());
    }

    /**
     * Returns the {@link SortingAlgorithm} for the given mode.
     *
     * @param mode the sorting mode
     * @return the associated algorithm, never {@code null}
     */
    public static SortingAlgorithm get(SortingMode mode) {
        return REGISTRY.getOrDefault(mode, REGISTRY.get(SortingMode.SMART));
    }

    private SortingAlgorithmRegistry() {}
}
