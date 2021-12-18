package com.yungnickyoung.minecraft.betterdungeons.mixin.accessor;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BaseSpawner.class)
public interface BaseSpawnerAccessor {
    @Accessor
    SimpleWeightedRandomList<SpawnData> getSpawnPotentials();

    @Accessor
    void setSpawnPotentials(SimpleWeightedRandomList<SpawnData> spawnPotentials);

    @Accessor
    int getRequiredPlayerRange();

    @Accessor
    void setRequiredPlayerRange(int requiredPlayerRange);

    @Accessor
    int getMaxNearbyEntities();

    @Accessor
    void setMaxNearbyEntities(int maxNearbyEntities);

    @Accessor
    int getMaxSpawnDelay();

    @Accessor
    void setMaxSpawnDelay(int maxSpawnDelay);
}
