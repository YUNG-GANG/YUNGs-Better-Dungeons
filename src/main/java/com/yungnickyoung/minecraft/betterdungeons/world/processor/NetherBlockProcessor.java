package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class NetherBlockProcessor extends StructureProcessor {
    public static final NetherBlockProcessor INSTANCE = new NetherBlockProcessor();
    public static final Codec<NetherBlockProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (!BDConfig.general.enableNetherBlocks.get()) {
            if (blockInfoGlobal.state.isIn(Blocks.SOUL_SAND) || blockInfoGlobal.state.isIn(Blocks.SOUL_SOIL)) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.COARSE_DIRT.getDefaultState(), blockInfoGlobal.nbt);
            } else if (blockInfoGlobal.state.isIn(Blocks.SOUL_CAMPFIRE)) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAMPFIRE.getDefaultState(), blockInfoGlobal.nbt);
            } else if (blockInfoGlobal.state.isIn(Blocks.SOUL_LANTERN)) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, blockInfoGlobal.state.get(LanternBlock.HANGING)), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.NETHER_BLOCK_PROCESSOR;
    }
}