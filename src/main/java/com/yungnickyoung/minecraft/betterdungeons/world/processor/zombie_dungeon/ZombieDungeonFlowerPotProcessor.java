package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class ZombieDungeonFlowerPotProcessor extends StructureProcessor {
    public static final ZombieDungeonFlowerPotProcessor INSTANCE = new ZombieDungeonFlowerPotProcessor();
    public static final Codec<ZombieDungeonFlowerPotProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector FLOWER_SELECTOR = new BlockSetSelector(Blocks.FLOWER_POT.getDefaultState())
        .addBlock(Blocks.POTTED_CORNFLOWER.getDefaultState(), 0.1f)
        .addBlock(Blocks.POTTED_BROWN_MUSHROOM.getDefaultState(), 0.1f)
        .addBlock(Blocks.POTTED_RED_MUSHROOM.getDefaultState(), 0.1f)
        .addBlock(Blocks.POTTED_DEAD_BUSH.getDefaultState(), 0.4f)
        .addBlock(Blocks.POTTED_POPPY.getDefaultState(), 0.1f);


    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.POTTED_CORNFLOWER)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, FLOWER_SELECTOR.get(random), blockInfoGlobal.nbt);
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR;
    }
}
