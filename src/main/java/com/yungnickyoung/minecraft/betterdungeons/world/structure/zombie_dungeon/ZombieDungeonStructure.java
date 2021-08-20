package com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.List;

public class ZombieDungeonStructure extends StructureFeature<DefaultFeatureConfig> {
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

    public ZombieDungeonStructure() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    private static final List<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = ImmutableList.of(
        new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 100, 4, 15)
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
            // Generate from the center of the chunk
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int minY = BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonStartMinY;
            int maxY = BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonStartMaxY;
            int y = this.random.nextInt(maxY - minY) + minY;

            BlockPos blockpos = new BlockPos(x, y, z);
            YungJigsawConfig jigsawConfig = new YungJigsawConfig(
                () -> dynamicRegistryManager.get(Registry.TEMPLATE_POOL_WORLDGEN)
                    .get(new Identifier(BetterDungeons.MOD_ID, "zombie_dungeon")),
                20
            );

            // Generate the structure
            YungJigsawManager.assembleJigsawStructure(
                dynamicRegistryManager,
                jigsawConfig,
                PoolStructurePiece::new,
                chunkGenerator,
                structureManager,
                blockpos,
                this.children,
                this.random,
                false,
                false
            );

            // Set the bounds of the structure once it's assembled
            this.setBoundingBoxFromChildren();

            // Debug log the coordinates of the center starting piece.
            BetterDungeons.LOGGER.debug("Zombie Dungeon at {} {} {}",
                this.children.get(0).getBoundingBox().minX,
                this.children.get(0).getBoundingBox().minY,
                this.children.get(0).getBoundingBox().minZ
            );
        }
    }
}