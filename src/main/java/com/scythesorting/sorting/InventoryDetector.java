package com.scythesorting.sorting;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects which inventory section (container or player) a given slot belongs to,
 * and returns the full range of slots for that section.
 *
 * <p>This class is critical for ensuring that the hotkey and button only sort the
 * inventory section currently under the mouse cursor, never mixing container and
 * player inventory slots.</p>
 *
 * <p>Detection strategy: In vanilla Minecraft, the player inventory slots always
 * occupy the last 36 slots of the screen handler (indices {@code size-36} to
 * {@code size-1}). Slots before that index belong to the container inventory.</p>
 */
public class InventoryDetector {

    /**
     * Represents a detected inventory section with its slot range.
     */
    public record InventorySection(List<Slot> slots, boolean isPlayerInventory) {}

    /**
     * Returns the {@link InventorySection} for the slot currently under the mouse cursor.
     *
     * @param screen    the currently open handled screen
     * @param mouseX    the current mouse X position
     * @param mouseY    the current mouse Y position
     * @return the detected section, or {@code null} if no slot is under the cursor
     */
    public static InventorySection detectFromMouse(HandledScreen<?> screen, double mouseX, double mouseY) {
        Slot hoveredSlot = getSlotUnderMouse(screen, mouseX, mouseY);
        if (hoveredSlot == null) return null;
        return detectFromSlot(screen.getScreenHandler(), hoveredSlot);
    }

    /**
     * Returns the {@link InventorySection} for the given slot.
     *
     * @param handler the screen handler
     * @param slot    the slot to check
     * @return the detected section
     */
    public static InventorySection detectFromSlot(ScreenHandler handler, Slot slot) {
        List<Slot> allSlots = handler.slots;
        int totalSlots = allSlots.size();

        // Player inventory is always the last 36 slots (hotbar + main inventory)
        int playerInventoryStart = totalSlots - 36;

        boolean isPlayerSlot = slot.getIndex() >= playerInventoryStart;

        if (isPlayerSlot) {
            // Return all 36 player inventory slots
            List<Slot> playerSlots = new ArrayList<>(allSlots.subList(playerInventoryStart, totalSlots));
            return new InventorySection(playerSlots, true);
        } else {
            // Return all container slots (everything before player inventory)
            List<Slot> containerSlots = new ArrayList<>(allSlots.subList(0, playerInventoryStart));
            return new InventorySection(containerSlots, false);
        }
    }

    /**
     * Returns the slot directly under the mouse cursor, or {@code null} if none.
     *
     * @param screen the currently open handled screen
     * @param mouseX the mouse X coordinate
     * @param mouseY the mouse Y coordinate
     * @return the hovered slot, or {@code null}
     */
    public static Slot getSlotUnderMouse(HandledScreen<?> screen, double mouseX, double mouseY) {
        ScreenHandler handler = screen.getScreenHandler();
        for (Slot slot : handler.slots) {
            if (isMouseOverSlot(screen, slot, mouseX, mouseY)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Checks whether the mouse is over the given slot in the given screen.
     * Uses the standard 16x16 slot size.
     */
    private static boolean isMouseOverSlot(HandledScreen<?> screen, Slot slot, double mouseX, double mouseY) {
        // HandledScreen exposes the GUI offset via getX/getY (backgroundWidth/Height)
        // We use the slot's x/y which are relative to the GUI background origin
        int guiLeft = getGuiLeft(screen);
        int guiTop = getGuiTop(screen);
        int slotX = guiLeft + slot.x;
        int slotY = guiTop + slot.y;
        return mouseX >= slotX && mouseX < slotX + 16
                && mouseY >= slotY && mouseY < slotY + 16;
    }

    /**
     * Retrieves the GUI left (x) offset of the screen.
     * Uses reflection-free accessor provided by the HandledScreenMixin.
     */
    public static int getGuiLeft(HandledScreen<?> screen) {
        return ((HandledScreenAccessor) screen).scythesorting$getX();
    }

    /**
     * Retrieves the GUI top (y) offset of the screen.
     */
    public static int getGuiTop(HandledScreen<?> screen) {
        return ((HandledScreenAccessor) screen).scythesorting$getY();
    }
}
