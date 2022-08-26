package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SmallNetherDungeonEntranceStairsProcessor extends StructureProcessor {
    public static final SmallNetherDungeonEntranceStairsProcessor INSTANCE = new SmallNetherDungeonEntranceStairsProcessor();
    public static final Codec<SmallNetherDungeonEntranceStairsProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.BRICK_STAIRS)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }
            Direction facing = blockInfoGlobal.state.hasProperty(StairBlock.FACING)
                    ? blockInfoGlobal.state.getValue(StairBlock.FACING)
                    : Direction.NORTH;
            facing = structurePlacementData.getRotation().rotate(facing);
            BlockPos pos = blockInfoGlobal.pos.relative(facing);
            levelReader.getChunk(pos).setBlockState(
                    pos,
                    Blocks.NETHER_BRICK_STAIRS
                            .withPropertiesOf(blockInfoGlobal.state)
                            .setValue(StairBlock.FACING, facing.getOpposite()),
                    false);
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.NETHER_BRICKS.defaultBlockState(), blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.SMALL_NETHER_DUNGEON_ENTRANCE_STAIRS_PROCESSOR;
    }
}
