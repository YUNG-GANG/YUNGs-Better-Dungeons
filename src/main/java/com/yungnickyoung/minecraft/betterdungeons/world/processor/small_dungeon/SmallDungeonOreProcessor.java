package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Replaces ore in props with cobblestone if ores are disabled in the config.
 */
@MethodsReturnNonnullByDefault
public class SmallDungeonOreProcessor extends StructureProcessor {
    public static final SmallDungeonOreProcessor INSTANCE = new SmallDungeonOreProcessor();
    public static final Codec<SmallDungeonOreProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() instanceof OreBlock) {
            if (!BDConfig.smallDungeons.enableOreProps.get()) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_ORE_PROCESSOR;
    }
}
