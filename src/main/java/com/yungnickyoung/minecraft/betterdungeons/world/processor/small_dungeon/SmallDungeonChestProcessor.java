package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.world.DungeonContext;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Random;

/**
 * Replaces some chests with air, ensuring at least 1 per dungeon but no more than 2.
 */
public class SmallDungeonChestProcessor extends StructureProcessor {
    public static final SmallDungeonChestProcessor INSTANCE = new SmallDungeonChestProcessor();
    public static final Codec<SmallDungeonChestProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof ChestBlock) {
            // Fetch thread-local dungeon context
            DungeonContext context = DungeonContext.peek();
            int chestCount = DungeonContext.peek().getChestCount();

            if (chestCount < BetterDungeons.CONFIG.betterDungeons.smallDungeon.chestMinCount) { // Ensure there is at least minimum amount of chests
                context.incrementChestCount();
            } else if (chestCount < BetterDungeons.CONFIG.betterDungeons.smallDungeon.chestMaxCount) { // 20% chance of additional chest, per chest prop
                Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
                if (random.nextFloat() > .2f) {
                    return new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
                }
                context.incrementChestCount();
            } else { // Can't spawn more than max chests
                return new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SMALL_DUNGEON_CHEST_PROCESSOR;
    }
}
