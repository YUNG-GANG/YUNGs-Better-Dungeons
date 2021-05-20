package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
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

/**
 * Replaces white stained glass and gray stained glass with cobwebs and air.
 * The replacement rate for each of these blocks is configurable.
 * By default, gray stained glass yields a higher proportion of cobwebs than white.
 * Gray is intended for use around mob spawners and similar hostile areas.
 */
@MethodsReturnNonnullByDefault
public class CobwebProcessor extends StructureProcessor {
    public static final CobwebProcessor INSTANCE = new CobwebProcessor();
    public static final Codec<CobwebProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.WHITE_STAINED_GLASS) || blockInfoGlobal.state.isIn(Blocks.GRAY_STAINED_GLASS)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float replacementChance = getReplacementChance(blockInfoGlobal.state);
            if (random.nextFloat() < replacementChance)
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.COBWEB.getDefaultState(), blockInfoGlobal.nbt);
            else
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.COBWEB_PROCESSOR;
    }

    /**
     * Returns cobweb replacement chance for the given BlockState.
     * TODO - config values?
     */
    private float getReplacementChance(BlockState blockState) {
        if (blockState.isIn(Blocks.WHITE_STAINED_GLASS))
            return .3f;
        else if (blockState.isIn(Blocks.GRAY_STAINED_GLASS))
            return .7f;
        else return 0; // Should never happen
    }
}
