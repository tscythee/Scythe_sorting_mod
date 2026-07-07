package com.scythesorting.client;

import com.scythesorting.config.ScytheSortingConfig;
import com.scythesorting.keybind.SortingKeybind;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Client-side entrypoint for the Scythe Sorting mod.
 *
 * <p>This class is the only entrypoint registered in {@code fabric.mod.json}.
 * It is annotated with {@link Environment}{@code (EnvType.CLIENT)} to guarantee
 * that no server-side code is ever executed, keeping the mod 100% client-side.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Load the persistent configuration.</li>
 *   <li>Register the sort keybind.</li>
 * </ul>
 */
@Environment(EnvType.CLIENT)
public class ScytheSortingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Load configuration (creates default config file if not present)
        ScytheSortingConfig.getInstance();

        // Register the sort keybind
        SortingKeybind.register();

        System.out.println("[ScytheSorting] Scythe Sorting initialized successfully.");
    }
}
