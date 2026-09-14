package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;



public class SmallNetherDungeonEntranceStairsProcessor implements StructureProcessor {
    public static final SmallNetherDungeonEntranceStairsProcessor INSTANCE = new SmallNetherDungeonEntranceStairsProcessor();
    public static final MapCodec<SmallNetherDungeonEntranceStairsProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().is(Blocks.BRICK_STAIRS)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfo.pos()))) {
                return blockInfo;
            }
            Direction facing = blockInfo.state().hasProperty(StairBlock.FACING)
                    ? blockInfo.state().getValue(StairBlock.FACING)
                    : Direction.NORTH;
            facing = structurePlacementData.getRotation().rotate(facing);
            BlockPos pos = blockInfo.pos().relative(facing);
            levelReader.getChunk(pos).setBlockState(pos, Blocks.NETHER_BRICK_STAIRS
                    .withPropertiesOf(blockInfo.state())
                    .setValue(StairBlock.FACING, facing.getOpposite()));
            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.NETHER_BRICKS.defaultBlockState(), blockInfo.nbt());
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
