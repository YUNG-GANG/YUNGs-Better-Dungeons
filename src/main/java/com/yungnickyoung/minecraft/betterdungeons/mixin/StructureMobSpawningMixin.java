package com.yungnickyoung.minecraft.betterdungeons.mixin;

import com.yungnickyoung.minecraft.betterdungeons.init.BDModStructureFeatures;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Set some of our structures to exclusively spawn certain enemies.
 */
@Mixin(NoiseBasedChunkGenerator.class)
public class StructureMobSpawningMixin {
    @Inject(method = "getMobsAt", at = @At(value = "HEAD"), cancellable = true)
    private void betterdungeons_structureMobs(Biome biome, StructureFeatureManager structureFeatureManager, MobCategory mobCategory, BlockPos blockPos, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {
        WeightedRandomList<MobSpawnSettings.SpawnerData> pool = null;
        if (mobCategory == MobCategory.MONSTER) {
            if (structureFeatureManager.getStructureAt(blockPos, BDModStructureFeatures.SKELETON_DUNGEON).isValid()) {
                pool = SkeletonDungeonStructure.ENEMIES;
            }
            if (structureFeatureManager.getStructureAt(blockPos, BDModStructureFeatures.SPIDER_DUNGEON).isValid()) {
                pool = SpiderDungeonStructure.ENEMIES;
            }
            if (structureFeatureManager.getStructureAt(blockPos, BDModStructureFeatures.ZOMBIE_DUNGEON).isValid()) {
                pool = ZombieDungeonStructure.ENEMIES;
            }
        }

        if (pool != null && !pool.isEmpty()) {
            cir.setReturnValue(pool);
        }
    }
}
