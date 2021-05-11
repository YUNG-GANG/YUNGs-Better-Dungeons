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
import java.util.Random;

@MethodsReturnNonnullByDefault
public class CeilingPropProcessor extends StructureProcessor {
    public static final CeilingPropProcessor INSTANCE = new CeilingPropProcessor();
    public static final Codec<CeilingPropProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.MAGENTA_STAINED_GLASS)) {
            // If ceiling isn't solid, simply ignore processing since we don't want floating props
            if (!world.getBlockState(blockInfoGlobal.pos.up()).isSolid()) {
                return null;
            }

            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            float f = random.nextFloat();

            // Choose ceiling prop
            if (f < .3f) blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.nbt);
            else if (f < .75f) blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.nbt);
            else return null;
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.SMALL_DUNGEON_CEILING_PROP_PROCESSOR;
    }
}