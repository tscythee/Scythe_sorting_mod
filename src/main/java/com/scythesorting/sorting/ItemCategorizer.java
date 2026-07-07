package com.scythesorting.sorting;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Classifies Minecraft items into logical {@link ItemCategory} groups.
 * This classification is used by the Smart and Custom sorting modes.
 * The logic is based on item registry paths, item class types, and known item IDs.
 */
public class ItemCategorizer {

    /**
     * Returns the {@link ItemCategory} for the given {@link Item}.
     *
     * @param item the item to classify
     * @return the appropriate category
     */
    public static ItemCategory categorize(Item item) {
        if (item == Items.AIR) return ItemCategory.MISC;

        Identifier id = Registries.ITEM.getId(item);
        String path = id.getPath();

        // -----------------------------------------------------------------
        // Potions
        // -----------------------------------------------------------------
        if (item instanceof PotionItem || item instanceof SplashPotionItem
                || item instanceof LingeringPotionItem || item instanceof TippedArrowItem) {
            return ItemCategory.POTIONS;
        }

        // -----------------------------------------------------------------
        // Armor
        // -----------------------------------------------------------------
        if (item instanceof ArmorItem) {
            return ItemCategory.ARMOR;
        }

        // -----------------------------------------------------------------
        // Weapons
        // -----------------------------------------------------------------
        if (item instanceof SwordItem || item instanceof BowItem || item instanceof CrossbowItem
                || item instanceof TridentItem || item instanceof MaceItem) {
            return ItemCategory.WEAPONS;
        }

        // -----------------------------------------------------------------
        // Tools
        // -----------------------------------------------------------------
        if (item instanceof ToolItem || item instanceof FishingRodItem
                || item instanceof FlintAndSteelItem || item instanceof ShearsItem) {
            return ItemCategory.TOOLS;
        }

        // -----------------------------------------------------------------
        // Food
        // -----------------------------------------------------------------
        if (item.isFood()) {
            return ItemCategory.FOOD;
        }

        // -----------------------------------------------------------------
        // Ores (by path keywords)
        // -----------------------------------------------------------------
        if (path.endsWith("_ore") || path.contains("raw_")) {
            return ItemCategory.ORES;
        }

        // -----------------------------------------------------------------
        // Ingots and Nuggets
        // -----------------------------------------------------------------
        if (path.endsWith("_ingot") || path.endsWith("_nugget") || path.endsWith("_scrap")
                || path.equals("netherite_ingot") || path.equals("copper_ingot")) {
            return ItemCategory.INGOTS;
        }

        // -----------------------------------------------------------------
        // Gems
        // -----------------------------------------------------------------
        if (path.equals("diamond") || path.equals("emerald") || path.equals("amethyst_shard")
                || path.equals("quartz") || path.equals("lapis_lazuli") || path.equals("prismarine_crystals")
                || path.equals("prismarine_shard") || path.equals("nether_star")) {
            return ItemCategory.GEMS;
        }

        // -----------------------------------------------------------------
        // Redstone
        // -----------------------------------------------------------------
        if (path.equals("redstone") || path.equals("redstone_block") || path.equals("comparator")
                || path.equals("repeater") || path.equals("observer") || path.equals("piston")
                || path.equals("sticky_piston") || path.equals("lever") || path.equals("tripwire_hook")
                || path.equals("target") || path.equals("daylight_detector") || path.equals("hopper")
                || path.equals("dropper") || path.equals("dispenser") || path.equals("tnt")
                || path.equals("redstone_torch") || path.equals("redstone_lamp")
                || path.contains("pressure_plate") || path.contains("button")
                || path.contains("trapdoor") || path.contains("door")) {
            return ItemCategory.REDSTONE;
        }

        // -----------------------------------------------------------------
        // Farming
        // -----------------------------------------------------------------
        if (path.endsWith("_seeds") || path.endsWith("_sapling") || path.endsWith("_bulb")
                || path.equals("bone_meal") || path.equals("wheat") || path.equals("sugar_cane")
                || path.equals("pumpkin") || path.equals("melon") || path.equals("cactus")
                || path.equals("bamboo") || path.equals("kelp") || path.equals("sea_pickle")
                || path.equals("lily_pad") || path.equals("flower_pot") || path.endsWith("_flower")
                || path.endsWith("_tulip") || path.equals("dandelion") || path.equals("poppy")
                || path.equals("blue_orchid") || path.equals("allium") || path.equals("azure_bluet")
                || path.equals("oxeye_daisy") || path.equals("cornflower") || path.equals("lily_of_the_valley")
                || path.equals("wither_rose") || path.endsWith("_mushroom") || path.equals("chorus_fruit")
                || path.equals("chorus_flower") || path.equals("chorus_plant")
                || path.equals("sweet_berries") || path.equals("glow_berries")) {
            return ItemCategory.FARMING;
        }

        // -----------------------------------------------------------------
        // Mob Drops
        // -----------------------------------------------------------------
        if (path.equals("bone") || path.equals("string") || path.equals("feather")
                || path.equals("leather") || path.equals("ink_sac") || path.equals("glow_ink_sac")
                || path.equals("slime_ball") || path.equals("blaze_rod") || path.equals("blaze_powder")
                || path.equals("ender_pearl") || path.equals("ender_eye") || path.equals("ghast_tear")
                || path.equals("magma_cream") || path.equals("spider_eye") || path.equals("fermented_spider_eye")
                || path.equals("rotten_flesh") || path.equals("gunpowder") || path.equals("arrow")
                || path.equals("spectral_arrow") || path.equals("rabbit_hide") || path.equals("rabbit_foot")
                || path.equals("phantom_membrane") || path.equals("turtle_scute") || path.equals("armadillo_scute")
                || path.equals("shulker_shell") || path.equals("nautilus_shell") || path.equals("heart_of_the_sea")
                || path.equals("dragon_breath") || path.equals("totem_of_undying")
                || path.equals("wither_skeleton_skull") || path.equals("creeper_head")
                || path.equals("zombie_head") || path.equals("skeleton_skull") || path.equals("player_head")) {
            return ItemCategory.MOB_DROPS;
        }

        // -----------------------------------------------------------------
        // Utility items
        // -----------------------------------------------------------------
        if (item instanceof BucketItem || item instanceof BoatItem || item instanceof MinecartItem
                || path.equals("compass") || path.equals("clock") || path.equals("map")
                || path.equals("filled_map") || path.equals("book") || path.equals("written_book")
                || path.equals("writable_book") || path.equals("enchanted_book")
                || path.equals("name_tag") || path.equals("lead") || path.equals("saddle")
                || path.equals("horse_armor") || path.endsWith("_horse_armor")
                || path.equals("elytra") || path.equals("shield") || path.equals("spyglass")
                || path.equals("recovery_compass") || path.equals("echo_shard")
                || path.equals("bundle") || path.equals("lantern") || path.equals("soul_lantern")
                || path.equals("torch") || path.equals("soul_torch") || path.equals("campfire")
                || path.equals("soul_campfire") || path.equals("crafting_table")
                || path.equals("furnace") || path.equals("blast_furnace") || path.equals("smoker")
                || path.equals("anvil") || path.equals("chipped_anvil") || path.equals("damaged_anvil")
                || path.equals("grindstone") || path.equals("smithing_table") || path.equals("loom")
                || path.equals("cartography_table") || path.equals("fletching_table")
                || path.equals("stonecutter") || path.equals("brewing_stand") || path.equals("cauldron")
                || path.equals("water_cauldron") || path.equals("lava_cauldron") || path.equals("powder_snow_cauldron")
                || path.equals("enchanting_table") || path.equals("bookshelf")
                || path.equals("chiseled_bookshelf") || path.equals("chest") || path.equals("trapped_chest")
                || path.equals("barrel") || path.equals("shulker_box") || path.endsWith("_shulker_box")
                || path.equals("ender_chest") || path.equals("hopper") || path.equals("dropper")
                || path.equals("dispenser")) {
            return ItemCategory.UTILITY;
        }

        // -----------------------------------------------------------------
        // Wood (logs, planks, slabs, stairs, fences, etc.)
        // -----------------------------------------------------------------
        if (path.endsWith("_log") || path.endsWith("_wood") || path.endsWith("_planks")
                || path.endsWith("_slab") && isWoodType(path) || path.endsWith("_stairs") && isWoodType(path)
                || path.endsWith("_fence") || path.endsWith("_fence_gate")
                || path.endsWith("_sign") || path.endsWith("_hanging_sign")
                || path.endsWith("_stripped_log") || path.endsWith("_stripped_wood")) {
            return ItemCategory.WOOD;
        }

        // -----------------------------------------------------------------
        // Stone (cobblestone, stone bricks, deepslate, etc.)
        // -----------------------------------------------------------------
        if (path.contains("stone") || path.contains("cobblestone") || path.contains("deepslate")
                || path.contains("basalt") || path.contains("blackstone") || path.contains("granite")
                || path.contains("diorite") || path.contains("andesite") || path.contains("tuff")
                || path.contains("calcite") || path.contains("dripstone") || path.contains("sandstone")
                || path.contains("red_sandstone") || path.contains("prismarine") || path.contains("purpur")
                || path.contains("end_stone") || path.contains("nether_brick") || path.contains("quartz_block")
                || path.contains("smooth_quartz") || path.contains("cut_sandstone")) {
            return ItemCategory.STONE;
        }

        // -----------------------------------------------------------------
        // Natural Blocks (dirt, grass, sand, gravel, clay, etc.)
        // -----------------------------------------------------------------
        if (path.equals("dirt") || path.equals("grass_block") || path.equals("coarse_dirt")
                || path.equals("podzol") || path.equals("mycelium") || path.equals("rooted_dirt")
                || path.equals("mud") || path.equals("muddy_mangrove_roots") || path.equals("sand")
                || path.equals("red_sand") || path.equals("gravel") || path.equals("clay")
                || path.equals("snow") || path.equals("snow_block") || path.equals("ice")
                || path.equals("packed_ice") || path.equals("blue_ice") || path.equals("powder_snow")
                || path.equals("soul_sand") || path.equals("soul_soil") || path.equals("netherrack")
                || path.equals("magma_block") || path.equals("crimson_nylium") || path.equals("warped_nylium")
                || path.equals("end_stone") || path.equals("obsidian") || path.equals("crying_obsidian")
                || path.equals("bedrock") || path.equals("sponge") || path.equals("wet_sponge")
                || path.equals("moss_block") || path.equals("moss_carpet") || path.equals("sculk")
                || path.equals("sculk_vein") || path.equals("sculk_catalyst") || path.equals("sculk_shrieker")
                || path.equals("sculk_sensor") || path.equals("calibrated_sculk_sensor")) {
            return ItemCategory.NATURAL_BLOCKS;
        }

        // -----------------------------------------------------------------
        // Decoration
        // -----------------------------------------------------------------
        if (path.contains("glass") || path.contains("carpet") || path.contains("banner")
                || path.contains("bed") || path.contains("painting") || path.contains("frame")
                || path.contains("candle") || path.contains("dye") || path.contains("concrete")
                || path.contains("terracotta") || path.contains("glazed") || path.contains("wool")
                || path.contains("stained") || path.contains("coral") || path.contains("sea_lantern")
                || path.contains("glowstone") || path.contains("shroomlight")
                || path.contains("froglight") || path.contains("amethyst")
                || path.contains("tinted_glass") || path.contains("chain") || path.contains("bell")
                || path.contains("conduit") || path.contains("beacon") || path.contains("jukebox")
                || path.contains("note_block") || path.contains("music_disc")) {
            return ItemCategory.DECORATION;
        }

        // -----------------------------------------------------------------
        // Building Blocks (generic fallback for block items)
        // -----------------------------------------------------------------
        if (item instanceof BlockItem) {
            return ItemCategory.BUILDING_BLOCKS;
        }

        return ItemCategory.MISC;
    }

    /**
     * Heuristic check: does the given path belong to a wood-type item?
     */
    private static boolean isWoodType(String path) {
        return path.contains("oak") || path.contains("spruce") || path.contains("birch")
                || path.contains("jungle") || path.contains("acacia") || path.contains("dark_oak")
                || path.contains("cherry") || path.contains("mangrove") || path.contains("bamboo")
                || path.contains("crimson") || path.contains("warped");
    }
}
