package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Random;

public class ZombieDungeonFlowerPotProcessor extends StructureProcessor {
    public static final ZombieDungeonFlowerPotProcessor INSTANCE = new ZombieDungeonFlowerPotProcessor();
    public static final Codec<ZombieDungeonFlowerPotProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector FLOWER_SELECTOR = new BlockSetSelector(Blocks.FLOWER_POT.getDefaultState())
        .addBlock(Blocks.POTTED_CORNFLOWER.getDefaultState(), 0.1f)
        .addBlock(Blocks.POTTED_BROWN_MUSHROOM.getDefaultState(), 0.1f)
        .addBlock(Blocks.POTTED_RED_MUSHROOM.getDefaultState(), 0.1f)
        .addBlock(Blocks.POTTED_DEAD_BUSH.getDefaultState(), 0.4f)
        .addBlock(Blocks.POTTED_POPPY.getDefaultState(), 0.1f);


    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.POTTED_CORNFLOWER)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, FLOWER_SELECTOR.get(random), blockInfoGlobal.nbt);
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR;
    }
}
