package com.adityagadre.economyx.menu;

import com.adityagadre.economyx.registry.ModBlocks;
import com.adityagadre.economyx.registry.ModMenus;
import com.adityagadre.economyx.util.MoneyUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Currency Exchanger menu: a single input slot where bills are inserted, a synced "total value"
 * read-out, and six denomination buttons (sent as the button id). Clicking a denomination converts
 * the inserted value into that denomination, returning any remainder as change.
 */
public class ExchangerMenu extends AbstractContainerMenu {

    private static final int INPUT_X = 12;
    private static final int INPUT_Y = 30;

    private final Container input = new SimpleContainer(1) {
        @Override public void setChanged() {
            super.setChanged();
            ExchangerMenu.this.slotsChanged(this);
        }
    };
    private final ContainerLevelAccess access;
    private int syncedValue;

    /** Server-side constructor. */
    public ExchangerMenu(int id, Inventory playerInv, ContainerLevelAccess access) {
        super(ModMenus.EXCHANGER.get(), id);
        this.access = access;

        addSlot(new Slot(this.input, 0, INPUT_X, INPUT_Y) {
            @Override public boolean mayPlace(ItemStack stack) {
                return MoneyUtil.billValues().containsKey(stack.getItem());
            }
        });
        addPlayerInventory(playerInv);

        this.addDataSlot(new DataSlot() {
            @Override public int get() {
                return (int) Math.min(Integer.MAX_VALUE, MoneyUtil.valueOf(input.getItem(0)));
            }
            @Override public void set(int value) {
                ExchangerMenu.this.syncedValue = value;
            }
        });
    }

    /** Client-side constructor (via IForgeMenuType.create). */
    public ExchangerMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, ContainerLevelAccess.NULL);
    }

    /** Inserted bills' total value, for the client read-out. */
    public int getInsertedValue() {
        return this.syncedValue;
    }

    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 114 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 172));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        long target = id; // button id == target denomination value
        if (target <= 0) return false;
        Item targetItem = MoneyUtil.billForValue(target);
        if (targetItem == null) return false;

        ItemStack in = this.input.getItem(0);
        long value = MoneyUtil.valueOf(in);
        if (value <= 0) return false;

        this.input.setItem(0, ItemStack.EMPTY);
        long count = value / target;
        long remainder = value % target;
        MoneyUtil.giveStacks(player, targetItem, count);
        MoneyUtil.giveBills(player, remainder); // change in largest fitting bills
        broadcastChanges();
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index == 0) {
                // input -> player inventory
                if (!moveItemStackTo(stack, 1, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if (MoneyUtil.billValues().containsKey(stack.getItem())) {
                // player inventory bills -> input slot
                if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> clearContainer(player, this.input));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.CURRENCY_EXCHANGER.get());
    }
}
