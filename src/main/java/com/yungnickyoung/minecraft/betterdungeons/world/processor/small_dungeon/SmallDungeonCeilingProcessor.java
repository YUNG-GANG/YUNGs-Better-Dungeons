package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Replaces any liquid in the ceiling with cobblestone to attempt to minimize weird
 * floating fluid interactions.
 */
@MethodsReturnNonnullByDefault
public class SmallDungeonCeilingProcessor extends StructureProcessor {
    public static final SmallDungeonCeilingProcessor INSTANCE = new SmallDungeonCeilingProcessor();
    public static final Codec<SmallDungeonCeilingProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.ORANGE_STAINED_GLASS) {
            if (world.getFluidState(blockInfoGlobal.pos).isTagged(FluidTags.WATER) || world.getFluidState(blockInfoGlobal.pos).isTagged(FluidTags.LAVA)) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.COBBLESTONE.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, world.getBlockState(blockInfoGlobal.pos), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_CEILING_PROCESSOR;
    }
}
