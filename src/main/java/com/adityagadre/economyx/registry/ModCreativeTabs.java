package com.adityagadre.economyx.registry;

import com.adityagadre.economyx.EconomyX;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EconomyX.MODID);

    public static final RegistryObject<CreativeModeTab> ECONOMYX_TAB =
            CREATIVE_MODE_TABS.register("economyx", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + EconomyX.MODID))
                    .icon(() -> new ItemStack(ModItems.HUNDRED_DOLLAR_BILL.get()))
                    .displayItems((parameters, output) -> {
                        // Currency
                        output.accept(ModItems.ONE_DOLLAR_BILL.get());
                        output.accept(ModItems.FIVE_DOLLAR_BILL.get());
                        output.accept(ModItems.TEN_DOLLAR_BILL.get());
                        output.accept(ModItems.TWENTY_DOLLAR_BILL.get());
                        output.accept(ModItems.FIFTY_DOLLAR_BILL.get());
                        output.accept(ModItems.HUNDRED_DOLLAR_BILL.get());
                        output.accept(ModItems.CREDIT_CARD.get());
                        output.accept(ModItems.WALLET.get());
                        // Blocks
                        output.accept(ModBlocks.ATM.get());
                        output.accept(ModBlocks.CASH_REGISTER.get());
                        output.accept(ModBlocks.CURRENCY_EXCHANGER.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(BusGroup modBus) {
        CREATIVE_MODE_TABS.register(modBus);
    }
}
