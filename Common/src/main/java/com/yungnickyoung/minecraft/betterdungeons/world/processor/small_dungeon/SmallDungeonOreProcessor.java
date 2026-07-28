package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.function.Predicate;

/**
 * Replaces ore in props with cobblestone if ores are disabled in the config.
 */


public class SmallDungeonOreProcessor implements StructureProcessor {
    public static final SmallDungeonOreProcessor INSTANCE = new SmallDungeonOreProcessor();
    public static final MapCodec<SmallDungeonOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static final Predicate<BlockState> isOre = blockState ->
            blockState.is(BlockTags.GOLD_ORES) ||
            blockState.is(BlockTags.IRON_ORES) ||
            blockState.is(BlockTags.COPPER_ORES);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (isOre.test(blockInfo.state())) {
            if (!BetterDungeonsCommon.CONFIG.smallDungeons.enableOreProps) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.CAVE_AIR.defaultBlockState(), null);
            }
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
