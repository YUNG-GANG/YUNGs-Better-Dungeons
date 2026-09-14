package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;
import net.minecraft.world.item.DyeColor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;
import com.yungnickyoung.minecraft.yungsapi.world.structure.processor.ISafeWorldModifier;

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
import java.util.Optional;

/**
 * Dynamically generates support legs below zombie dungeons.
 * Magenta stained glass is used to mark the positions where the legs will spawn for simplicity.
 * Purpur slabs are to be replaced with smooth stone slabs if air is present.
 */


public class ZombieDungeonLegProcessor implements StructureProcessor,  ISafeWorldModifier {
    public static final ZombieDungeonLegProcessor INSTANCE = new ZombieDungeonLegProcessor();
    public static final MapCodec<ZombieDungeonLegProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static final BlockStateRandomizer LEG_SELECTOR = new BlockStateRandomizer(Blocks.COBBLESTONE.defaultBlockState())
        .addBlock(Blocks.POLISHED_ANDESITE.defaultBlockState(), 0.8f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() == Blocks.STAINED_GLASS.pick(DyeColor.MAGENTA)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfo.pos()))) {
                return blockInfo;
            }

            RandomSource random = structurePlacementData.getRandom(blockInfo.pos());

            // Always replace the glass itself with smooth stone
            Optional<BlockState> blockState = getBlockStateSafe(levelReader, blockInfo.pos());
            if (blockState.isEmpty() || blockState.get().isAir() || blockState.get().liquid()) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.SMOOTH_STONE.defaultBlockState(), null);
            } else {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), blockState.get(), blockInfo.nbt());
            }

            BlockPos.MutableBlockPos mutable = blockInfo.pos().mutable().move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);

            // Generate vertical pillar down
            while (mutable.getY() > levelReader.getMinY()
                    && mutable.getY() < levelReader.getMaxY()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
                levelReader.getChunk(mutable).setBlockState(mutable, LEG_SELECTOR.get(random));
                mutable.move(Direction.DOWN);
                currBlockState = levelReader.getBlockState(mutable);
            }
        } else if (blockInfo.state().getBlock() == Blocks.PURPUR_SLAB) {
            Optional<BlockState> blockState = getBlockStateSafe(levelReader, blockInfo.pos());
            if (blockState.isEmpty() || blockState.get().isAir() || blockState.get().liquid()) {
                blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), null);
            } else {
                blockInfo = null;
            }
        }

        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
