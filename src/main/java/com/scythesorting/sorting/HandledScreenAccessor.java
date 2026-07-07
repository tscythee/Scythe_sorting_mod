package com.scythesorting.sorting;

/**
 * Accessor interface implemented by {@link com.scythesorting.mixin.HandledScreenMixin}
 * to expose the protected GUI origin coordinates of {@link net.minecraft.client.gui.screen.ingame.HandledScreen}.
 *
 * <p>This avoids reflection and is the idiomatic Fabric/Mixin approach for accessing
 * protected fields from outside the class hierarchy.</p>
 */
public interface HandledScreenAccessor {

    /**
     * Returns the X coordinate of the GUI background (left edge).
     */
    int scythesorting$getX();

    /**
     * Returns the Y coordinate of the GUI background (top edge).
     */
    int scythesorting$getY();
}
