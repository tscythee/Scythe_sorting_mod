package com.scythesorting.config;

import com.scythesorting.gui.ButtonPosition;
import com.scythesorting.sorting.SortingMode;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Builds the in-game configuration screen for Scythe Sorting using Cloth Config.
 * This screen is accessible via Mod Menu or the in-game pause menu.
 */
public class ScytheSortingConfigScreen {

    /**
     * Creates and returns the Cloth Config screen.
     *
     * @param parent the parent screen to return to when the config screen is closed
     * @return the built configuration {@link Screen}
     */
    public static Screen create(Screen parent) {
        ScytheSortingConfig config = ScytheSortingConfig.getInstance();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("scythesorting.config.title"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // -----------------------------------------------------------------
        // Category: Sorting
        // -----------------------------------------------------------------
        ConfigCategory sortingCategory = builder.getOrCreateCategory(
                Text.translatable("scythesorting.config.category.sorting"));

        sortingCategory.addEntry(entryBuilder
                .startEnumSelector(
                        Text.translatable("scythesorting.config.sorting_mode"),
                        SortingMode.class,
                        config.sortingMode)
                .setDefaultValue(SortingMode.SMART)
                .setEnumNameProvider(mode -> Text.translatable(((SortingMode) mode).getTranslationKey()))
                .setTooltip(Text.translatable("scythesorting.config.sorting_mode.tooltip"))
                .setSaveConsumer(val -> config.sortingMode = val)
                .build());

        sortingCategory.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("scythesorting.config.ascending_order"),
                        config.ascendingOrder)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("scythesorting.config.ascending_order.tooltip"))
                .setSaveConsumer(val -> config.ascendingOrder = val)
                .build());

        sortingCategory.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("scythesorting.config.merge_stacks"),
                        config.mergeStacksBeforeSorting)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("scythesorting.config.merge_stacks.tooltip"))
                .setSaveConsumer(val -> config.mergeStacksBeforeSorting = val)
                .build());

        sortingCategory.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("scythesorting.config.remember_mode"),
                        config.rememberLastSortingMode)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("scythesorting.config.remember_mode.tooltip"))
                .setSaveConsumer(val -> config.rememberLastSortingMode = val)
                .build());

        // -----------------------------------------------------------------
        // Category: Button
        // -----------------------------------------------------------------
        ConfigCategory buttonCategory = builder.getOrCreateCategory(
                Text.translatable("scythesorting.config.category.button"));

        buttonCategory.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("scythesorting.config.show_button"),
                        config.showSortButton)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("scythesorting.config.show_button.tooltip"))
                .setSaveConsumer(val -> config.showSortButton = val)
                .build());

        buttonCategory.addEntry(entryBuilder
                .startEnumSelector(
                        Text.translatable("scythesorting.config.button_position"),
                        ButtonPosition.class,
                        config.buttonPosition)
                .setDefaultValue(ButtonPosition.TOP_RIGHT)
                .setEnumNameProvider(pos -> Text.translatable(((ButtonPosition) pos).getTranslationKey()))
                .setTooltip(Text.translatable("scythesorting.config.button_position.tooltip"))
                .setSaveConsumer(val -> config.buttonPosition = val)
                .build());

        buttonCategory.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("scythesorting.config.play_sound"),
                        config.playSoundOnSort)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("scythesorting.config.play_sound.tooltip"))
                .setSaveConsumer(val -> config.playSoundOnSort = val)
                .build());

        buttonCategory.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("scythesorting.config.animate_button"),
                        config.animateButton)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("scythesorting.config.animate_button.tooltip"))
                .setSaveConsumer(val -> config.animateButton = val)
                .build());

        // -----------------------------------------------------------------
        // Category: Custom Order
        // -----------------------------------------------------------------
        ConfigCategory customCategory = builder.getOrCreateCategory(
                Text.translatable("scythesorting.config.category.custom_order"));

        customCategory.addEntry(entryBuilder
                .startStrList(
                        Text.translatable("scythesorting.config.custom_order"),
                        config.customCategoryOrder)
                .setDefaultValue(Arrays.asList(
                        "BUILDING_BLOCKS", "NATURAL_BLOCKS", "ORES", "INGOTS", "GEMS",
                        "REDSTONE", "WOOD", "STONE", "DECORATION", "FOOD", "FARMING",
                        "TOOLS", "WEAPONS", "ARMOR", "POTIONS", "UTILITY", "MOB_DROPS", "MISC"))
                .setTooltip(Text.translatable("scythesorting.config.custom_order.tooltip"))
                .setSaveConsumer(val -> config.customCategoryOrder = val)
                .build());

        return builder.build();
    }
}
