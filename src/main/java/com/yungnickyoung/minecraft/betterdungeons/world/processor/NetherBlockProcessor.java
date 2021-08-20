package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class NetherBlockProcessor extends StructureProcessor {
    public static final NetherBlockProcessor INSTANCE = new NetherBlockProcessor();
    public static final Codec<NetherBlockProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (!BetterDungeons.CONFIG.betterDungeons.general.enableNetherBlocks) {
            if (blockInfoGlobal.state.isOf(Blocks.SOUL_SAND) || blockInfoGlobal.state.isOf(Blocks.SOUL_SOIL)) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COARSE_DIRT.getDefaultState(), blockInfoGlobal.tag);
            } else if (blockInfoGlobal.state.isOf(Blocks.SOUL_CAMPFIRE)) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAMPFIRE.getDefaultState(), blockInfoGlobal.tag);
            } else if (blockInfoGlobal.state.isOf(Blocks.SOUL_LANTERN)) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, blockInfoGlobal.state.get(LanternBlock.HANGING)), blockInfoGlobal.tag);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.NETHER_BLOCK_PROCESSOR;
    }
}