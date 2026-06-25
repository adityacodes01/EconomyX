package com.adityagadre.economyx.client;

import com.adityagadre.economyx.menu.ExchangerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/** Custom Currency Exchanger screen: input slot, total value read-out, and denomination buttons. */
public class ExchangerScreen extends AbstractContainerScreen<ExchangerMenu> {

    private static final int GUI_W = 230;
    private static final int GUI_H = 196;
    private static final Identifier BG = Identifier.fromNamespaceAndPath("economyx", "exchanger");

    private static final long[] DENOMS = {1, 5, 10, 20, 50, 100};

    public ExchangerScreen(ExchangerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - GUI_W) / 2;
        this.topPos = (this.height - GUI_H) / 2;

        // 6 denomination buttons in a 3x2 grid.
        int[] colX = {8, 82, 156};
        int[] rowY = {66, 88};
        for (int i = 0; i < DENOMS.length; i++) {
            long denom = DENOMS[i];
            int x = this.leftPos + colX[i % 3];
            int y = this.topPos + rowY[i / 3];
            addRenderableWidget(Button.builder(Component.literal("$" + denom), b -> exchange(denom))
                    .bounds(x, y, 66, 18).build());
        }
    }

    private void exchange(long denom) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, (int) denom);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BG, this.leftPos, this.topPos, GUI_W, GUI_H);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, Component.literal("Currency Exchanger"), 8, 6, 0xFFE0E0E0, false);
        graphics.text(this.font, Component.literal("Insert bills"), 36, 22, 0xFF404040, false);
        graphics.text(this.font, Component.literal("Value: $" + this.menu.getInsertedValue()), 36, 36, 0xFF33CC33, false);
        graphics.text(this.font, Component.literal("Exchange to:"), 8, 56, 0xFF404040, false);
    }
}
