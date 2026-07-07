package com.scythesorting.gui;

/**
 * Defines the possible positions for the sorting button within the inventory GUI.
 */
public enum ButtonPosition {
    TOP_LEFT("scythesorting.button_position.top_left"),
    TOP_RIGHT("scythesorting.button_position.top_right"),
    BOTTOM_LEFT("scythesorting.button_position.bottom_left"),
    BOTTOM_RIGHT("scythesorting.button_position.bottom_right");

    private final String translationKey;

    ButtonPosition(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }
}
