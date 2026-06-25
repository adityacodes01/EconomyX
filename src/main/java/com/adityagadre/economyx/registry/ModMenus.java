package com.adityagadre.economyx.registry;

import com.adityagadre.economyx.EconomyX;
import com.adityagadre.economyx.menu.AtmMenu;
import com.adityagadre.economyx.menu.ExchangerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, EconomyX.MODID);

    public static final RegistryObject<MenuType<AtmMenu>> ATM =
            MENUS.register("atm",
                    () -> IForgeMenuType.create((id, inv, buf) -> new AtmMenu(id, inv, buf)));

    public static final RegistryObject<MenuType<ExchangerMenu>> EXCHANGER =
            MENUS.register("exchanger",
                    () -> IForgeMenuType.create((id, inv, buf) -> new ExchangerMenu(id, inv, buf)));

    private ModMenus() {}

    public static void register(BusGroup modBus) {
        MENUS.register(modBus);
    }
}
