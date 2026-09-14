package com.yungnickyoung.minecraft.betterdungeons.world.processor;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;

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

    private static final List<Block> CANDLES = List.of(Blocks.CANDLE, Blocks.DYED_CANDLE.pick(DyeColor.WHITE), Blocks.DYED_CANDLE.pick(DyeColor.GRAY),
            Blocks.DYED_CANDLE.pick(DyeColor.LIGHT_GRAY), Blocks.DYED_CANDLE.pick(DyeColor.BROWN), Blocks.DYED_CANDLE.pick(DyeColor.GREEN), Blocks.DYED_CANDLE.pick(DyeColor.PURPLE), Blocks.DYED_CANDLE.pick(DyeColor.BLACK));

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() instanceof SeaPickleBlock) {
            RandomSource random = structurePlacementData.getRandom(blockInfo.pos());
            int numCandles = random.nextInt(4) + 1;
            boolean lit = random.nextFloat() < .1f;
            BlockState newBlockState = getRandomCandle(random).defaultBlockState()
                    .setValue(CandleBlock.CANDLES, numCandles)
                    .setValue(CandleBlock.LIT, lit);
            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), newBlockState, blockInfo.nbt());
        }
        return blockInfo;
    }

    private static Block getRandomCandle(RandomSource random) {
        int i = random.nextInt(CANDLES.size());
        return CANDLES.get(i);
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
