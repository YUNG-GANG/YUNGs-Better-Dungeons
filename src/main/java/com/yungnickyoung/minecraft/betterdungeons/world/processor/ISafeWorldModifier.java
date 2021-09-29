package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Utility methods that bypass the PaletteContainer's lock, as it was causing an
 * `Accessing PalettedContainer from multiple threads` crash, even though everything
 * seemed to be safe.
 *
 * This crash started occurring in 1.17. I currently do not know the cause, so this
 * is a workaround in the meantime.
 *
 * @author TelepathicGrunt
 */
public interface ISafeWorldModifier {
    /**
     * Safe method for grabbing a FluidState. Copies what vanilla ores do.
     */
    default FluidState getFluidStateSafe(ChunkSection chunkSection, BlockPos pos) {
        if (ChunkSection.isEmpty(chunkSection)) return Fluids.EMPTY.getDefaultState();
        return chunkSection.getFluidState(
            ChunkSectionPos.getLocalCoord(pos.getX()),
            ChunkSectionPos.getLocalCoord(pos.getY()),
            ChunkSectionPos.getLocalCoord(pos.getZ()));
    }

    /**
     * Safe method for grabbing a FluidState. Copies what vanilla ores do.
     */
    default FluidState getFluidStateSafe(WorldView world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int sectionYIndex = chunk.getSectionIndex(pos.getY());
        // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
        // at least prevent the game from crashing.
        if (sectionYIndex < 0) {
            return Fluids.EMPTY.getDefaultState();
        }

        ChunkSection chunkSection = chunk.getSection(sectionYIndex);

        return getFluidStateSafe(chunkSection, pos);
    }

    /**
     * Safe method for grabbing a BlockState. Copies what vanilla ores do.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     *
     * @author TelepathicGrunt
     */
    default BlockState getBlockStateSafe(ChunkSection chunkSection, BlockPos pos) {
        if (ChunkSection.isEmpty(chunkSection)) return null;
        return chunkSection.getBlockState(
            ChunkSectionPos.getLocalCoord(pos.getX()),
            ChunkSectionPos.getLocalCoord(pos.getY()),
            ChunkSectionPos.getLocalCoord(pos.getZ()));
    }

    /**
     * Safe method for grabbing a BlockState. Copies what vanilla ores do.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     *
     * @author TelepathicGrunt
     */
    default BlockState getBlockStateSafe(WorldView world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int sectionYIndex = chunk.getSectionIndex(pos.getY());
        // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
        // at least prevent the game from crashing.
        if (sectionYIndex < 0) {
            return Blocks.AIR.getDefaultState();
        }

        ChunkSection chunkSection = chunk.getSection(sectionYIndex);

        return getBlockStateSafe(chunkSection, pos);
    }

    default boolean isBlockStateAirSafe(WorldView world, BlockPos pos) {
        BlockState blockState = getBlockStateSafe(world, pos);
        return blockState != null && blockState.isAir();
    }

    default boolean isMaterialLiquidSafe(WorldView world, BlockPos pos) {
        BlockState blockState = getBlockStateSafe(world, pos);
        return blockState != null && blockState.getMaterial().isLiquid();
    }

    /**
     * Safe method for setting a BlockState. Copies what vanilla ores do.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     *
     * @author TelepathicGrunt
     */
    default BlockState setBlockStateSafe(ChunkSection chunkSection, BlockPos pos, BlockState state) {
        if (chunkSection == WorldChunk.EMPTY_SECTION) return null;
        return chunkSection.setBlockState(
            ChunkSectionPos.getLocalCoord(pos.getX()),
            ChunkSectionPos.getLocalCoord(pos.getY()),
            ChunkSectionPos.getLocalCoord(pos.getZ()),
            state,
            false);
    }

    /**
     * Safe method for setting a BlockState. Copies what vanilla ores do.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     *
     * @author TelepathicGrunt
     */
    default BlockState setBlockStateSafe(WorldView world, BlockPos pos, BlockState state) {
        ChunkPos chunkPos = new ChunkPos(pos);
        Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int sectionYIndex = chunk.getSectionIndex(pos.getY());
        // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
        // at least prevent the game from crashing.
        if (sectionYIndex < 0) {
            return null;
        }

        ChunkSection chunkSection = chunk.getSection(sectionYIndex);

        return setBlockStateSafe(chunkSection, pos, state);
    }
}
