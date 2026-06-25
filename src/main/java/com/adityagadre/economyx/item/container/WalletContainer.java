package com.adityagadre.economyx.item.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.core.component.DataComponents;

/**
 * A 9-slot {@link Container} backed by the wallet {@link ItemStack}. Reads from and writes through
 * to the stack's vanilla container component, so storing/removing items persists on the wallet
 * with no custom data component or block entity required.
 */
public class WalletContainer implements Container {

    private static final int SIZE = 9;
    private final ItemStack wallet;
    private final NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public WalletContainer(ItemStack wallet) {
        this.wallet = wallet;
        ItemContainerContents contents = wallet.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(this.items);
    }

    private void save() {
        this.wallet.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
    }

    @Override public int getContainerSize() { return SIZE; }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override public ItemStack getItem(int slot) { return this.items.get(slot); }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) save();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ContainerHelper.takeItem(this.items, slot);
        save();
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        save();
    }

    @Override
    public boolean stillValid(Player player) {
        // Valid while the player still carries the wallet stack.
        return player.getInventory().contains(this.wallet);
    }

    @Override public void setChanged() { save(); }

    @Override public void clearContent() { this.items.clear(); save(); }

    // A wallet shouldn't nest itself.
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return !(stack.getItem() instanceof com.adityagadre.economyx.item.WalletItem);
    }
}
