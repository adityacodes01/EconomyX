package com.adityagadre.economyx.registry;

import com.adityagadre.economyx.EconomyX;
import com.adityagadre.economyx.block.AtmBlock;
import com.adityagadre.economyx.block.CashRegisterBlock;
import com.adityagadre.economyx.block.CurrencyExchangerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

/**
 * All EconomyX blocks. Each block is registered together with a matching {@link BlockItem} so it
 * can be carried and placed. Models are supplied as Blockbench JSON resources.
 */
public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EconomyX.MODID);

    public static final RegistryObject<Block> ATM = registerBlock("atm",
            name -> new AtmBlock(metalProps(name)));
    public static final RegistryObject<Block> CASH_REGISTER = registerBlock("cash_register",
            name -> new CashRegisterBlock(metalProps(name)));
    public static final RegistryObject<Block> CURRENCY_EXCHANGER = registerBlock("currency_exchanger",
            name -> new CurrencyExchangerBlock(metalProps(name)));

    private ModBlocks() {
    }

    private static BlockBehaviour.Properties metalProps(String name) {
        // Forge 65: registered blocks must carry their ResourceKey in Properties via setId.
        // Non-full custom models also need noOcclusion so neighbouring faces still render.
        return BlockBehaviour.Properties.of()
                .setId(BLOCKS.key(name))
                .mapColor(MapColor.METAL)
                .strength(3.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .noOcclusion();
    }

    private static RegistryObject<Block> registerBlock(String name, Function<String, Block> factory) {
        RegistryObject<Block> registered = BLOCKS.register(name, () -> factory.apply(name));
        ModItems.ITEMS.register(name, () -> new BlockItem(registered.get(),
                new Item.Properties().setId(ModItems.ITEMS.key(name))));
        return registered;
    }

    public static void register(BusGroup modBus) {
        BLOCKS.register(modBus);
    }
}
