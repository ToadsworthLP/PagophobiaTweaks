package com.toastworth.pagophobiatweaks.mixin;

import com.alcatrazescapee.primalwinter.platform.client.FogDensityCallback;
import com.alcatrazescapee.primalwinter.util.Config;
import com.toastworth.pagophobiatweaks.config.ServerConfig;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = com.alcatrazescapee.primalwinter.client.ClientEventHandler.class, remap = false)
public class PrimalWinterClientEventHandlerMixin {
    @Accessor("prevFogDensity")
    private static float getPrevFogDensity() { throw new AssertionError(); }
    @Accessor("prevFogDensity")
    private static void setPrevFogDensity(float value) { throw new AssertionError(); }
    @Accessor("prevFogTick")
    private static long getPrevFogTick() { throw new AssertionError(); }
    @Accessor("prevFogTick")
    private static void setPrevFogTick(long value) { throw new AssertionError(); }

    /**
     * @author Toastworth
     * @reason Patches in the ability to configure the density of Primal Winter's fog.
     */
    @Overwrite
    public static void renderFogDensity(Camera camera, FogDensityCallback callback) {
        Entity var3 = camera.getEntity();
        if (var3 instanceof Player player) {
            long thisTick = Util.getMillis();
            boolean firstTick = getPrevFogTick() == -1L;
            float deltaTick = firstTick ? 1.0E10F : (float)(thisTick - getPrevFogTick()) * 1.5E-4F;
            setPrevFogTick(thisTick);
            float expectedFogDensity = 0.0F;
            Level level = player.level();
            Biome biome = (Biome)level.getBiome(camera.getBlockPosition()).value();
            if (level.isRaining() && biome.coldEnoughToSnow(camera.getBlockPosition())) {
                int light = level.getBrightness(LightLayer.SKY, BlockPos.containing(player.getEyePosition()));
                expectedFogDensity = Mth.clampedMap((float)light, 0.0F, 15.0F, 0.0F, 1.0F);
            }

            if (expectedFogDensity > getPrevFogDensity()) {
                setPrevFogDensity(Math.min(getPrevFogDensity() + 4.0F * deltaTick, expectedFogDensity));
            } else if (expectedFogDensity < getPrevFogDensity()) {
                setPrevFogDensity(Math.max(getPrevFogDensity() - deltaTick, expectedFogDensity));
            }

            if (camera.getFluidInCamera() != FogType.NONE) {
                setPrevFogDensity(-1.0F);
                setPrevFogTick(-1L);
            }

            if (getPrevFogDensity() > 0.0F) {
                float scaledDelta = 1.0F - (1.0F - getPrevFogDensity()) * (1.0F - getPrevFogDensity());
                float fogDensity = Config.INSTANCE.fogDensity.getAsFloat() * (float)ServerConfig.primalWinterFogDensityServerMultiplier;
                float farPlaneScale = Mth.lerp(scaledDelta, 1.0F, fogDensity);
                float nearPlaneScale = Mth.lerp(scaledDelta, 1.0F, 0.3F * fogDensity);
                callback.accept(nearPlaneScale, farPlaneScale);
            }
        }
    }
}
