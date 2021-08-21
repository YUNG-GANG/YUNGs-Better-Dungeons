package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Replaces yellow glass w/ air in skeleton dungeons when air is already there,
 * giving them a more natural, ruined look that opens up to caves.
 * Replaces them w/ stone bricks otherwise.
 */
public class RuinedStoneBrickProcessor extends StructureProcessor {
    public static final RuinedStoneBrickProcessor INSTANCE = new RuinedStoneBrickProcessor();
    public static final Codec<RuinedStoneBrickProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector STONE_BRICK_SELECTOR = new BlockSetSelector(Blocks.STONE_BRICKS.getDefaultState())
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.3f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.2f);

    private static final BlockSetSelector STONE_BRICK_SLAB_SELECTOR = new BlockSetSelector(Blocks.STONE_BRICK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP))
        .addBlock(Blocks.MOSSY_STONE_BRICK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP), 0.3f);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.YELLOW_STAINED_GLASS) {
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, STONE_BRICK_SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos)), blockInfoGlobal.nbt);
            }
        } else if (blockInfoGlobal.state.getBlock() == Blocks.PRISMARINE_BRICK_SLAB) {
            if (world.getBlockState(blockInfoGlobal.pos).isAir()) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            } else {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, STONE_BRICK_SLAB_SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos)), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR;
    }
}
