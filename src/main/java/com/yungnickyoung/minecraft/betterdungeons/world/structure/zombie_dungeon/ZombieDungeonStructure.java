package com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.List;
import java.util.Optional;

public class ZombieDungeonStructure extends StructureFeature<YungJigsawConfig> {
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

    public static final WeightedRandomList<MobSpawnSettings.SpawnerData> ENEMIES = WeightedRandomList.create(
            new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 100, 4, 15));

    public ZombieDungeonStructure() {
        super(YungJigsawConfig.CODEC, context -> {
            // Get starting position with random y-value
            int minY = BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonStartMinY;
            int maxY = BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonStartMaxY;
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