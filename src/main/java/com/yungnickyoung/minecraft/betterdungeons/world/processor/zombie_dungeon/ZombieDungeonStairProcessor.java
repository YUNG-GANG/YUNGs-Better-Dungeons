package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Processes stairs to give them a more varied and ruined look.
 */
public class ZombieDungeonStairProcessor extends StructureProcessor {
    public static final ZombieDungeonStairProcessor INSTANCE = new ZombieDungeonStairProcessor();
    public static final Codec<ZombieDungeonStairProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE_STAIRS.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE_STAIRS.getDefaultState(), 0.4f)
        .addBlock(Blocks.COBBLESTONE_SLAB.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.1f)
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.COBBLESTONE_STAIRS) {
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                // Don't replace air to maintain rotted look
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                BlockState newBlock = SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos));
                if (newBlock.getBlock() instanceof StairsBlock) {
                    newBlock = newBlock
                        .with(StairsBlock.FACING, blockInfoGlobal.state.get(StairsBlock.FACING))
                        .with(StairsBlock.HALF, blockInfoGlobal.state.get(StairsBlock.HALF))
                        .with(StairsBlock.SHAPE, blockInfoGlobal.state.get(StairsBlock.SHAPE));
                }
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, newBlock, blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_DUNGEON_STAIR_PROCESSOR;
    }
}