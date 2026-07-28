package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Replaces cobblestone w/ air in small dungeons when air is already there,
 * giving them a more natural, ruined look that opens up to caves.
 */


public class SmallDungeonCobblestoneProcessor implements StructureProcessor {
    public static final SmallDungeonCobblestoneProcessor INSTANCE = new SmallDungeonCobblestoneProcessor();
    public static final MapCodec<SmallDungeonCobblestoneProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.COBBLESTONE) {
            if (levelReader.getBlockState(blockInfo.pos()).isAir()) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), null);
            }
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
