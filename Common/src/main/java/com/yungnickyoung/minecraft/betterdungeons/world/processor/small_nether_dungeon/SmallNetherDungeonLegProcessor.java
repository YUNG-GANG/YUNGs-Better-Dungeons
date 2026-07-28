package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Dynamically generates support legs below small Nether dungeons.
 * Orange terracotta glass is used to mark the positions where the legs will spawn.
 */


public class SmallNetherDungeonLegProcessor implements StructureProcessor {
    public static final SmallNetherDungeonLegProcessor INSTANCE = new SmallNetherDungeonLegProcessor();
    public static final MapCodec<SmallNetherDungeonLegProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfo.pos()))) {
                return blockInfo;
            }

            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.NETHER_BRICKS.defaultBlockState(), null);
            BlockPos.MutableBlockPos mutable = blockInfo.pos().mutable().move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);

            while (mutable.getY() > levelReader.getMinY()
                    && mutable.getY() < levelReader.getMaxY()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
                levelReader.getChunk(mutable).setBlockState(mutable, Blocks.NETHER_BRICKS.defaultBlockState());
                mutable.move(Direction.DOWN);
                currBlockState = levelReader.getBlockState(mutable);
            }
        }

        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
