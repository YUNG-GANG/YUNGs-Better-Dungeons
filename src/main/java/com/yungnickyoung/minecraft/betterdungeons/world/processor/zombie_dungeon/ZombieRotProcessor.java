package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Replaces certain blocks w/ air in zombie dungeons when air is already there,
 * giving them a more natural, ruined look that opens up to caves.
 */
public class ZombieRotProcessor extends StructureProcessor {
    public static final ZombieRotProcessor INSTANCE = new ZombieRotProcessor();
    public static final Codec<ZombieRotProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.COBBLESTONE || blockInfoGlobal.state.getBlock() == Blocks.CYAN_TERRACOTTA || blockInfoGlobal.state.getBlock() == Blocks.COBBLESTONE_STAIRS) {
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_ROT_PROCESSOR;
    }
}
