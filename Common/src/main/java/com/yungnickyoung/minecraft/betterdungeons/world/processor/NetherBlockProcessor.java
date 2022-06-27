package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class NetherBlockProcessor extends StructureProcessor {
    public static final NetherBlockProcessor INSTANCE = new NetherBlockProcessor();
    public static final Codec<NetherBlockProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (!BetterDungeonsCommon.CONFIG.general.enableNetherBlocks) {
            if (blockInfoGlobal.state.is(Blocks.SOUL_SAND) || blockInfoGlobal.state.is(Blocks.SOUL_SOIL)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.COARSE_DIRT.defaultBlockState(), blockInfoGlobal.nbt);
            } else if (blockInfoGlobal.state.is(Blocks.SOUL_CAMPFIRE)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAMPFIRE.defaultBlockState(), blockInfoGlobal.nbt);
            } else if (blockInfoGlobal.state.is(Blocks.SOUL_LANTERN)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, blockInfoGlobal.state.getValue(LanternBlock.HANGING)), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.NETHER_BLOCK_PROCESSOR;
    }
}