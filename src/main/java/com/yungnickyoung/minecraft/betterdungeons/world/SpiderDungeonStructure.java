package com.yungnickyoung.minecraft.betterdungeons.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.jigsaw.JigsawConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.jigsaw.JigsawManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpiderDungeonStructure extends Structure<NoFeatureConfig> {
    public SpiderDungeonStructure(Codec<NoFeatureConfig> p_i231996_1_) {
        super(p_i231996_1_);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return SpiderDungeonStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int minY = BDConfig.spiderDungeons.spiderDungeonStartMinY.get();
            int maxY = BDConfig.spiderDungeons.spiderDungeonStartMaxY.get();
            int y = rand.nextInt(maxY - minY) + minY;

            BlockPos blockpos = new BlockPos(x, y, z);
            JigsawConfig villageConfig = new JigsawConfig(
                () -> dynamicRegistryManager.getRegistry(Registry.JIGSAW_POOL_KEY)
                    .getOrDefault(new ResourceLocation(BetterDungeons.MOD_ID, "starts/spider")),
                BDConfig.spiderDungeons.spiderDungeonSize.get()
            );

            // Generate the structure
            JigsawManager.assembleJigsawStructure(
                dynamicRegistryManager,
                villageConfig,
                chunkGenerator,
                templateManagerIn,
                blockpos,
                this.components,
                this.rand,
                false,
                false
            );

            // Sets the bounds of the structure once you are finished.
            this.recalculateStructureSize();

            // Debug log the coordinates of the center starting piece.
            BetterDungeons.LOGGER.debug("Spider Dungeon at {} {} {}",
                this.components.get(0).getBoundingBox().minX,
                this.components.get(0).getBoundingBox().minY,
                this.components.get(0).getBoundingBox().minZ
            );
        }
    }
}
