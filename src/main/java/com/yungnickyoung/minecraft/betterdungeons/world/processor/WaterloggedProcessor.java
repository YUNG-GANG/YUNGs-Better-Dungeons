package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.world.chunk.ChunkSection;

/**
 * A patchwork fix for https://bugs.mojang.com/browse/MC-130584.
 */
public class WaterloggedProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final WaterloggedProcessor INSTANCE = new WaterloggedProcessor();
    public static final Codec<WaterloggedProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector blockSelector = new BlockSetSelector(Blocks.COBBLESTONE.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), .5f);

    /**
     * Workaround for https://bugs.mojang.com/browse/MC-130584
     * Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
     * Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
     */
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        // Check if block is waterloggable and not intended to be waterlogged
        if (blockInfoGlobal.state.contains(Properties.WATERLOGGED) && !blockInfoGlobal.state.get(Properties.WATERLOGGED)) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            Chunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            int sectionYIndex = currentChunk.getSectionIndex(blockInfoGlobal.pos.getY());

            // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
            // at least prevent the game from crashing.
            if (sectionYIndex < 0) {
                return blockInfoGlobal;
            }

            ChunkSection currChunkSection = currentChunk.getSection(sectionYIndex);

            if (getFluidStateSafe(currChunkSection, blockInfoGlobal.pos).isIn(FluidTags.WATER)) {
                setBlockStateSafe(currChunkSection, blockInfoGlobal.pos, blockInfoGlobal.state);
            }

            // Remove water in adjacent blocks
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (Direction direction : Direction.values()) {
                mutable.set(blockInfoGlobal.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunkPos = new ChunkPos(mutable);
                    currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
                    sectionYIndex = currentChunk.getSectionIndex(mutable.getY());
                    if (sectionYIndex < 0) {
                        return blockInfoGlobal;
                    }
                    currChunkSection = currentChunk.getSection(sectionYIndex);
                }

                if (getFluidStateSafe(currChunkSection, mutable).isIn(FluidTags.WATER)) {
                    BlockState blockState = getBlockStateSafe(currChunkSection, mutable);
                    if (blockState != null && !(blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED))) {
                        setBlockStateSafe(currChunkSection, mutable, blockSelector.get(structurePlacementData.getRandom(blockInfoGlobal.pos)));
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