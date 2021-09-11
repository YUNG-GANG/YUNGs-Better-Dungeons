package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.ISafeWorldModifier;
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
import net.minecraft.world.chunk.ChunkSection;

import java.util.Random;

/**
 * Dynamically generates support legs below zombie dungeons.
 * Magenta stained glass is used to mark the positions where the legs will spawn for simplicity.
 * Purpur slabs are to be replaced with smooth stone slabs if air is present.
 */
public class ZombieDungeonLegProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final ZombieDungeonLegProcessor INSTANCE = new ZombieDungeonLegProcessor();
    public static final Codec<ZombieDungeonLegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector LEG_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE.getDefaultState())
        .addBlock(Blocks.POLISHED_ANDESITE.getDefaultState(), 0.8f);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.MAGENTA_STAINED_GLASS) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            Chunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Always replace the glass itself with smooth stone
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SMOOTH_STONE.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, world.getBlockState(blockInfoGlobal.pos), blockInfoGlobal.nbt);
            }

            // Reusable mutable
            BlockPos.Mutable mutable = blockInfoGlobal.pos.down().mutableCopy();

            // Chunk section information
            int sectionYIndex = currentChunk.getSectionIndex(mutable.getY());
            ChunkSection currChunkSection = currentChunk.getSection(sectionYIndex);

            // Initialize currBlock
            BlockState currBlock = getBlockStateSafe(currChunkSection, mutable);
            if (currBlock == null) return blockInfoGlobal;

            // Generate vertical pillar down
            while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
                setBlockStateSafe(currChunkSection, mutable, LEG_SELECTOR.get(random));

                // Move down
                mutable.move(Direction.DOWN);

                // Update chunk section
                sectionYIndex = currentChunk.getSectionIndex(mutable.getY());
                currChunkSection = currentChunk.getSection(sectionYIndex);
                currBlock = getBlockStateSafe(currChunkSection, mutable);
                if (currBlock == null) break;
            }
        } else if (blockInfoGlobal.state.getBlock() == Blocks.PURPUR_SLAB) {
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = null;
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_DUNGEON_LEG_PROCESSOR;
    }
}
