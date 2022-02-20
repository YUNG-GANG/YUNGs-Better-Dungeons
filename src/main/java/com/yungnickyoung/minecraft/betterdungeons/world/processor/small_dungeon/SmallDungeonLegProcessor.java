package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Material;

import java.util.Optional;
import java.util.Random;

/**
 * Dynamically generates support legs below small dungeons.
 * Yellow stained glass is used to mark the corner positions where the legs will spawn for simplicity.
 */
public class SmallDungeonLegProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final SmallDungeonLegProcessor INSTANCE = new SmallDungeonLegProcessor();
    public static final Codec<SmallDungeonLegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector STONE_BRICK_SELECTOR = new BlockSetSelector(Blocks.STONE_BRICKS.defaultBlockState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.5f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.2f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.YELLOW_STAINED_GLASS) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            ChunkAccess currentChunk = levelReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Always replace the glass itself with mossy cobble
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), blockInfoGlobal.nbt);

            // Reusable mutable
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos.below().mutable();

            // Chunk section information
            int sectionYIndex = currentChunk.getSectionIndex(mutable.getY());

            // Validate chunk section index. Sometimes the index is -1. Not sure why, but this will
            // at least prevent the game from crashing.
            if (sectionYIndex < 0) {
                return blockInfoGlobal;
            }

            LevelChunkSection currChunkSection = currentChunk.getSection(sectionYIndex);

            // Initialize currBlock
            Optional<BlockState> currBlock = getBlockStateSafe(currChunkSection, mutable);
            if (currBlock.isEmpty()) return blockInfoGlobal;

            // Generate vertical pillar down
            while (mutable.getY() > levelReader.getMinBuildHeight() && (currBlock.get().getMaterial() == Material.AIR || !currBlock.get().getFluidState().isEmpty())) {
                setBlockStateSafe(currChunkSection, mutable, STONE_BRICK_SELECTOR.get(random));

                // Move down
                mutable.move(Direction.DOWN);

                // Update chunk section
                sectionYIndex = currentChunk.getSectionIndex(mutable.getY());

                // Validate chunk section index
                if (sectionYIndex < 0) {
                    return blockInfoGlobal;
                }

                currChunkSection = currentChunk.getSection(sectionYIndex);
                currBlock = getBlockStateSafe(currChunkSection, mutable);
                if (currBlock.isEmpty()) break;
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_LEG_PROCESSOR;
    }
}
