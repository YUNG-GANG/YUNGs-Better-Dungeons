package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Dynamically generates support legs below small dungeons.
 * Yellow stained glass is used to mark the corner positions where the legs will spawn for simplicity.
 */


public class SmallDungeonLegProcessor implements StructureProcessor {
    public static final SmallDungeonLegProcessor INSTANCE = new SmallDungeonLegProcessor();
    public static final MapCodec<SmallDungeonLegProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static final BlockStateRandomizer STONE_BRICK_SELECTOR = new BlockStateRandomizer(Blocks.STONE_BRICKS.defaultBlockState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.5f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.2f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.STAINED_GLASS.pick(DyeColor.YELLOW)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfo.pos()))) {
                return blockInfo;
            }

            RandomSource random = structurePlacementData.getRandom(blockInfo.pos());

            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);
            BlockPos.MutableBlockPos mutable = blockInfo.pos().mutable().move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);

            while (mutable.getY() > levelReader.getMinY()
                    && mutable.getY() < levelReader.getMaxY()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
                levelReader.getChunk(mutable).setBlockState(mutable, STONE_BRICK_SELECTOR.get(random));
                mutable.move(Direction.DOWN);
                currBlockState = levelReader.getBlockState(mutable);
            }
        }

        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
