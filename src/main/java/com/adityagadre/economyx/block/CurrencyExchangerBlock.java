package com.adityagadre.economyx.block;

import com.adityagadre.economyx.menu.ExchangerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Currency Exchanger — opens a custom money-changing screen: insert bills, then convert their
 * total value into any denomination you choose.
 */
public class CurrencyExchangerBlock extends Block {

    private static final Component TITLE = Component.translatable("block.economyx.currency_exchanger");

    public CurrencyExchangerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new ExchangerMenu(id, inv, ContainerLevelAccess.create(level, pos)),
                    TITLE));
        }
        return InteractionResult.SUCCESS;
    }
}
