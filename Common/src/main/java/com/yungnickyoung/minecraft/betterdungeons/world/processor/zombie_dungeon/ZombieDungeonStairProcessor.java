package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Processes stairs to give them a more varied and ruined look.
 */


public class ZombieDungeonStairProcessor implements StructureProcessor {
    public static final ZombieDungeonStairProcessor INSTANCE = new ZombieDungeonStairProcessor();
    public static final MapCodec<ZombieDungeonStairProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static final BlockStateRandomizer SELECTOR = new BlockStateRandomizer(Blocks.COBBLESTONE_STAIRS.defaultBlockState())
        .addBlock(Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState(), 0.4f)
        .addBlock(Blocks.COBBLESTONE_SLAB.defaultBlockState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.1f)
        .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.1f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.COBBLESTONE_STAIRS) {
            if (levelReader.getBlockState(blockInfo.pos()).isAir()) {
                // Don't replace air to maintain rotted look
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), null);
            } else {
                BlockState newBlock = SELECTOR.get(structurePlacementData.getRandom(blockInfo.pos()));
                if (newBlock.getBlock() instanceof StairBlock) {
                    newBlock = newBlock
                        .setValue(StairBlock.FACING, blockInfo.state().getValue(StairBlock.FACING))
                        .setValue(StairBlock.HALF, blockInfo.state().getValue(StairBlock.HALF))
                        .setValue(StairBlock.SHAPE, blockInfo.state().getValue(StairBlock.SHAPE));
                }
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), newBlock, null);
            }
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}