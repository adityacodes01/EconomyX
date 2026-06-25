package com.adityagadre.economyx.client;

import com.adityagadre.economyx.menu.AtmMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Custom ATM screen for MC 26.2 (extract-based GUI rendering): balance readout, a "how much cash"
 * field, and Take Cash / Give Cash buttons over a custom background sprite.
 */
public class AtmScreen extends AbstractContainerScreen<AtmMenu> {

    private static final int GUI_W = 230;
    private static final int GUI_H = 180;
    private static final Identifier BG = Identifier.fromNamespaceAndPath("economyx", "atm");

    private EditBox amountField;

    public AtmScreen(AtmMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        // Center a custom-sized panel (imageWidth/Height are final, so position manually).
        this.leftPos = (this.width - GUI_W) / 2;
        this.topPos = (this.height - GUI_H) / 2;

        this.amountField = new EditBox(this.font, this.leftPos + 121, this.topPos + 41, 98, 14,
                Component.literal("Amount"));
        this.amountField.setMaxLength(9);
        this.amountField.setHint(Component.literal("How much cash"));
        addRenderableWidget(this.amountField);

        addRenderableWidget(Button.builder(Component.literal("Take Cash"), b -> send(true))
                .bounds(this.leftPos + 120, this.topPos + 62, 100, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Give Cash"), b -> send(false))
                .bounds(this.leftPos + 8, this.topPos + 68, 96, 20).build());
    }

    private void send(boolean take) {
        int amount = parseAmount();
        if (amount <= 0) return;
        int id = take ? amount : -amount;
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
        }
    }

    private int parseAmount() {
        try {
            return Integer.parseInt(this.amountField.getValue().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BG, this.leftPos, this.topPos, GUI_W, GUI_H);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        // Pose is already translated to (leftPos, topPos); coordinates are GUI-relative. Colors are ARGB.
        graphics.text(this.font, Component.literal("ATM"), 8, 5, 0xFFE0E0E0, false);
        graphics.text(this.font, Component.literal("You have:"), 12, 26, 0xFF404040, false);
        graphics.text(this.font, Component.literal("$" + this.menu.getDisplayBalance()), 14, 38, 0xFF33CC33, false);
        graphics.text(this.font, Component.literal("Take Cash"), 132, 30, 0xFF404040, false);
        graphics.text(this.font, Component.literal("Give Cash"), 10, 58, 0xFF404040, false);
    }
}
