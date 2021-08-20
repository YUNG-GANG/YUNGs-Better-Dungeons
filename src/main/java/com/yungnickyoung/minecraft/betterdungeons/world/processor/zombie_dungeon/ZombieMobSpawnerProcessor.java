package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Sets mob spawners to spawn zombies.
 * Also tweaks the default spawner parameters.
 */
public class ZombieMobSpawnerProcessor extends StructureProcessor {
    public static final ZombieMobSpawnerProcessor INSTANCE = new ZombieMobSpawnerProcessor();
    public static final Codec<ZombieMobSpawnerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof SpawnerBlock) {
            // First initialize NBT if it's null for some reason
            if (blockInfoGlobal.tag == null) {
                CompoundTag newNBT = new CompoundTag();
                newNBT.putShort("SpawnCount", (short) 4);
                newNBT.putString("id", "minecraft:mob_spawner");
                newNBT.putShort("MinSpawnDelay", (short) 200);
                blockInfoGlobal.tag = newNBT;
            }

            // Update the spawner block's NBT
            // SpawnData
            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", "minecraft:zombie");
            blockInfoGlobal.tag.put("SpawnData", spawnData);

            // SpawnPotentials
            CompoundTag spawnPotentials = new CompoundTag();
            CompoundTag spawnPotentialsEntity = new CompoundTag();
            spawnPotentialsEntity.putString("id", "minecraft:zombie");
            spawnPotentials.put("Entity", spawnPotentialsEntity);
            spawnPotentials.put("Weight", IntTag.of(1));
            blockInfoGlobal.tag.getList("SpawnPotentials", spawnPotentials.getType()).clear();
            blockInfoGlobal.tag.getList("SpawnPotentials", spawnPotentials.getType()).add(0, spawnPotentials);

            // Player range (default 16)
            blockInfoGlobal.tag.putShort("RequiredPlayerRange", (short)16);

            // Range at which mobs can spawn from spawner (default 4?)
            blockInfoGlobal.tag.putShort("SpawnRange", (short)4);

            // Max nearby entities allowed (default 6)
            blockInfoGlobal.tag.putShort("MaxNearbyEntities", (short)8);

            // Time between spawn attempts (default 800)
            blockInfoGlobal.tag.putShort("MaxSpawnDelay", (short)800);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_MOB_SPAWNER_PROCESSOR;
    }
}