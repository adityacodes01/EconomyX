package com.adityagadre.economyx.menu;

import com.adityagadre.economyx.block.entity.AtmBlockEntity;
import com.adityagadre.economyx.registry.ModBlocks;
import com.adityagadre.economyx.registry.ModMenus;
import com.adityagadre.economyx.util.MoneyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server/client menu for the ATM. Holds the player inventory slots, a synced balance data-slot for
 * the client display, and handles Take/Give actions via {@link #clickMenuButton} (no custom packets:
 * the typed amount is sent as the button id — positive = take, negative = give).
 */
public class AtmMenu extends AbstractContainerMenu {

    private final AtmBlockEntity atm;
    private final ContainerLevelAccess access;
    private int syncedBalance;

    /** Server-side constructor (from the block entity's MenuProvider). */
    public AtmMenu(int id, Inventory playerInv, AtmBlockEntity atm) {
        super(ModMenus.ATM.get(), id);
        this.atm = atm;
        this.access = (atm != null && atm.getLevel() != null)
                ? ContainerLevelAccess.create(atm.getLevel(), atm.getBlockPos())
                : ContainerLevelAccess.NULL;
        addPlayerInventory(playerInv);
        this.addDataSlot(new DataSlot() {
            @Override public int get() {
                return atm != null ? (int) Math.min(Integer.MAX_VALUE, atm.getBalance()) : 0;
            }
            @Override public void set(int value) {
                AtmMenu.this.syncedBalance = value;
            }
        });
    }

    /** Client-side constructor (via IForgeMenuType.create); reads the ATM position from the buffer. */
    public AtmMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, clientAtm(playerInv, buf.readBlockPos()));
    }

    private static AtmBlockEntity clientAtm(Inventory inv, BlockPos pos) {
        if (inv.player.level().getBlockEntity(pos) instanceof AtmBlockEntity atm) return atm;
        return null;
    }

    /** Balance to show on the client screen. */
    public int getDisplayBalance() {
        return this.syncedBalance;
    }

    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 96 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 154));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.atm == null || id == 0) return false;
        boolean take = id > 0;
        long amount = Math.abs((long) id);

        if (take) {
            if (this.atm.getBalance() < amount) {
                message(player, "Insufficient balance.");
                return false;
            }
            this.atm.withdraw(amount);
            MoneyUtil.giveBills(player, amount);
            message(player, "Withdrew $" + amount + ". Balance: $" + this.atm.getBalance());
        } else {
            if (MoneyUtil.totalBillValue(player) < amount) {
                message(player, "Not enough bills to deposit $" + amount + ".");
                return false;
            }
            long removed = MoneyUtil.removeBills(player, amount);
            long change = removed - amount;
            if (change > 0) MoneyUtil.giveBills(player, change); // refund overpay
            this.atm.deposit(amount);
            message(player, "Deposited $" + amount + ". Balance: $" + this.atm.getBalance());
        }
        broadcastChanges();
        return true;
    }

    private static void message(Player player, String text) {
        player.sendSystemMessage(Component.literal(text));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // no container slots to shift items into
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.ATM.get());
    }
}
