package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Replaces any liquid in the ceiling with cobblestone to attempt to minimize weird
 * floating fluid interactions.
 */


public class SmallDungeonCeilingProcessor implements StructureProcessor {
    public static final SmallDungeonCeilingProcessor INSTANCE = new SmallDungeonCeilingProcessor();
    public static final MapCodec<SmallDungeonCeilingProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.STAINED_GLASS.pick(DyeColor.ORANGE)) {
            if (levelReader.getFluidState(blockInfo.pos()).is(FluidTags.WATER) || levelReader.getFluidState(blockInfo.pos()).is(FluidTags.LAVA)) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.COBBLESTONE.defaultBlockState(), null);
            } else {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), levelReader.getBlockState(blockInfo.pos()), null);
            }
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
