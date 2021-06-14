package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
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

/**
 * Replaces yellow glass w/ air in skeleton dungeons when air is already there,
 * giving them a more natural, ruined look that opens up to caves.
 * Replaces them w/ stone bricks otherwise.
 */
@MethodsReturnNonnullByDefault
public class RuinedStoneBrickProcessor extends StructureProcessor {
    public static final RuinedStoneBrickProcessor INSTANCE = new RuinedStoneBrickProcessor();
    public static final Codec<RuinedStoneBrickProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector STONE_BRICK_SELECTOR = new BlockSetSelector(Blocks.STONE_BRICKS.getDefaultState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.3f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.2f)
        .addBlock(Blocks.INFESTED_STONE_BRICKS.getDefaultState(), 0.05f);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.YELLOW_STAINED_GLASS) {
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, STONE_BRICK_SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos)), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR;
    }
}
