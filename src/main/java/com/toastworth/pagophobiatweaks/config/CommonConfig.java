package com.toastworth.pagophobiatweaks.config;

import com.toastworth.pagophobiatweaks.PagophobiaTweaks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = PagophobiaTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonConfig {
    public static int primalWinterMaxSnowAccumulationLayers;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.IntValue PRIMAL_WINTER_MAX_SNOW_ACCUMULATION_LAYERS = BUILDER
            .comment("Primal Winter: Limits the layer count of naturally accumulating snow.")
            .defineInRange("primalWinterMaxSnowAccumulationLayers", 5, 1, 8);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if(event.getConfig().getSpec() != SPEC) return;
        primalWinterMaxSnowAccumulationLayers = PRIMAL_WINTER_MAX_SNOW_ACCUMULATION_LAYERS.get();
    }
}
