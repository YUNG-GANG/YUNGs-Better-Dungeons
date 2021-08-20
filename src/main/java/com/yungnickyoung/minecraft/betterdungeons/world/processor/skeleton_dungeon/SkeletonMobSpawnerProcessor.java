package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

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
 * Sets mob spawners to spawn skeletons.
 * Also buffs spawners to spawn more enemies more frequently, at a greater distance.
 */
public class SkeletonMobSpawnerProcessor extends StructureProcessor {
    public static final SkeletonMobSpawnerProcessor INSTANCE = new SkeletonMobSpawnerProcessor();
    public static final Codec<SkeletonMobSpawnerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof SpawnerBlock) {
            // Update the spawner block's NBT
            // SpawnData
            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", "minecraft:skeleton");
            blockInfoGlobal.tag.remove("SpawnData");
            blockInfoGlobal.tag.put("SpawnData", spawnData);

            // SpawnPotentials
            CompoundTag spawnPotentials = new CompoundTag();
            CompoundTag spawnPotentialsEntity = new CompoundTag();
            spawnPotentialsEntity.putString("id", "minecraft:skeleton");
            spawnPotentials.put("Entity", spawnPotentialsEntity);
            spawnPotentials.put("Weight", IntTag.of(1));
            blockInfoGlobal.tag.getList("SpawnPotentials", spawnPotentials.getType()).clear();
            blockInfoGlobal.tag.getList("SpawnPotentials", spawnPotentials.getType()).add(0, spawnPotentials);

            // Player range (default 16)
            blockInfoGlobal.tag.putShort("RequiredPlayerRange", (short)18);

            // Range at which skeletons can spawn from spawner
            blockInfoGlobal.tag.putShort("SpawnRange", (short)4);

            // Max nearby entities allowed
            blockInfoGlobal.tag.putShort("MaxNearbyEntities", (short)8);

            // Time between spawn attempts (default 800)
            blockInfoGlobal.tag.putShort("MaxSpawnDelay", (short)650);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SKELETON_MOB_SPAWNER_PROCESSOR;
    }
}
