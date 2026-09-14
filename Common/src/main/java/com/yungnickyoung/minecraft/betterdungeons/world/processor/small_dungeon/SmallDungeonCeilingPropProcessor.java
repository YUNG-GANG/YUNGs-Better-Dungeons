package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;



public class SmallDungeonCeilingPropProcessor implements StructureProcessor {
    public static final SmallDungeonCeilingPropProcessor INSTANCE = new SmallDungeonCeilingPropProcessor();
    public static final MapCodec<SmallDungeonCeilingPropProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().is(Blocks.STAINED_GLASS.pick(DyeColor.MAGENTA))) {
            // If ceiling isn't solid, place air since we don't want floating props
            if (!levelReader.getBlockState(blockInfo.pos().above()).isFaceSturdy(levelReader, blockInfo.pos().above(), Direction.DOWN)) {
                return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), null);
            }

            RandomSource random = structurePlacementData.getRandom(blockInfo.pos());
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .2f) blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.IRON_CHAIN.defaultBlockState(), blockInfo.nbt());
            else blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), blockInfo.nbt());
        } else if (blockInfo.state().is(Blocks.STAINED_GLASS.pick(DyeColor.BROWN))) {
            // If ceiling isn't solid, simply ignore processing since we don't want floating props
            if (!levelReader.getBlockState(blockInfo.pos().above(2)).isFaceSturdy(levelReader, blockInfo.pos().above(), Direction.DOWN)) {
                return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), null);
            }

            RandomSource random = structurePlacementData.getRandom(blockInfo.pos());
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .5f) blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.IRON_CHAIN.defaultBlockState(), blockInfo.nbt());
            else blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), blockInfo.nbt());
        } else if (blockInfo.state().is(Blocks.IRON_CHAIN)) {
            // If ceiling isn't solid, don't place top chains for potential double chains if they would be floating
            if (!levelReader.getBlockState(blockInfo.pos().above()).isFaceSturdy(levelReader, blockInfo.pos().above(), Direction.DOWN)) {
                return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), null);
            }
        }

        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}