package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SpiderDungeonStructure extends StructureFeature<NoneFeatureConfiguration> {
    /**
     * Lists of whitelisted dimensions and blacklisted biomes.
     * Will be reinitialized later w/ values from config.
     */
    public static List<String> whitelistedDimensions = Lists.newArrayList("minecraft:overworld");
    public static List<String> blacklistedBiomes = Lists.newArrayList(
        "minecraft:ocean", "minecraft:frozen_ocean", "minecraft:deep_ocean",
        "minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:cold_ocean",
        "minecraft:deep_lukewarm_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean",
        "minecraft:beach", "minecraft:snowy_beach",
        "minecraft:river", "minecraft:frozen_river"
    );

    // Spider dungeons can only spawn spiders & cave spiders
    public static final List<MobSpawnSettings.SpawnerData> ENEMIES = List.of(
            new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 15),
            new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 50, 4, 8));

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public SpiderDungeonStructure() {
        super(NoneFeatureConfiguration.CODEC, context -> {
            // Get starting position with random y-value
            int minY = BDConfig.spiderDungeons.spiderDungeonStartMinY.get();
            int maxY = BDConfig.spiderDungeons.spiderDungeonStartMaxY.get();
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
                .nextInt(BDConfig.spiderDungeons.spiderDungeonStartMaxY.get() - BDConfig.spiderDungeons.spiderDungeonStartMinY.get())
                + BDConfig.spiderDungeons.spiderDungeonStartMinY.get();

        // Spider dungeons use traditional code-based structure gen instead of Jigsaw
        SpiderDungeonPiece startPiece = new SpiderDungeonBigTunnelPiece(x, y, z);
        structurePiecesBuilder.addPiece(startPiece);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        startPiece.addChildren(startPiece, structurePiecesBuilder, context.random());

    }
}
