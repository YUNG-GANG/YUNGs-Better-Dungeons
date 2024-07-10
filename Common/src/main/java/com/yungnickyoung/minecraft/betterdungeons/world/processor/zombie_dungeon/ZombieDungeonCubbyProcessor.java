package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Processes cubbies to give them a more varied and ruined look.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ZombieDungeonCubbyProcessor extends StructureProcessor {
    public static final ZombieDungeonCubbyProcessor INSTANCE = new ZombieDungeonCubbyProcessor();
    public static final MapCodec<ZombieDungeonCubbyProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private static final BlockStateRandomizer SELECTOR = new BlockStateRandomizer(Blocks.COBBLESTONE_STAIRS.defaultBlockState())
        .addBlock(Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState(), 0.4f)
        .addBlock(Blocks.COBBLESTONE_SLAB.defaultBlockState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState(), 0.1f);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() == Blocks.COBBLESTONE_STAIRS) {
            BlockState newBlock = SELECTOR.get(structurePlacementData.getRandom(blockInfoGlobal.pos()));
            if (newBlock.getBlock() instanceof StairBlock) {
                newBlock = newBlock
                    .setValue(StairBlock.FACING, blockInfoGlobal.state().getValue(StairBlock.FACING))
                    .setValue(StairBlock.HALF, blockInfoGlobal.state().getValue(StairBlock.HALF))
                    .setValue(StairBlock.SHAPE, blockInfoGlobal.state().getValue(StairBlock.SHAPE));
            }
            if (newBlock.getBlock() instanceof SlabBlock) {
                if (blockInfoGlobal.state().getValue(StairBlock.HALF) == Half.TOP) {
                    newBlock = newBlock.setValue(SlabBlock.TYPE, SlabType.TOP);
                }
            }
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), newBlock, blockInfoGlobal.nbt());
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.ZOMBIE_DUNGEON_CUBBY_PROCESSOR;
    }
}

