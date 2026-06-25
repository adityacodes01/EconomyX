package com.adityagadre.economyx;

import com.adityagadre.economyx.registry.ModBlocks;
import com.adityagadre.economyx.registry.ModBlockEntities;
import com.adityagadre.economyx.registry.ModCreativeTabs;
import com.adityagadre.economyx.registry.ModItems;
import com.adityagadre.economyx.registry.ModMenus;
import com.adityagadre.economyx.registry.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

/**
 * EconomyX — a money and banking mod (dollar bills, ATM, currency exchanger, cash register, vault).
 *
 * <p>This build registers all currency items and all blocks (placeable, using the supplied
 * Blockbench models). Interactive GUIs and block-entity logic are layered on top in later builds.</p>
 */
@Mod(EconomyX.MODID)
public final class EconomyX {

    public static final String MODID = "economyx";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EconomyX(FMLJavaModLoadingContext context) {
        BusGroup modBus = context.getModBusGroup();

        ModBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        ModSounds.register(modBus);
        ModItems.register(modBus);
        ModCreativeTabs.register(modBus);
        ModMenus.register(modBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.adityagadre.economyx.client.EconomyXClient.init(modBus);
        }

        LOGGER.info("[EconomyX] Initialised.");
    }

    /** Builds an economyx-namespaced resource location. */
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
