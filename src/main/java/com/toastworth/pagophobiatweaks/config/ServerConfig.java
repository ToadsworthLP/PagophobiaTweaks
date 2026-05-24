package com.toastworth.pagophobiatweaks.config;

import com.toastworth.pagophobiatweaks.PagophobiaTweaks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = PagophobiaTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {
    public static double primalWinterFogDensityServerMultiplier;
    public static boolean vanillaShowSkyDuringBadWeather;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue PRIMAL_WINTER_FOG_DENSITY_SERVER_MULTIPLIER = BUILDER
            .comment("Primal Winter: Server-side multiplier for the 'fogDensity' option in Primal Winter's config.")
            .defineInRange("primalWinterFogDensityServerOverride", 1.0d, 0.0d, Double.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue VANILLA_SHOW_SKY_DURING_BAD_WEATHER = BUILDER
            .comment("Vanilla: If enabled, shows the sun, moon and stars even while it's raining/snowing.")
            .define("vanillaShowSkyDuringBadWeather", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if(event.getConfig().getSpec() != SPEC) return;
        primalWinterFogDensityServerMultiplier = PRIMAL_WINTER_FOG_DENSITY_SERVER_MULTIPLIER.get();
        vanillaShowSkyDuringBadWeather = VANILLA_SHOW_SKY_DURING_BAD_WEATHER.get();
    }
}