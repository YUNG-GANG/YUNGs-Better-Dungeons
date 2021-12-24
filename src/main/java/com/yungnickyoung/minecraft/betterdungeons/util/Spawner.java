package com.yungnickyoung.minecraft.betterdungeons.util;


import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;

public class Spawner {
    public int spawnDelay = 20;
    public SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();
    public SpawnData nextSpawnData = new SpawnData();
    public int minSpawnDelay = 200;
    public int maxSpawnDelay = 800;
    public int spawnCount = 4;
    public int maxNearbyEntities = 6;
    public int requiredPlayerRange = 16;
    public int spawnRange = 4;

    public void setEntityId(EntityType<?> p_45463_) {
        this.nextSpawnData.getEntityToSpawn().putString("id", Registry.ENTITY_TYPE.getKey(p_45463_).toString());
    }

    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putShort("Delay", (short)this.spawnDelay);
        compoundTag.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        compoundTag.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        compoundTag.putShort("SpawnCount", (short)this.spawnCount);
        compoundTag.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        compoundTag.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
        compoundTag.putShort("SpawnRange", (short)this.spawnRange);
        compoundTag.put("SpawnData", SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(() -> new IllegalStateException("Invalid SpawnData")));
        compoundTag.put("SpawnPotentials", SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
        return compoundTag;
    }
}
