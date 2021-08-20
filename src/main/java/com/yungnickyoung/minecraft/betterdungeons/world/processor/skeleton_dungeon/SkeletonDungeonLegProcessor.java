package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

/**
 * Dynamically generates support legs below skeleton dungeons.
 * Blue stained glass is used to mark the positions where the legs will spawn for simplicity.
 */
public class SkeletonDungeonLegProcessor extends StructureProcessor {
    public static final SkeletonDungeonLegProcessor INSTANCE = new SkeletonDungeonLegProcessor();
    public static final Codec<SkeletonDungeonLegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector COBBLE_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.5f);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.BLUE_STAINED_GLASS) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            Chunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Always replace the glass itself with cobble
            currentChunk.setBlockState(blockInfoGlobal.pos, Blocks.COBBLESTONE.getDefaultState(), false);
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COBBLESTONE.getDefaultState(), blockInfoGlobal.tag);

            // Generate vertical pillar down
            BlockPos.Mutable mutable = blockInfoGlobal.pos.down().mutableCopy();
            BlockState currBlock = world.getBlockState(mutable);
            while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
                currentChunk.setBlockState(mutable, COBBLE_SELECTOR.get(random), false);
                mutable.move(Direction.DOWN);
                currBlock = world.getBlockState(mutable);
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SKELETON_DUNGEON_LEG_PROCESSOR;
    }
}
