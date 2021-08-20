package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Replaces any liquid in the ceiling with cobblestone to attempt to minimize weird
 * floating fluid interactions.
 */
public class SmallDungeonCeilingProcessor extends StructureProcessor {
    public static final SmallDungeonCeilingProcessor INSTANCE = new SmallDungeonCeilingProcessor();
    public static final Codec<SmallDungeonCeilingProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.ORANGE_STAINED_GLASS) {
            if (world.getFluidState(blockInfoGlobal.pos).isIn(FluidTags.WATER) || world.getFluidState(blockInfoGlobal.pos).isIn(FluidTags.LAVA)) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COBBLESTONE.getDefaultState(), blockInfoGlobal.tag);
            } else {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, world.getBlockState(blockInfoGlobal.pos), blockInfoGlobal.tag);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_CEILING_PROCESSOR;
    }
}
