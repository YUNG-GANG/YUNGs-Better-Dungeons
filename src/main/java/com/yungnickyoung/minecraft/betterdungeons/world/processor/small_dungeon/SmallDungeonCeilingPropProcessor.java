package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
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
import java.util.Random;

@MethodsReturnNonnullByDefault
public class SmallDungeonCeilingPropProcessor extends StructureProcessor {
    public static final SmallDungeonCeilingPropProcessor INSTANCE = new SmallDungeonCeilingPropProcessor();
    public static final Codec<SmallDungeonCeilingPropProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.MAGENTA_STAINED_GLASS)) {
            // If ceiling isn't solid, place air since we don't want floating props
            if (!world.getBlockState(blockInfoGlobal.pos.up()).isSolid()) {
                return new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            }

            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .2f) blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.nbt);
            else blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
        } else if (blockInfoGlobal.state.isIn(Blocks.BROWN_STAINED_GLASS)) {
            // If ceiling isn't solid, simply ignore processing since we don't want floating props
            if (!world.getBlockState(blockInfoGlobal.pos.up(2)).isSolid()) {
                return new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            }

            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .5f) blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.nbt);
            else blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
        } else if (blockInfoGlobal.state.isIn(Blocks.CHAIN)) {
            // If ceiling isn't solid, don't place top chains for potential double chains if they would be floating
            if (!world.getBlockState(blockInfoGlobal.pos.up()).isSolid()) {
                return new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            }
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_CEILING_PROP_PROCESSOR;
    }
}