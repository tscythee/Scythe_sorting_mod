package com.scythesorting.gui;

import com.scythesorting.config.ScytheSortingConfig;
import com.scythesorting.sorting.InventoryDetector;
import com.scythesorting.sorting.SortingExecutor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * The on-screen sort button rendered inside inventory GUIs.
 *
 * <p>The button is positioned relative to the GUI background according to the
 * {@link ButtonPosition} setting in the configuration. It displays a small sort icon
 * and shows a tooltip on hover. While sorting is in progress, the button is disabled
 * to prevent duplicate operations.</p>
 */
public class SortButton extends ButtonWidget {

    /** Texture atlas for the sort button icon (16x16 sprite). */
    private static final Identifier BUTTON_TEXTURE =
            Identifier.of("scythesorting", "textures/gui/sort_button.png");

    /** Button size in pixels. */
    public static final int SIZE = 16;

    private final HandledScreen<?> screen;
    private boolean animating = false;
    private int animationTick = 0;
    private static final int ANIMATION_DURATION = 6;

    /**
     * Creates a new sort button for the given screen.
     *
     * @param screen the inventory screen this button belongs to
     * @param x      the absolute X position on screen
     * @param y      the absolute Y position on screen
     */
    public SortButton(HandledScreen<?> screen, int x, int y) {
        super(x, y, SIZE, SIZE,
                Text.translatable("scythesorting.button.sort"),
                button -> {
                    if (!SortingExecutor.isSorting()) {
                        triggerSort(screen);
                    }
                },
                ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.screen = screen;
    }

    /**
     * Triggers a sort operation on the container inventory of the current screen.
     * The button always sorts the container (not the player inventory).
     */
    private static void triggerSort(HandledScreen<?> screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Sort the container inventory (first section of the screen handler)
        InventoryDetector.InventorySection section =
                InventoryDetector.detectFromSlot(screen.getScreenHandler(),
                        screen.getScreenHandler().slots.get(0));

        if (section != null) {
            SortingExecutor.sort(screen, section);
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Disable while sorting
        this.active = !SortingExecutor.isSorting();

        // Determine render brightness based on state
        float brightness = this.active ? (this.isHovered() ? 1.0f : 0.85f) : 0.5f;

        // Animation: slight scale pulse when clicked
        ScytheSortingConfig config = ScytheSortingConfig.getInstance();
        if (config.animateButton && animating) {
            animationTick++;
            if (animationTick >= ANIMATION_DURATION) {
                animating = false;
                animationTick = 0;
            }
        }

        int renderX = this.getX();
        int renderY = this.getY();

        // Draw button background (vanilla button style)
        int color = this.active ? (this.isHovered() ? 0xFFFFFFFF : 0xFFAAAAAA) : 0xFF666666;
        context.fill(renderX - 1, renderY - 1, renderX + SIZE + 1, renderY + SIZE + 1, 0xFF000000);
        context.fill(renderX, renderY, renderX + SIZE, renderY + SIZE, color);

        // Draw sort icon texture
        context.drawTexture(BUTTON_TEXTURE, renderX, renderY, 0, 0, SIZE, SIZE, SIZE, SIZE);

        // Draw tooltip on hover
        if (this.isHovered()) {
            context.drawTooltip(
                    MinecraftClient.getInstance().textRenderer,
                    Text.translatable("scythesorting.button.sort.tooltip"),
                    mouseX, mouseY);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        ScytheSortingConfig config = ScytheSortingConfig.getInstance();
        if (config.animateButton) {
            animating = true;
            animationTick = 0;
        }
    }

    /**
     * Computes the X position of the sort button based on the configured position.
     *
     * @param guiLeft  the left edge of the GUI background
     * @param guiWidth the width of the GUI background
     * @param position the configured button position
     * @return the absolute X coordinate
     */
    public static int computeX(int guiLeft, int guiWidth, ButtonPosition position) {
        return switch (position) {
            case TOP_LEFT, BOTTOM_LEFT -> guiLeft + 4;
            case TOP_RIGHT, BOTTOM_RIGHT -> guiLeft + guiWidth - SIZE - 4;
        };
    }

    /**
     * Computes the Y position of the sort button based on the configured position.
     *
     * @param guiTop    the top edge of the GUI background
     * @param guiHeight the height of the GUI background
     * @param position  the configured button position
     * @return the absolute Y coordinate
     */
    public static int computeY(int guiTop, int guiHeight, ButtonPosition position) {
        return switch (position) {
            case TOP_LEFT, TOP_RIGHT -> guiTop + 4;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> guiTop + guiHeight - SIZE - 4;
        };
    }
}
