package com.yungnickyoung.minecraft.betterdungeons.world.processor;


import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

/**
 * A patchwork fix for https://bugs.mojang.com/browse/MC-130584.
 */
public class WaterloggedProcessor extends StructureProcessor {
    public static final WaterloggedProcessor INSTANCE = new WaterloggedProcessor();
    public static final Codec<WaterloggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);

        // Check if block is waterloggable and not intended to be waterlogged
        if (blockInfoGlobal.state.contains(Properties.WATERLOGGED) && !blockInfoGlobal.state.get(Properties.WATERLOGGED)) {
            Chunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            if (world.getFluidState(blockInfoGlobal.pos).isIn(FluidTags.WATER)) {
                currentChunk.setBlockState(blockInfoGlobal.pos, Blocks.STONE_BRICKS.getDefaultState(), false);
            }

            // Remove water in adjacent blocks
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (Direction direction : Direction.values()) {
                mutable.set(blockInfoGlobal.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunk = world.getChunk(mutable);
                    currentChunkPos = new ChunkPos(mutable);
                }

                if (currentChunk.getFluidState(mutable).isIn(FluidTags.WATER)) {
                    if (!(currentChunk.getBlockState(mutable).getBlock() instanceof Waterloggable && currentChunk.getBlockState(mutable).get(Properties.WATERLOGGED))) {
                        currentChunk.setBlockState(mutable, Blocks.STONE_BRICKS.getDefaultState(), false);
                    }
                }
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.WATERLOGGED_PROCESSOR;
    }
}