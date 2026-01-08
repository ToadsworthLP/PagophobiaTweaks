package com.toastworth.pagophobiatweaks.mixin;

import com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.util.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = com.alcatrazescapee.primalwinter.util.EventHandler.class, remap = false)
public class PrimalWinterEventHandlerMixin {
    /**
     * @author Toastworth
     * @reason Patches in ability to configure maximum snow layer count for Primal Winter's snow accumulation feature
     */
    @Overwrite
    public static void placeExtraSnow(ServerLevel level, ChunkAccess chunk)
    {
        if (Config.INSTANCE.enableSnowAccumulationDuringWeather.getAsBoolean() && level.random.nextInt(16) == 0)
        {
            final int blockX = chunk.getPos().getMinBlockX();
            final int blockZ = chunk.getPos().getMinBlockZ();
            final BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(blockX, 0, blockZ, 15));
            final BlockState state = level.getBlockState(pos);
            final Biome biome = level.getBiome(pos).value();
            if (level.isRaining() && biome.coldEnoughToSnow(pos) && level.getBrightness(LightLayer.BLOCK, pos) < 10)
            {
                if (state.getBlock() == Blocks.SNOW)
                {
                    // Stack snow layers
                    final int layers = state.getValue(BlockStateProperties.LAYERS);
                    if (layers < com.toastworth.pagophobiatweaks.Config.primalWinterMaxSnowAccumulationLayers)
                    {
                        level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LAYERS, 1 + layers));
                    }

                    final BlockPos belowPos = pos.below();
                    final BlockState belowState = level.getBlockState(belowPos);
                    final Block replacementBlock = PrimalWinterBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(belowState.getBlock(), () -> null).get();
                    if (replacementBlock != null)
                    {
                        level.setBlockAndUpdate(belowPos, replacementBlock.defaultBlockState());
                    }
                }
            }
        }
    }
}
