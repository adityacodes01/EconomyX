package com.adityagadre.economyx.client;

import com.adityagadre.economyx.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Client-only setup: registers the ATM screen with its menu type during client setup. */
public final class EconomyXClient {

    private EconomyXClient() {}

    public static void init(BusGroup modBus) {
        IModBusEvent.getBus(modBus, FMLClientSetupEvent.class)
                .addListener(EconomyXClient::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.ATM.get(), AtmScreen::new);
            MenuScreens.register(ModMenus.EXCHANGER.get(), ExchangerScreen::new);
        });
    }
}
