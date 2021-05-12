package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.ModProcessors;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
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
public class CeilingLampPropProcessor extends StructureProcessor {
    public static final CeilingLampPropProcessor INSTANCE = new CeilingLampPropProcessor();
    public static final Codec<CeilingLampPropProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.isIn(Blocks.CYAN_STAINED_GLASS)) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Choose lamp prop
            float f = random.nextFloat();
            if (f < 0.1f) // Soul lantern
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.SOUL_LANTERN.getDefaultState().with(BlockStateProperties.HANGING, true), blockInfoGlobal.nbt);
            else if (f < 0.625f) // Chain
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CHAIN.getDefaultState(), blockInfoGlobal.nbt);
            else // None
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.nbt);
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.SMALL_DUNGEON_CEILING_LAMP_PROCESSOR;
    }
}
