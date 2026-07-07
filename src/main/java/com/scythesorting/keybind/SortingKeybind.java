package com.scythesorting.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Registers and provides access to the Scythe Sorting hotkey.
 * The default key is {@code R}, configurable via Minecraft's Controls screen.
 */
public class SortingKeybind {

    private static KeyBinding sortKeyBinding;

    /**
     * Registers the sort keybind with the Fabric keybinding API.
     * Must be called during client initialization.
     */
    public static void register() {
        sortKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.scythesorting.sort",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.scythesorting"
        ));
    }

    /**
     * Returns the registered {@link KeyBinding} instance.
     */
    public static KeyBinding getKeyBinding() {
        return sortKeyBinding;
    }

    /**
     * Returns {@code true} if the sort key was pressed since the last call to this method.
     */
    public static boolean wasPressed() {
        return sortKeyBinding != null && sortKeyBinding.wasPressed();
    }
}
