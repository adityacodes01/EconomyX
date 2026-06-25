package com.adityagadre.economyx.block.entity;

import com.adityagadre.economyx.menu.AtmMenu;
import com.adityagadre.economyx.registry.ModBlockEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Holds a persistent dollar balance for an ATM. Stored via the block-entity ValueIO so it saves
 * with the world. Players deposit and withdraw through {@link com.adityagadre.economyx.block.AtmBlock}.
 */
public class AtmBlockEntity extends BlockEntity implements MenuProvider {

    private long balance;

    public AtmBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ATM.get(), pos, state);
    }

    public long getBalance() {
        return this.balance;
    }

    public void deposit(long amount) {
        this.balance += amount;
        setChanged();
    }

    /** Removes up to {@code amount} from the balance and returns how much was actually withdrawn. */
    public long withdraw(long amount) {
        long taken = Math.min(amount, this.balance);
        this.balance -= taken;
        setChanged();
        return taken;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Balance", this.balance);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.balance = input.getLongOr("Balance", 0L);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.economyx.atm");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AtmMenu(id, playerInventory, this);
    }
}
