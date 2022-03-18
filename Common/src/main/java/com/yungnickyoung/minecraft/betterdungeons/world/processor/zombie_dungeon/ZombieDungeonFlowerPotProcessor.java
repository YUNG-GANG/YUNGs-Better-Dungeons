package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorModule;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class ZombieDungeonFlowerPotProcessor extends StructureProcessor {
    public static final ZombieDungeonFlowerPotProcessor INSTANCE = new ZombieDungeonFlowerPotProcessor();
    public static final Codec<ZombieDungeonFlowerPotProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockStateRandomizer FLOWER_SELECTOR = new BlockStateRandomizer(Blocks.FLOWER_POT.defaultBlockState())
        .addBlock(Blocks.POTTED_CORNFLOWER.defaultBlockState(), 0.1f)
        .addBlock(Blocks.POTTED_BROWN_MUSHROOM.defaultBlockState(), 0.1f)
        .addBlock(Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 0.1f)
        .addBlock(Blocks.POTTED_DEAD_BUSH.defaultBlockState(), 0.4f)
        .addBlock(Blocks.POTTED_POPPY.defaultBlockState(), 0.1f);


    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.POTTED_CORNFLOWER)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, FLOWER_SELECTOR.get(random), blockInfoGlobal.nbt);
        }

        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorModule.ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR;
    }
}
