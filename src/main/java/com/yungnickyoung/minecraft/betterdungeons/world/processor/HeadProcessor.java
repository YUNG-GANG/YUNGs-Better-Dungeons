package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class HeadProcessor extends StructureProcessor {
    public static final HeadProcessor INSTANCE = new HeadProcessor();
    public static final Codec<HeadProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof AbstractSkullBlock) {
            if (!BetterDungeons.CONFIG.betterDungeons.general.enableHeads) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.HEAD_PROCESSOR;
    }
}
