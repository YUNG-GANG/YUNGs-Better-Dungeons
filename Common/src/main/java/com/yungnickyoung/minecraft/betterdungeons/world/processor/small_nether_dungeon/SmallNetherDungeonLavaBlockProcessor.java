package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;



public class SmallNetherDungeonLavaBlockProcessor implements StructureProcessor {
    public static final SmallNetherDungeonLavaBlockProcessor INSTANCE = new SmallNetherDungeonLavaBlockProcessor();
    public static final MapCodec<SmallNetherDungeonLavaBlockProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().is(Blocks.WOOL.pick(DyeColor.ORANGE))) {
            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.LAVA.defaultBlockState(), null);
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfo.pos()))) {
                return blockInfo;
            }
            levelReader.getChunk(blockInfo.pos()).markPosForPostProcessing(blockInfo.pos()); // Schedule fluid tick
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
