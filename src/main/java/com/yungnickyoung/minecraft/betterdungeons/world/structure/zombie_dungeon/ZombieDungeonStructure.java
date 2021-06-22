package com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon;

import com.google.common.collect.ImmutableList;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ZombieDungeonStructure extends Structure<NoFeatureConfig> {
    public ZombieDungeonStructure() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }


    private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
        new MobSpawnInfo.Spawners(EntityType.ZOMBIE, 100, 4, 15)
    );

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    @Override
    public boolean getDefaultRestrictsSpawnsToInside() {
        return true;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            // Generate from the center of the chunk
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int minY = BDConfig.zombieDungeons.zombieDungeonStartMinY.get();
            int maxY = BDConfig.zombieDungeons.zombieDungeonStartMaxY.get();
            int y = rand.nextInt(maxY - minY) + minY;

            BlockPos blockpos = new BlockPos(x, y, z);
            YungJigsawConfig jigsawConfig = new YungJigsawConfig(
                () -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY).getOrDefault(new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon")),
                10
            );

            // Generate the structure
            YungJigsawManager.assembleJigsawStructure(
                dynamicRegistryManager,
                jigsawConfig,
                chunkGenerator,
                templateManagerIn,
                blockpos,
                this.components,
                this.rand,
                false,
                false
            );

            // Set the bounds of the structure once it's assembled
            this.recalculateStructureSize();

            // Debug log the coordinates of the center starting piece.
            BetterDungeons.LOGGER.debug("Zombie Dungeon at {} {} {}",
                this.components.get(0).getBoundingBox().minX,
                this.components.get(0).getBoundingBox().minY,
                this.components.get(0).getBoundingBox().minZ
            );
        }
    }
}