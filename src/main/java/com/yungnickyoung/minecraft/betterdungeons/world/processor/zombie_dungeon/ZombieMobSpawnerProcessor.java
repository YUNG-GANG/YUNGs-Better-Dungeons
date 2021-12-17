package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureBlockInfoAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Sets mob spawners to spawn zombies.
 * Also tweaks the default spawner parameters.
 */
public class ZombieMobSpawnerProcessor extends StructureProcessor {
    public static final ZombieMobSpawnerProcessor INSTANCE = new ZombieMobSpawnerProcessor();
    public static final Codec<ZombieMobSpawnerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof SpawnerBlock) {
            // First initialize NBT if it's null for some reason
            if (blockInfoGlobal.nbt == null) {
                CompoundTag newNBT = new CompoundTag();
                newNBT.putShort("SpawnCount", (short) 4);
                newNBT.putString("id", "minecraft:mob_spawner");
                newNBT.putShort("MinSpawnDelay", (short) 200);
                ((StructureBlockInfoAccessor)blockInfoGlobal).setNbt(newNBT);
            }

            // Update the spawner block's NBT
            // SpawnData
            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", "minecraft:zombie");
            blockInfoGlobal.nbt.put("SpawnData", spawnData);

            // SpawnPotentials
            CompoundTag spawnPotentials = new CompoundTag();
            CompoundTag spawnPotentialsEntity = new CompoundTag();
            spawnPotentialsEntity.putString("id", "minecraft:zombie");
            spawnPotentials.put("Entity", spawnPotentialsEntity);
            spawnPotentials.put("Weight", IntTag.valueOf(1));
            blockInfoGlobal.nbt.getList("SpawnPotentials", spawnPotentials.getId()).clear();
            blockInfoGlobal.nbt.getList("SpawnPotentials", spawnPotentials.getId()).add(0, spawnPotentials);

            // Player range (default 16)
            blockInfoGlobal.nbt.putShort("RequiredPlayerRange", (short)16);

            // Range at which mobs can spawn from spawner (default 4?)
            blockInfoGlobal.nbt.putShort("SpawnRange", (short)4);

            // Max nearby entities allowed (default 6)
            blockInfoGlobal.nbt.putShort("MaxNearbyEntities", (short)8);

            // Time between spawn attempts (default 800)
            blockInfoGlobal.nbt.putShort("MaxSpawnDelay", (short)800);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_MOB_SPAWNER_PROCESSOR;
    }
}