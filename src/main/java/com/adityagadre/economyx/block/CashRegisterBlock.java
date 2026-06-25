package com.adityagadre.economyx.block;

import com.adityagadre.economyx.block.entity.CashRegisterBlockEntity;
import com.adityagadre.economyx.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Cash Register — a storage block that opens a 27-slot chest-style GUI and plays a register sound
 * when opened. Storage is held by {@link CashRegisterBlockEntity} and persists via block-entity NBT.
 */
public class CashRegisterBlock extends Block implements EntityBlock {

    public CashRegisterBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CashRegisterBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CashRegisterBlockEntity register) {
                // Play the register sound for everyone nearby, then open the chest GUI.
                level.playSound(null, pos, ModSounds.CASH_REGISTER_OPEN.get(),
                        SoundSource.BLOCKS, 1.0F, 1.0F);
                serverPlayer.openMenu(register, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
