package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.ModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class HeadProcessor extends StructureProcessor {
    public static final HeadProcessor INSTANCE = new HeadProcessor();
    public static final Codec<HeadProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.SKELETON_SKULL)) {
            // TODO - config option for disabling heads
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.SMALL_DUNGEON_CEILING_PROP_PROCESSOR;
    }
}
