# Scythe Sorting (SS)

**A client-side inventory sorting mod for Minecraft Fabric 1.21.1**

---

## Overview

Scythe Sorting is a quality-of-life utility mod that allows you to quickly organize any inventory in Minecraft — player inventory, chests, barrels, shulker boxes, hoppers, dispensers, droppers, ender chests, and more — through a configurable hotkey or an on-screen button.

The mod is **100% client-side**: it requires no server installation and never bypasses vanilla mechanics. All sorting is performed through normal inventory click packets, exactly as if you were manually moving items.

---

## Features

### Supported Containers

- Player Inventory
- Chest & Double Chest
- Barrel
- Shulker Box (all colors)
- Hopper, Dispenser, Dropper
- Ender Chest
- Crafter
- Any vanilla storage inventory
- Compatible modded containers (via generic inventory detection)

### Sorting Modes

| Mode | Description |
|------|-------------|
| **Smart (Default)** | Groups items into logical categories, then sorts alphabetically within each category |
| **By Quantity** | Largest stacks first, then alphabetical |
| **Alphabetical** | Simple A-Z sorting by item name |
| **By Item Type** | Groups by vanilla item type/namespace |
| **Blocks First** | All blocks first, then everything else |
| **Equipment First** | Armor → Weapons → Tools → Shield → other items |
| **Ores & Resources** | Prioritizes ores, ingots, gems, and related resources |
| **Custom Order** | Uses your player-defined category order from configuration |

### Smart Mode Categories (in order)

1. Building Blocks
2. Natural Blocks
3. Ores
4. Ingots
5. Gems
6. Redstone
7. Wood
8. Stone
9. Decoration
10. Food
11. Farming
12. Tools
13. Weapons
14. Armor
15. Potions
16. Utility
17. Mob Drops
18. Miscellaneous

### Hotkey

- Default key: **R** (configurable in Minecraft Controls)
- Works only when an inventory GUI is open
- Sorts **only** the inventory section under the mouse cursor:
  - Mouse over chest slots → sorts the chest
  - Mouse over player inventory slots → sorts the player inventory
  - No slot under cursor → does nothing

### GUI Button

- Small sort icon displayed inside every supported inventory GUI
- Clean vanilla-style appearance
- Position configurable (Top Left, Top Right, Bottom Left, Bottom Right)
- Toggle on/off in configuration
- Tooltip: "Sort Inventory"
- Disabled while sorting is in progress (prevents duplicate operations)
- Optional click animation

### Stack Merging

Before sorting, the mod automatically merges identical stacks to free up inventory space. Only truly identical items are merged — enchantments, durability, custom names, potion data, and all NBT/component data are respected.

---

## Configuration

Open the configuration screen via **Mod Menu** or the in-game settings.

### Sorting Options

| Option | Default | Description |
|--------|---------|-------------|
| Sorting Mode | Smart | Active sorting algorithm |
| Ascending Order | On | Sort A-Z / smallest first (off = descending) |
| Merge Stacks Before Sorting | On | Merge identical stacks before sorting |
| Remember Last Sorting Mode | On | Persist the selected mode across sessions |

### Button Options

| Option | Default | Description |
|--------|---------|-------------|
| Show Sort Button | On | Display the button in inventory GUIs |
| Button Position | Top Right | Where the button appears |
| Play Sound on Sort | On | Play a click sound when sorting |
| Animate Button | On | Brief animation on button click |

### Custom Order

When using the **Custom Order** mode, you can define the category priority list in the configuration screen. Available category names:

```
BUILDING_BLOCKS, NATURAL_BLOCKS, ORES, INGOTS, GEMS, REDSTONE, WOOD, STONE,
DECORATION, FOOD, FARMING, TOOLS, WEAPONS, ARMOR, POTIONS, UTILITY, MOB_DROPS, MISC
```

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.1.
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api).
3. *(Optional but recommended)* Install [Cloth Config API](https://modrinth.com/mod/cloth-config) for the in-game configuration screen.
4. *(Optional)* Install [Mod Menu](https://modrinth.com/mod/modmenu) to access the config screen from the mods list.
5. Place `scythe-sorting-1.0.0.jar` in your `.minecraft/mods` folder.
6. Launch Minecraft 1.21.1 with Fabric.

---

## Building from Source

### Requirements

- JDK 21+
- Internet connection (for Gradle dependency download)

### Steps

```bash
git clone https://github.com/scythesorting/scythe-sorting.git
cd scythe-sorting
./gradlew build
```

The compiled JAR will be in `build/libs/scythe-sorting-1.0.0.jar`.

---

## Project Structure

```
src/main/java/com/scythesorting/
├── client/          # ClientModInitializer entrypoint
├── config/          # Configuration (ScytheSortingConfig, ConfigScreen, ModMenuIntegration)
├── gui/             # SortButton, ButtonPosition
├── sorting/         # SortingExecutor, InventoryDetector, ItemCategory, ItemCategorizer
├── algorithm/       # All sorting algorithm implementations
├── registry/        # SortingAlgorithmRegistry
├── keybind/         # SortingKeybind
├── mixin/           # HandledScreenMixin
└── util/            # ItemStackUtil

src/main/resources/
├── fabric.mod.json
├── scythesorting.mixins.json
└── assets/scythesorting/
    ├── icon.png
    ├── lang/
    │   ├── en_us.json
    │   └── pt_br.json
    └── textures/gui/
        └── sort_button.png
```

---

## Compatibility

| Dependency | Version | Required |
|------------|---------|----------|
| Minecraft | 1.21.1 | Yes |
| Fabric Loader | ≥ 0.15.0 | Yes |
| Fabric API | 0.107.1+1.21.1 | Yes |
| Cloth Config | 15.0.140 | Optional (config screen) |
| Mod Menu | 11.0.2 | Optional (config access) |

---

## Localization

The mod ships with full localization for:

- **English (en_us)**
- **Portuguese (Brazil) (pt_br)**

All text uses language files. Contributions for additional languages are welcome.

---

## Safety & Reliability

- **No item duplication** — the sorting algorithm tracks item positions before executing any moves.
- **No item loss** — all operations use vanilla inventory click packets; the server validates every move.
- **Graceful error handling** — the mod handles container closing mid-sort, lag spikes, and unexpected slot updates without crashing.
- **Safety over speed** — the mod favors correctness over performance.

---

## License

MIT License — see [LICENSE](LICENSE) for details.
