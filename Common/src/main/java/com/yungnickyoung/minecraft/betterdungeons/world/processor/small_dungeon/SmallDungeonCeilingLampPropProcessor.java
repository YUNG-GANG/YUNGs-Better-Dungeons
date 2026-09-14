package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;



public class SmallDungeonCeilingLampPropProcessor implements StructureProcessor {
    public static final SmallDungeonCeilingLampPropProcessor INSTANCE = new SmallDungeonCeilingLampPropProcessor();
    public static final MapCodec<SmallDungeonCeilingLampPropProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().is(Blocks.STAINED_GLASS.pick(DyeColor.CYAN))) {
            RandomSource random = structurePlacementData.getRandom(blockInfo.pos());

            // Choose lamp prop
            float f = random.nextFloat();
            if (f < 0.625f) { // Chain
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.IRON_CHAIN.defaultBlockState(), null);
            } else { // None
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
