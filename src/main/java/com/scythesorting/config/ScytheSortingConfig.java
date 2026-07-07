package com.scythesorting.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scythesorting.gui.ButtonPosition;
import com.scythesorting.sorting.SortingMode;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all persistent configuration for the Scythe Sorting mod.
 * Configuration is stored as JSON in the standard Fabric config directory.
 * This class is a simple POJO serialized/deserialized with Gson.
 */
public class ScytheSortingConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "scythesorting.json";
    private static ScytheSortingConfig instance;

    // -------------------------------------------------------------------------
    // Sorting Options
    // -------------------------------------------------------------------------

    /** The active sorting mode. */
    public SortingMode sortingMode = SortingMode.SMART;

    /** Whether to sort in ascending order (true) or descending (false). */
    public boolean ascendingOrder = true;

    /** Whether to merge identical stacks before sorting. */
    public boolean mergeStacksBeforeSorting = true;

    /** Whether to remember the last selected sorting mode across sessions. */
    public boolean rememberLastSortingMode = true;

    // -------------------------------------------------------------------------
    // Button Options
    // -------------------------------------------------------------------------

    /** Whether the sort button is shown in inventory GUIs. */
    public boolean showSortButton = true;

    /** The position of the sort button within the inventory GUI. */
    public ButtonPosition buttonPosition = ButtonPosition.TOP_RIGHT;

    /** Whether to play a click sound when sorting. */
    public boolean playSoundOnSort = true;

    /** Whether to show a brief animation when the sort button is clicked. */
    public boolean animateButton = true;

    // -------------------------------------------------------------------------
    // Custom Order
    // -------------------------------------------------------------------------

    /**
     * The custom category order used when {@link SortingMode#CUSTOM} is active.
     * Each entry is the name of a {@link com.scythesorting.sorting.ItemCategory}.
     */
    public List<String> customCategoryOrder = new ArrayList<>(List.of(
            "BUILDING_BLOCKS", "NATURAL_BLOCKS", "ORES", "INGOTS", "GEMS",
            "REDSTONE", "WOOD", "STONE", "DECORATION", "FOOD", "FARMING",
            "TOOLS", "WEAPONS", "ARMOR", "POTIONS", "UTILITY", "MOB_DROPS", "MISC"
    ));

    // -------------------------------------------------------------------------
    // Singleton Access
    // -------------------------------------------------------------------------

    public static ScytheSortingConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    private static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
    }

    public static ScytheSortingConfig load() {
        Path path = getConfigPath();
        if (path.toFile().exists()) {
            try (Reader reader = new FileReader(path.toFile())) {
                ScytheSortingConfig config = GSON.fromJson(reader, ScytheSortingConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (IOException e) {
                System.err.println("[ScytheSorting] Failed to load config: " + e.getMessage());
            }
        }
        ScytheSortingConfig defaults = new ScytheSortingConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try (Writer writer = new FileWriter(getConfigPath().toFile())) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[ScytheSorting] Failed to save config: " + e.getMessage());
        }
    }
}
