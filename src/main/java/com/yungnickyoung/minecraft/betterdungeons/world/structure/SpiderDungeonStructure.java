package com.yungnickyoung.minecraft.betterdungeons.world.structure;

import com.google.common.collect.ImmutableList;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.jigsaw.JigsawConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.jigsaw.JigsawManager;
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
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class SpiderDungeonStructure extends Structure<NoFeatureConfig> {
    public SpiderDungeonStructure() {
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
        new MobSpawnInfo.Spawners(EntityType.SPIDER, 100, 4, 15)
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
        @ParametersAreNonnullByDefault
        public void func_230364_a_(DynamicRegistries registryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            BlockPos startingPos = new BlockPos((chunkX << 4), 40, (chunkZ << 4));
            StructurePiece startPiece = new SpiderDungeonPiece(startingPos);
            this.components.add(startPiece);
            startPiece.buildComponent(startPiece, this.components, this.rand);

//             Set the bounds of the structure once it's assembled
            this.recalculateStructureSize();

//            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
//            int x = (chunkX << 4) + 7;
//            int z = (chunkZ << 4) + 7;
//
//            int y = 80;
//
//            BlockPos blockpos = new BlockPos(x, y, z);
//            JigsawConfig jigsawConfig = new JigsawConfig(
//                () -> registryManager.getRegistry(Registry.JIGSAW_POOL_KEY).getOrDefault(new ResourceLocation(BetterDungeons.MOD_ID, "spider_start")),
//                20
//            );
//
//            // Generate the structure
//            JigsawManager.assembleJigsawStructure(
//                registryManager,
//                jigsawConfig,
//                chunkGenerator,
//                templateManagerIn,
//                blockpos,
//                this.components,
//                this.rand,
//                false,
//                false
//            );
//
//            // Set the bounds of the structure once it's assembled
//            this.recalculateStructureSize();

            // Debug log the coordinates of the center starting piece.
            BetterDungeons.LOGGER.debug("Spider Dungeon at {} {} {}",
                this.components.get(0).getBoundingBox().minX,
                this.components.get(0).getBoundingBox().minY,
                this.components.get(0).getBoundingBox().minZ
            );
        }
    }
}
