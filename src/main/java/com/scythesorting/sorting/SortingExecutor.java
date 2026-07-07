package com.scythesorting.sorting;

import com.scythesorting.algorithm.SortingAlgorithm;
import com.scythesorting.config.ScytheSortingConfig;
import com.scythesorting.registry.SortingAlgorithmRegistry;
import com.scythesorting.util.ItemStackUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Executes the full sorting pipeline for a given inventory section.
 *
 * <p>The pipeline consists of two phases:</p>
 * <ol>
 *   <li><strong>Stack merging</strong>: identical stacks are merged to free up slots.</li>
 *   <li><strong>Sorting</strong>: items are rearranged according to the active algorithm.</li>
 * </ol>
 *
 * <p>All interactions are performed via legitimate vanilla inventory click packets
 * ({@link net.minecraft.client.network.ClientPlayerInteractionManager#clickSlot}),
 * exactly as if the player were manually moving items. No server memory is accessed
 * or modified directly.</p>
 *
 * <p>A guard flag ({@link #isSorting}) prevents concurrent sorting operations.</p>
 */
public class SortingExecutor {

    /** Prevents duplicate/concurrent sorting operations. */
    private static final AtomicBoolean isSorting = new AtomicBoolean(false);

    /**
     * Returns {@code true} if a sorting operation is currently in progress.
     */
    public static boolean isSorting() {
        return isSorting.get();
    }

    /**
     * Initiates sorting for the given inventory section.
     * This method is safe to call from the render thread.
     *
     * @param screen  the currently open inventory screen
     * @param section the inventory section to sort
     */
    public static void sort(HandledScreen<?> screen, InventoryDetector.InventorySection section) {
        if (!isSorting.compareAndSet(false, true)) {
            return; // Already sorting
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) {
            isSorting.set(false);
            return;
        }

        try {
            ScytheSortingConfig config = ScytheSortingConfig.getInstance();
            ScreenHandler handler = screen.getScreenHandler();
            List<Slot> slots = section.slots();

            // Phase 1: Merge identical stacks (if enabled)
            if (config.mergeStacksBeforeSorting) {
                mergeStacks(client, handler, slots);
            }

            // Phase 2: Sort items
            SortingAlgorithm algorithm = SortingAlgorithmRegistry.get(config.sortingMode);
            sortSlots(client, handler, slots, algorithm, config.ascendingOrder);

            // Play sound (if enabled)
            if (config.playSoundOnSort) {
                client.getSoundManager().play(
                        PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f, 0.5f));
            }

        } catch (Exception e) {
            System.err.println("[ScytheSorting] Error during sorting: " + e.getMessage());
        } finally {
            isSorting.set(false);
        }
    }

    // -------------------------------------------------------------------------
    // Phase 1: Stack Merging
    // -------------------------------------------------------------------------

    /**
     * Merges identical stacks within the given slot list.
     * Uses vanilla shift-click and drag mechanics via click packets.
     */
    private static void mergeStacks(MinecraftClient client, ScreenHandler handler, List<Slot> slots) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        int syncId = handler.syncId;

        for (int i = 0; i < slots.size(); i++) {
            Slot source = slots.get(i);
            ItemStack sourceStack = source.getStack();
            if (sourceStack.isEmpty()) continue;

            for (int j = i + 1; j < slots.size(); j++) {
                Slot target = slots.get(j);
                ItemStack targetStack = target.getStack();
                if (targetStack.isEmpty()) continue;

                int transferable = ItemStackUtil.getTransferableCount(targetStack, sourceStack);
                if (transferable <= 0) continue;

                // Pick up source stack
                client.interactionManager.clickSlot(syncId, source.id, 0, SlotActionType.PICKUP, player);
                // Place onto target (partial or full merge)
                client.interactionManager.clickSlot(syncId, target.id, 0, SlotActionType.PICKUP, player);
                // If source was not fully merged, put remainder back
                if (!handler.getCursorStack().isEmpty()) {
                    client.interactionManager.clickSlot(syncId, source.id, 0, SlotActionType.PICKUP, player);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Phase 2: Sorting
    // -------------------------------------------------------------------------

    /**
     * Sorts the items in the given slots according to the provided algorithm.
     * Computes the optimal move plan before executing any click packets.
     */
    private static void sortSlots(MinecraftClient client, ScreenHandler handler,
                                   List<Slot> slots, SortingAlgorithm algorithm, boolean ascending) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // Snapshot current contents
        List<ItemStack> contents = new ArrayList<>();
        for (Slot slot : slots) {
            contents.add(slot.getStack().copy());
        }

        // Compute sorted order
        List<ItemStack> sorted = new ArrayList<>(contents);
        algorithm.sort(sorted, ascending);

        // Execute moves: for each target position, find the current slot holding
        // the desired item and swap it into place using pickup clicks.
        int syncId = handler.syncId;

        // We work with a mutable copy of slot indices to track current positions
        List<ItemStack> current = new ArrayList<>(contents);

        for (int targetIdx = 0; targetIdx < slots.size(); targetIdx++) {
            ItemStack desired = sorted.get(targetIdx);
            ItemStack currentAtTarget = current.get(targetIdx);

            if (desired.isEmpty() && currentAtTarget.isEmpty()) continue;
            if (!desired.isEmpty() && ItemStack.canCombine(desired, currentAtTarget)
                    && desired.getCount() == currentAtTarget.getCount()) {
                continue; // Already in correct position
            }

            // Find where the desired item currently is
            int sourceIdx = findStack(current, desired, targetIdx);
            if (sourceIdx < 0) continue;

            // Perform swap: pick up source, place at target, put displaced item at source
            Slot sourceSlot = slots.get(sourceIdx);
            Slot targetSlot = slots.get(targetIdx);

            // Pick up source
            client.interactionManager.clickSlot(syncId, sourceSlot.id, 0, SlotActionType.PICKUP, player);
            // Place at target (picks up whatever was there)
            client.interactionManager.clickSlot(syncId, targetSlot.id, 0, SlotActionType.PICKUP, player);
            // If something was displaced, put it at the source slot
            if (!handler.getCursorStack().isEmpty()) {
                client.interactionManager.clickSlot(syncId, sourceSlot.id, 0, SlotActionType.PICKUP, player);
            }

            // Update our local tracking
            ItemStack displaced = current.get(targetIdx);
            current.set(targetIdx, current.get(sourceIdx));
            current.set(sourceIdx, displaced);
        }
    }

    /**
     * Finds the index of the first slot in {@code current} (starting from {@code startFrom})
     * that holds a stack matching {@code desired}.
     */
    private static int findStack(List<ItemStack> current, ItemStack desired, int startFrom) {
        for (int i = startFrom; i < current.size(); i++) {
            ItemStack stack = current.get(i);
            if (!stack.isEmpty() && ItemStack.canCombine(stack, desired)
                    && stack.getCount() == desired.getCount()) {
                return i;
            }
        }
        // Fallback: search from beginning (in case of duplicates)
        for (int i = 0; i < startFrom; i++) {
            ItemStack stack = current.get(i);
            if (!stack.isEmpty() && ItemStack.canCombine(stack, desired)
                    && stack.getCount() == desired.getCount()) {
                return i;
            }
        }
        return -1;
    }
}
