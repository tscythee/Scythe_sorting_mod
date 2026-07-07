package com.scythesorting.sorting;

/**
 * Defines all available sorting modes for the Scythe Sorting mod.
 * Each mode represents a distinct strategy for organizing inventory items.
 */
public enum SortingMode {

    /**
     * Smart/Default: Groups items into logical categories, then sorts alphabetically within each category.
     */
    SMART("scythesorting.sorting_mode.smart"),

    /**
     * Quantity: Sorts by stack size (largest first), then alphabetically.
     */
    QUANTITY("scythesorting.sorting_mode.quantity"),

    /**
     * Alphabetical: Simple A-Z sorting by item display name.
     */
    ALPHABETICAL("scythesorting.sorting_mode.alphabetical"),

    /**
     * Item Type: Groups by vanilla item categories/creative tabs.
     */
    ITEM_TYPE("scythesorting.sorting_mode.item_type"),

    /**
     * Blocks First: All blocks first, then everything else.
     */
    BLOCKS_FIRST("scythesorting.sorting_mode.blocks_first"),

    /**
     * Equipment First: Armor, weapons, tools, shield first; then remaining items.
     */
    EQUIPMENT_FIRST("scythesorting.sorting_mode.equipment_first"),

    /**
     * Ores and Resources: Prioritizes ores, ingots, gems, and related resources.
     */
    ORES_AND_RESOURCES("scythesorting.sorting_mode.ores_and_resources"),

    /**
     * Custom Order: Uses the player-defined category order from configuration.
     */
    CUSTOM("scythesorting.sorting_mode.custom");

    private final String translationKey;

    SortingMode(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }
}
