package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;



public class CandleProcessor implements StructureProcessor {
    public static final CandleProcessor INSTANCE = new CandleProcessor();
    public static final MapCodec<CandleProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static final List<Block> CANDLES = List.of(Blocks.CANDLE, Blocks.DYED_CANDLE.white(), Blocks.DYED_CANDLE.gray(),
            Blocks.DYED_CANDLE.lightGray(), Blocks.DYED_CANDLE.brown(), Blocks.DYED_CANDLE.green(), Blocks.DYED_CANDLE.purple(), Blocks.DYED_CANDLE.black());

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos templateRelativePos,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() instanceof SeaPickleBlock) {
            RandomSource random = structurePlacementData.getRandom(blockInfoGlobal.pos());
            int numCandles = random.nextInt(4) + 1;
            boolean lit = random.nextFloat() < .1f;
            BlockState newBlockState = getRandomCandle(random).defaultBlockState()
                    .setValue(CandleBlock.CANDLES, numCandles)
                    .setValue(CandleBlock.LIT, lit);
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), newBlockState, blockInfoGlobal.nbt());
        }
        return blockInfoGlobal;
    }

    private static Block getRandomCandle(RandomSource random) {
        int i = random.nextInt(CANDLES.size());
        return CANDLES.get(i);
    }

    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.CANDLE_PROCESSOR;
    }
}
