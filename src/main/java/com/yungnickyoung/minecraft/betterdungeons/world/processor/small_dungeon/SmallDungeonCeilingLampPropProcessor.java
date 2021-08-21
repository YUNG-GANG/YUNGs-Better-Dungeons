package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Random;

public class SmallDungeonCeilingLampPropProcessor extends StructureProcessor {
    public static final SmallDungeonCeilingLampPropProcessor INSTANCE = new SmallDungeonCeilingLampPropProcessor();
    public static final Codec<SmallDungeonCeilingLampPropProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.CYAN_STAINED_GLASS)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Choose lamp prop
            float f = random.nextFloat();
            if (f < 0.1f) // Soul lantern
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SOUL_LANTERN.getDefaultState().with(Properties.HANGING, true), blockInfoGlobal.nbt);
            else if (f < 0.625f) // Chain
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.nbt);
            else // None
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_CEILING_LAMP_PROCESSOR;
    }
}
