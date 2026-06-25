package com.adityagadre.economyx.item;

import com.adityagadre.economyx.item.container.WalletContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Wallet — a portable 9-slot storage item. Right-click to open a one-row chest interface; the
 * contents are saved on the item itself (vanilla container component), so they travel with it.
 */
public class WalletItem extends Item {

    public WalletItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            WalletContainer container = new WalletContainer(stack);
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new ChestMenu(MenuType.GENERIC_9x1, id, inv, container, 1),
                    Component.translatable("item.economyx.wallet")));
        }
        return InteractionResult.SUCCESS;
    }
}
