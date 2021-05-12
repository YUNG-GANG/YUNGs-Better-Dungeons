package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.ModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.util.Banner;
import com.yungnickyoung.minecraft.betterdungeons.world.DungeonContext;
import com.yungnickyoung.minecraft.betterdungeons.world.DungeonType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Replaces some chests with air, ensuring at least 1 per dungeon but no more than 2.
 */
@MethodsReturnNonnullByDefault
public class ChestProcessor extends StructureProcessor {
    public static final ChestProcessor INSTANCE = new ChestProcessor();
    public static final Codec<ChestProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() instanceof ChestBlock) {
            // Fetch thread-local dungeon context
            DungeonContext context = DungeonContext.peek();
            int chestCount = DungeonContext.peek().getChestCount();

            if (chestCount == 0) { // Ensure there is at least one chest
                context.incrementChestCount();
            } else if (chestCount == 1) { // 20% chance of second chest, per chest prop
                Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
                if (random.nextFloat() > .2f) {
                    return new Template.BlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.nbt);
                }
                context.incrementChestCount();
            } else { // Can't spawn more than 2 chests
                return new Template.BlockInfo(blockInfoGlobal.pos, Blocks.AIR.getDefaultState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.SMALL_DUNGEON_CHEST_PROCESSOR;
    }
}
