package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Processes stairs to give them a more varied and ruined look.
 */
@MethodsReturnNonnullByDefault
public class ZombieDungeonStairProcessor extends StructureProcessor {
    public static final ZombieDungeonStairProcessor INSTANCE = new ZombieDungeonStairProcessor();
    public static final Codec<ZombieDungeonStairProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE_STAIRS.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE_STAIRS.getDefaultState(), 0.4f)
        .addBlock(Blocks.COBBLESTONE_SLAB.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.1f)
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.COBBLESTONE_STAIRS) {
            BlockState newBlock = SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos));
            if (newBlock.getBlock() instanceof StairsBlock) {
                newBlock = newBlock
                    .with(StairsBlock.FACING, blockInfoGlobal.state.get(StairsBlock.FACING))
                    .with(StairsBlock.HALF, blockInfoGlobal.state.get(StairsBlock.HALF))
                    .with(StairsBlock.SHAPE, blockInfoGlobal.state.get(StairsBlock.SHAPE));
            }
            blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, newBlock, blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_DUNGEON_STAIR_PROCESSOR;
    }
}