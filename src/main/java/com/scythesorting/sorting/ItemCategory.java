package com.scythesorting.sorting;

/**
 * Logical item categories used by the Smart sorting mode.
 * Items are assigned to categories based on their registry path and properties.
 * The ordinal of each value represents the default sort priority.
 */
public enum ItemCategory {
    BUILDING_BLOCKS,
    NATURAL_BLOCKS,
    ORES,
    INGOTS,
    GEMS,
    REDSTONE,
    WOOD,
    STONE,
    DECORATION,
    FOOD,
    FARMING,
    TOOLS,
    WEAPONS,
    ARMOR,
    POTIONS,
    UTILITY,
    MOB_DROPS,
    MISC;

    /**
     * Returns the category ordinal to be used for sorting priority.
     * Lower ordinal = higher priority (appears first).
     */
    public int getPriority() {
        return ordinal();
    }
}
