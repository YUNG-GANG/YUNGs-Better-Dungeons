package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.List;

public class SpiderDungeonStructure extends StructureFeature<DefaultFeatureConfig> {
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

    public SpiderDungeonStructure() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    // Spider dungeons can only spawn spiders & cave spiders
    private static final List<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = ImmutableList.of(
        new SpawnSettings.SpawnEntry(EntityType.SPIDER, 100, 4, 15),
        new SpawnSettings.SpawnEntry(EntityType.CAVE_SPIDER, 50, 4, 8)
    );

    @Override
    public List<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return STRUCTURE_MONSTERS;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> structure, int chunkX, int chunkZ, BlockBox blockBox, int references, long seedInseed) {
            super(structure, chunkX, chunkZ, blockBox, references, seedInseed);
        }

        @Override
        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biomeIn, DefaultFeatureConfig config) {
            int startX = chunkX << 4;
            int startZ = chunkZ << 4;

            // Spider dungeons use traditional code-based structure gen instead of Jigsaw
            StructurePiece startPiece = new SpiderDungeonBigTunnelPiece(startX, startZ, this.random);
            this.children.add(startPiece);
            startPiece.fillOpenings(startPiece, this.children, this.random);

            // Set the bounds of the structure once it's assembled
            this.setBoundingBoxFromChildren();

            // Debug log the coordinates of the center starting piece.
            BetterDungeons.LOGGER.debug("Spider Dungeon at {} {} {}",
                this.children.get(0).getBoundingBox().minX,
                this.children.get(0).getBoundingBox().minY,
                this.children.get(0).getBoundingBox().minZ
            );
        }
    }
}
