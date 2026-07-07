package com.scythesorting.algorithm;

import com.scythesorting.util.ItemStackUtil;
import net.minecraft.item.*;

import java.util.Comparator;

/**
 * Equipment First sorting algorithm: prioritizes armor, weapons, tools, and shields,
 * then sorts the remaining items alphabetically.
 *
 * <p>Priority order within equipment: Armor (0) → Weapons (1) → Tools (2) → Shield (3) → Other (4).</p>
 */
public class EquipmentFirstSortingAlgorithm implements SortingAlgorithm {

    @Override
    public Comparator<ItemStack> getComparator(boolean ascending) {
        Comparator<ItemStack> base = Comparator
                .comparingInt(EquipmentFirstSortingAlgorithm::getEquipmentPriority)
                .thenComparing(ItemStackUtil::getDisplayName);

        return ascending ? base : base.reversed();
    }

    private static int getEquipmentPriority(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem) return 0;
        if (item instanceof SwordItem || item instanceof BowItem
                || item instanceof CrossbowItem || item instanceof TridentItem
                || item instanceof MaceItem) return 1;
        if (item instanceof ToolItem) return 2;
        if (item instanceof ShieldItem) return 3;
        return 4;
    }
}
