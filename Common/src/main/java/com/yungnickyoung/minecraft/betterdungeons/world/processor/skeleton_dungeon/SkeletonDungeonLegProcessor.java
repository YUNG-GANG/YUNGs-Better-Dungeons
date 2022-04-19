package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorModule;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
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
 * Dynamically generates support legs below skeleton dungeons.
 * Blue stained glass is used to mark the positions where the legs will spawn for simplicity.
 */
public class SkeletonDungeonLegProcessor extends StructureProcessor {
    public static final SkeletonDungeonLegProcessor INSTANCE = new SkeletonDungeonLegProcessor();
    public static final Codec<SkeletonDungeonLegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockStateRandomizer COBBLE_SELECTOR = new BlockStateRandomizer(Blocks.COBBLESTONE.defaultBlockState())
        .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.5f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.BLUE_STAINED_GLASS) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }

            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COBBLESTONE.defaultBlockState(), blockInfoGlobal.nbt);
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos.mutable().move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);

            while (mutable.getY() > levelReader.getMinBuildHeight()
                    && mutable.getY() < levelReader.getMaxBuildHeight()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
                levelReader.getChunk(mutable).setBlockState(mutable, COBBLE_SELECTOR.get(random), false);
                mutable.move(Direction.DOWN);
                currBlockState = levelReader.getBlockState(mutable);
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorModule.SKELETON_DUNGEON_LEG_PROCESSOR;
    }
}
