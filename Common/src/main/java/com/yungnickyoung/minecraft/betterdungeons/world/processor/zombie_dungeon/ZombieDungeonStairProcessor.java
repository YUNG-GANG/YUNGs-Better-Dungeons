package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Processes stairs to give them a more varied and ruined look.
 */
public class ZombieDungeonStairProcessor extends StructureProcessor {
    public static final ZombieDungeonStairProcessor INSTANCE = new ZombieDungeonStairProcessor();
    public static final Codec<ZombieDungeonStairProcessor> CODEC = Codec.unit(() -> INSTANCE);

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
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.COBBLESTONE_STAIRS) {
            if (levelReader.getBlockState(blockInfoGlobal.pos).isAir()) {
                // Don't replace air to maintain rotted look
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.defaultBlockState(), blockInfoGlobal.nbt);
            } else {
                BlockState newBlock = SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos));
                if (newBlock.getBlock() instanceof StairBlock) {
                    newBlock = newBlock
                        .setValue(StairBlock.FACING, blockInfoGlobal.state.getValue(StairBlock.FACING))
                        .setValue(StairBlock.HALF, blockInfoGlobal.state.getValue(StairBlock.HALF))
                        .setValue(StairBlock.SHAPE, blockInfoGlobal.state.getValue(StairBlock.SHAPE));
                }
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, newBlock, blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.ZOMBIE_DUNGEON_STAIR_PROCESSOR;
    }
}