package com.yungnickyoung.minecraft.betterdungeons.world.processor;


import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A patchwork fix for https://bugs.mojang.com/browse/MC-130584.
 */
@MethodsReturnNonnullByDefault
public class WaterloggedProcessor extends StructureProcessor {
    public static final WaterloggedProcessor INSTANCE = new WaterloggedProcessor();
    public static final Codec<WaterloggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
        Block block = blockInfoGlobal.state.getBlock();

        // Check if block is waterloggable and not intended to be waterlogged
        if (blockInfoGlobal.state.hasProperty(BlockStateProperties.WATERLOGGED) && !blockInfoGlobal.state.get(BlockStateProperties.WATERLOGGED)) {
            IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            if (worldReader.getFluidState(blockInfoGlobal.pos).isTagged(FluidTags.WATER)) {
                currentChunk.setBlockState(blockInfoGlobal.pos, Blocks.STONE_BRICKS.getDefaultState(), false);
            }

            // Remove water in adjacent blocks
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (Direction direction : Direction.values()) {
                mutable.setPos(blockInfoGlobal.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunk = worldReader.getChunk(mutable);
                    currentChunkPos = new ChunkPos(mutable);
                }

                if (currentChunk.getFluidState(mutable).isTagged(FluidTags.WATER)) {
                    if (!(currentChunk.getBlockState(mutable).getBlock() instanceof IWaterLoggable && currentChunk.getBlockState(mutable).get(BlockStateProperties.WATERLOGGED))) {
                        currentChunk.setBlockState(mutable, Blocks.STONE_BRICKS.getDefaultState(), false);
                    }
                }
            }
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.WATERLOGGED_PROCESSOR;
    }
}