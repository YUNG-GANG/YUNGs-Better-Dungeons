package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Random;

public class CandleProcessor extends StructureProcessor {
    public static final CandleProcessor INSTANCE = new CandleProcessor();
    public static final Codec<CandleProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final List<Block> CANDLES = List.of(Blocks.CANDLE, Blocks.WHITE_CANDLE, Blocks.GRAY_CANDLE,
            Blocks.LIGHT_GRAY_CANDLE, Blocks.BROWN_CANDLE, Blocks.GREEN_CANDLE, Blocks.PURPLE_CANDLE, Blocks.BLACK_CANDLE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof SeaPickleBlock) {
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            int numCandles = random.nextInt(4) + 1;
            boolean lit = random.nextFloat() < .1f;
            BlockState newBlockState = getRandomCandle(random).defaultBlockState()
                    .setValue(CandleBlock.CANDLES, numCandles)
                    .setValue(CandleBlock.LIT, lit);
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, newBlockState, blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    private static Block getRandomCandle(Random random) {
        int i = random.nextInt(CANDLES.size());
        return CANDLES.get(i);
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.CANDLE_PROCESSOR;
    }
}
