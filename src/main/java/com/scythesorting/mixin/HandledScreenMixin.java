package com.scythesorting.mixin;

import com.scythesorting.config.ScytheSortingConfig;
import com.scythesorting.gui.ButtonPosition;
import com.scythesorting.gui.SortButton;
import com.scythesorting.sorting.HandledScreenAccessor;
import com.scythesorting.sorting.InventoryDetector;
import com.scythesorting.sorting.SortingExecutor;
import com.scythesorting.keybind.SortingKeybind;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin targeting {@link HandledScreen} to:
 * <ul>
 *   <li>Inject the sort button into every inventory GUI.</li>
 *   <li>Handle the sort hotkey, routing it to the inventory section under the cursor.</li>
 *   <li>Expose the protected {@code x} and {@code y} GUI offsets via {@link HandledScreenAccessor}.</li>
 * </ul>
 */
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin implements HandledScreenAccessor {

    @Shadow protected int x;
    @Shadow protected int y;
    @Shadow protected int backgroundWidth;
    @Shadow protected int backgroundHeight;

    /** The sort button instance for this screen. */
    private SortButton scythesorting$sortButton;

    // -------------------------------------------------------------------------
    // HandledScreenAccessor implementation
    // -------------------------------------------------------------------------

    @Override
    public int scythesorting$getX() {
        return this.x;
    }

    @Override
    public int scythesorting$getY() {
        return this.y;
    }

    // -------------------------------------------------------------------------
    // Button injection: called after the screen is initialized
    // -------------------------------------------------------------------------

    @Inject(method = "init", at = @At("TAIL"))
    private void scythesorting$onInit(CallbackInfo ci) {
        ScytheSortingConfig config = ScytheSortingConfig.getInstance();
        if (!config.showSortButton) return;

        HandledScreen<?> self = (HandledScreen<?>) (Object) this;
        ButtonPosition position = config.buttonPosition;

        int btnX = SortButton.computeX(this.x, this.backgroundWidth, position);
        int btnY = SortButton.computeY(this.y, this.backgroundHeight, position);

        scythesorting$sortButton = new SortButton(self, btnX, btnY);
    }

    // -------------------------------------------------------------------------
    // Render: draw the sort button on top of the inventory GUI
    // -------------------------------------------------------------------------

    @Inject(method = "render", at = @At("TAIL"))
    private void scythesorting$onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (scythesorting$sortButton != null) {
            scythesorting$sortButton.render(context, mouseX, mouseY, delta);
        }
    }

    // -------------------------------------------------------------------------
    // Mouse click: forward to the sort button
    // -------------------------------------------------------------------------

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void scythesorting$onMouseClicked(double mouseX, double mouseY, int button,
                                               CallbackInfoReturnable<Boolean> cir) {
        if (scythesorting$sortButton != null
                && scythesorting$sortButton.isMouseOver(mouseX, mouseY)
                && button == 0) {
            scythesorting$sortButton.onClick(mouseX, mouseY);
            cir.setReturnValue(true);
        }
    }

    // -------------------------------------------------------------------------
    // Hotkey handling: called on every key press while the screen is open
    // -------------------------------------------------------------------------

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void scythesorting$onKeyPressed(int keyCode, int scanCode, int modifiers,
                                             CallbackInfoReturnable<Boolean> cir) {
        if (!SortingKeybind.wasPressed()) return;
        if (SortingExecutor.isSorting()) return;

        HandledScreen<?> self = (HandledScreen<?>) (Object) this;

        // Detect which inventory section is under the mouse cursor
        double mouseX = net.minecraft.client.MinecraftClient.getInstance().mouse.getX()
                / net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaleFactor();
        double mouseY = net.minecraft.client.MinecraftClient.getInstance().mouse.getY()
                / net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaleFactor();

        InventoryDetector.InventorySection section =
                InventoryDetector.detectFromMouse(self, mouseX, mouseY);

        if (section != null) {
            SortingExecutor.sort(self, section);
            cir.setReturnValue(true);
        }
    }
}
