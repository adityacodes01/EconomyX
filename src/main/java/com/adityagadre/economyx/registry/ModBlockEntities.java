package com.adityagadre.economyx.registry;

import com.adityagadre.economyx.EconomyX;
import com.adityagadre.economyx.block.entity.AtmBlockEntity;
import com.adityagadre.economyx.block.entity.CashRegisterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EconomyX.MODID);

    public static final RegistryObject<BlockEntityType<CashRegisterBlockEntity>> CASH_REGISTER =
            BLOCK_ENTITIES.register("cash_register",
                    () -> new BlockEntityType<>(
                            CashRegisterBlockEntity::new,
                            Set.of(ModBlocks.CASH_REGISTER.get())));

    public static final RegistryObject<BlockEntityType<AtmBlockEntity>> ATM =
            BLOCK_ENTITIES.register("atm",
                    () -> new BlockEntityType<>(
                            AtmBlockEntity::new,
                            Set.of(ModBlocks.ATM.get())));

    private ModBlockEntities() {
    }

    public static void register(BusGroup modBus) {
        BLOCK_ENTITIES.register(modBus);
    }
}
