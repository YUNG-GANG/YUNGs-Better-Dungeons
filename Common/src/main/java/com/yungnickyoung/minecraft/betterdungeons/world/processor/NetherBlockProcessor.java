package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;



public class NetherBlockProcessor implements StructureProcessor {
    public static final NetherBlockProcessor INSTANCE = new NetherBlockProcessor();
    public static final MapCodec<NetherBlockProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (!BetterDungeonsCommon.CONFIG.general.enableNetherBlocks) {
            if (blockInfo.state().is(Blocks.SOUL_SAND) || blockInfo.state().is(Blocks.SOUL_SOIL)) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.COARSE_DIRT.defaultBlockState(), null);
            } else if (blockInfo.state().is(Blocks.SOUL_CAMPFIRE)) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAMPFIRE.defaultBlockState(), null);
            } else if (blockInfo.state().is(Blocks.SOUL_LANTERN)) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, blockInfo.state().getValue(LanternBlock.HANGING)), null);
            }
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}