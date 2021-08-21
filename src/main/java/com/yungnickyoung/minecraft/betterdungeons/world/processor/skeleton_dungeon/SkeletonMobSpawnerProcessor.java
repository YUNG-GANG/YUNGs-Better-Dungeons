package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
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
            NbtCompound spawnData = new NbtCompound();
            spawnData.putString("id", "minecraft:skeleton");
            blockInfoGlobal.nbt.remove("SpawnData");
            blockInfoGlobal.nbt.put("SpawnData", spawnData);

            // SpawnPotentials
            NbtCompound spawnPotentials = new NbtCompound();
            NbtCompound spawnPotentialsEntity = new NbtCompound();
            spawnPotentialsEntity.putString("id", "minecraft:skeleton");
            spawnPotentials.put("Entity", spawnPotentialsEntity);
            spawnPotentials.put("Weight", NbtInt.of(1));
            blockInfoGlobal.nbt.getList("SpawnPotentials", spawnPotentials.getType()).clear();
            blockInfoGlobal.nbt.getList("SpawnPotentials", spawnPotentials.getType()).add(0, spawnPotentials);

            // Player range (default 16)
            blockInfoGlobal.nbt.putShort("RequiredPlayerRange", (short)18);

            // Range at which skeletons can spawn from spawner
            blockInfoGlobal.nbt.putShort("SpawnRange", (short)4);

            // Max nearby entities allowed
            blockInfoGlobal.nbt.putShort("MaxNearbyEntities", (short)8);

            // Time between spawn attempts (default 800)
            blockInfoGlobal.nbt.putShort("MaxSpawnDelay", (short)650);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SKELETON_MOB_SPAWNER_PROCESSOR;
    }
}
