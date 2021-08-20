package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import java.util.Random;

public class SmallDungeonCeilingPropProcessor extends StructureProcessor {
    public static final SmallDungeonCeilingPropProcessor INSTANCE = new SmallDungeonCeilingPropProcessor();
    public static final Codec<SmallDungeonCeilingPropProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.MAGENTA_STAINED_GLASS)) {
            // If ceiling isn't solid, place air since we don't want floating props
            if (!world.getBlockState(blockInfoGlobal.pos.up()).isSolidBlock(world, blockInfoGlobal.pos.up())) {
                return new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
            }

            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .2f) blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.tag);
            else blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
        } else if (blockInfoGlobal.state.isOf(Blocks.BROWN_STAINED_GLASS)) {
            // If ceiling isn't solid, simply ignore processing since we don't want floating props
            if (!world.getBlockState(blockInfoGlobal.pos.up(2)).isSolidBlock(world, blockInfoGlobal.pos.up())) {
                return new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
            }

            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .5f) blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.tag);
            else blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
        } else if (blockInfoGlobal.state.isOf(Blocks.CHAIN)) {
            // If ceiling isn't solid, don't place top chains for potential double chains if they would be floating
            if (!world.getBlockState(blockInfoGlobal.pos.up()).isSolidBlock(world, blockInfoGlobal.pos.up())) {
                return new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
            }
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_CEILING_PROP_PROCESSOR;
    }
}