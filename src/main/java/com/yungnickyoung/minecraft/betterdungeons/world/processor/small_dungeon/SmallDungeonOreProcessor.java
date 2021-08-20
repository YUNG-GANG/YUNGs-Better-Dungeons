package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Replaces ore in props with cobblestone if ores are disabled in the config.
 */
public class SmallDungeonOreProcessor extends StructureProcessor {
    public static final SmallDungeonOreProcessor INSTANCE = new SmallDungeonOreProcessor();
    public static final Codec<SmallDungeonOreProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof OreBlock) {
            if (!BetterDungeons.CONFIG.betterDungeons.smallDungeon.enableOreProps) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_ORE_PROCESSOR;
    }
}
