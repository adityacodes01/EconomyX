package com.adityagadre.economyx.registry;

import com.adityagadre.economyx.EconomyX;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EconomyX.MODID);

    public static final RegistryObject<SoundEvent> CASH_REGISTER_OPEN =
            SOUND_EVENTS.register("cash_register_open",
                    () -> SoundEvent.createVariableRangeEvent(EconomyX.id("cash_register_open")));

    private ModSounds() {
    }

    public static void register(BusGroup modBus) {
        SOUND_EVENTS.register(modBus);
    }
}
