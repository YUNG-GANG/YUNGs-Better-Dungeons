package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Dynamically generates support legs below zombie dungeons.
 * Magenta stained glass is used to mark the positions where the legs will spawn for simplicity.
 * Purpur slabs are to be replaced with smooth stone slabs if air is present.
 */
@MethodsReturnNonnullByDefault
public class ZombieDungeonLegProcessor extends StructureProcessor {
    public static final ZombieDungeonLegProcessor INSTANCE = new ZombieDungeonLegProcessor();
    public static final Codec<ZombieDungeonLegProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector LEG_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE.getDefaultState())
        .addBlock(Blocks.POLISHED_ANDESITE.getDefaultState(), 0.8f);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.MAGENTA_STAINED_GLASS) {
            ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
            IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            // Always replace the glass itself with smooth stone
            if (worldReader.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.SMOOTH_STONE.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, worldReader.getBlockState(blockInfoGlobal.pos), blockInfoGlobal.nbt);
            }

            // Generate vertical pillar down
            BlockPos.Mutable mutable = blockInfoGlobal.pos.down().toMutable();
            BlockState currBlock = worldReader.getBlockState(mutable);
            while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
                currentChunk.setBlockState(mutable, LEG_SELECTOR.get(random), false);
                mutable.move(Direction.DOWN);
                currBlock = worldReader.getBlockState(mutable);
            }
        } else if (blockInfoGlobal.state.getBlock() == Blocks.PURPUR_SLAB) {
            if (worldReader.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = null;
            }
        }

        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_DUNGEON_LEG_PROCESSOR;
    }
}
