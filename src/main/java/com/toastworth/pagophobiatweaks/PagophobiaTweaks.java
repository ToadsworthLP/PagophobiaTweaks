package com.toastworth.pagophobiatweaks;

import com.mojang.logging.LogUtils;
import com.toastworth.pagophobiatweaks.config.CommonConfig;
import com.toastworth.pagophobiatweaks.config.ServerConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PagophobiaTweaks.MODID)
public class PagophobiaTweaks {

    public static final String MODID = "pagophobiatweaks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PagophobiaTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Primal Winter patch - MaxSnowAccumulationLayers: " + CommonConfig.primalWinterMaxSnowAccumulationLayers);
    }
}
