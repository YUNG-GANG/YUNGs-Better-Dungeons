package com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.List;

public class SmallDungeonStructure extends StructureFeature<DefaultFeatureConfig> {
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

    public SmallDungeonStructure() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> structure, ChunkPos pos, int references, long seed) {
            super(structure, pos, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView heightLimitView) {
            // Generate from the center of the chunk
            int x = (pos.x << 4) + 7;
            int z = (pos.z << 4) + 7;

            int minY = BetterDungeons.CONFIG.betterDungeons.smallDungeon.smallDungeonMinY;
            int maxY = BetterDungeons.CONFIG.betterDungeons.smallDungeon.smallDungeonMaxY;
            int y = this.random.nextInt(maxY - minY) + minY;

            BlockPos blockpos = new BlockPos(x, y, z);
            YungJigsawConfig jigsawConfig = new YungJigsawConfig(
                () -> dynamicRegistryManager.get(Registry.STRUCTURE_POOL_KEY)
                    .get(new Identifier(BetterDungeons.MOD_ID, "small_dungeon")),
                10
            );

            // Generate the structure
            YungJigsawManager.assembleJigsawStructure(
                dynamicRegistryManager,
                jigsawConfig,
                PoolStructurePiece::new,
                chunkGenerator,
                structureManager,
                blockpos,
                this,
                this.random,
                false,
                false,
                heightLimitView
            );

            // Set the bounds of the structure once it's assembled
            this.setBoundingBoxFromChildren();

            // Debug log the coordinates of the center starting piece.
            BetterDungeons.LOGGER.debug("Small Dungeon at {} {} {}",
                this.children.get(0).getBoundingBox().getMinX(),
                this.children.get(0).getBoundingBox().getMinY(),
                this.children.get(0).getBoundingBox().getMinZ()
            );
        }
    }
}
