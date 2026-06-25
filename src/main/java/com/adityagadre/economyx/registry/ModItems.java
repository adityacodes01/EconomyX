package com.adityagadre.economyx.registry;

import com.adityagadre.economyx.EconomyX;
import com.adityagadre.economyx.item.WalletItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * All EconomyX items: the six denominations of dollar bill plus a credit card. Block items are
 * registered alongside their blocks in {@link ModBlocks}, but live in this same registry.
 */
public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EconomyX.MODID);

    public static final RegistryObject<Item> ONE_DOLLAR_BILL = bill("one_dollar_bill");
    public static final RegistryObject<Item> FIVE_DOLLAR_BILL = bill("five_dollar_bill");
    public static final RegistryObject<Item> TEN_DOLLAR_BILL = bill("ten_dollar_bill");
    public static final RegistryObject<Item> TWENTY_DOLLAR_BILL = bill("twenty_dollar_bill");
    public static final RegistryObject<Item> FIFTY_DOLLAR_BILL = bill("fifty_dollar_bill");
    public static final RegistryObject<Item> HUNDRED_DOLLAR_BILL = bill("hundred_dollar_bill");
    public static final RegistryObject<Item> CREDIT_CARD = bill("credit_card");

    public static final RegistryObject<Item> WALLET = ITEMS.register("wallet",
            () -> new WalletItem(new Item.Properties().stacksTo(1).setId(ITEMS.key("wallet"))));

    private ModItems() {
    }

    /** Plain stackable currency item (stacks to 64 by default). */
    private static RegistryObject<Item> bill(String name) {
        return ITEMS.register(name,
                () -> new Item(new Item.Properties().setId(ITEMS.key(name))));
    }

    public static void register(BusGroup modBus) {
        ITEMS.register(modBus);
    }
}
