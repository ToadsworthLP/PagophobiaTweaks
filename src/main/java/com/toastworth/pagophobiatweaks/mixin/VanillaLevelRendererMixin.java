package com.toastworth.pagophobiatweaks.mixin;

import com.toastworth.pagophobiatweaks.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = net.minecraft.client.renderer.LevelRenderer.class)
public class VanillaLevelRendererMixin {
    @ModifyVariable(
            method = "renderSky",
            at = @At(value = "STORE"),
            name = "f11")
    private float showStarsDuringBadWeather(float f11) {
        if(ServerConfig.vanillaShowSkyDuringBadWeather) {
            return 1.0f;
        } else {
            return f11;
        }
    }
}
