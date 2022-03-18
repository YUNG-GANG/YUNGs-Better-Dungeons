package com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;

public class SkeletonDungeonStructure extends StructureFeature<YungJigsawConfig> {
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public SkeletonDungeonStructure() {
        super(YungJigsawConfig.CODEC, context -> {
            // Get starting position with random y-value
            int minY = BetterDungeonsCommon.CONFIG.skeletonDungeons.skeletonDungeonStartMinY;
            int maxY = BetterDungeonsCommon.CONFIG.skeletonDungeons.skeletonDungeonStartMaxY;
            WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
            worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
            int y = worldgenRandom.nextInt(maxY - minY) + minY;
            BlockPos startPos = new BlockPos(context.chunkPos().getMiddleBlockX(), y, context.chunkPos().getMiddleBlockZ());

            // Only generate if location is valid
            if (!checkLocation(context, startPos)) {
                return Optional.empty();
            }

            return YungJigsawManager.assembleJigsawStructure(
                    context,
                    PoolElementStructurePiece::new,
                    startPos,
                    false,
                    false,
                    80);
        });
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<YungJigsawConfig> context, BlockPos startPos) {
        return context.validBiome().test(context.chunkGenerator().getNoiseBiome(
                QuartPos.fromBlock(startPos.getX()),
                QuartPos.fromBlock(startPos.getY()),
                QuartPos.fromBlock(startPos.getZ()))
        );
    }
}
