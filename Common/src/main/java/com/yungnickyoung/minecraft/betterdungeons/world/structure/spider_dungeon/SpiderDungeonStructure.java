package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SpiderDungeonStructure extends StructureFeature<NoneFeatureConfiguration> {
    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public SpiderDungeonStructure() {
        super(NoneFeatureConfiguration.CODEC, context -> {
            // Get starting position with random y-value
            int minY = BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMinY;
            int maxY = BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMaxY;
            WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
            worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
            int y = worldgenRandom.nextInt(maxY - minY) + minY;
            BlockPos startPos = new BlockPos(context.chunkPos().getMiddleBlockX(), y, context.chunkPos().getMiddleBlockZ());

            // Only generate if location is valid
            if (!checkLocation(context, startPos)) {
                return Optional.empty();
            }

            return Optional.of(SpiderDungeonStructure::generatePieces);
        });
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context, BlockPos startPos) {
        return context.validBiome().test(context.chunkGenerator().getNoiseBiome(
                QuartPos.fromBlock(startPos.getX()),
                QuartPos.fromBlock(startPos.getY()),
                QuartPos.fromBlock(startPos.getZ()))
        );
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int y = context.random()
                .nextInt(BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMaxY - BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMinY)
                + BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMinY;

        // Spider dungeons use traditional code-based structure gen instead of Jigsaw
        SpiderDungeonPiece startPiece = new SpiderDungeonBigTunnelPiece(x, y, z);
        structurePiecesBuilder.addPiece(startPiece);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        startPiece.addChildren(startPiece, structurePiecesBuilder, context.random());

    }
}
