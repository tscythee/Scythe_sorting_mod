package com.scythesorting.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Registers the Scythe Sorting configuration screen with Mod Menu.
 * When Mod Menu is installed, clicking the config button will open
 * the Cloth Config-powered settings screen.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ScytheSortingConfigScreen::create;
    }
}
