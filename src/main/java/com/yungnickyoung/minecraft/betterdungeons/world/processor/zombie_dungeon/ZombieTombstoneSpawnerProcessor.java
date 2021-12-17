package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureBlockInfoAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Sets mob spawners to spawn skeletons w/ swords.
 */
public class ZombieTombstoneSpawnerProcessor extends StructureProcessor {
    public static final ZombieTombstoneSpawnerProcessor INSTANCE = new ZombieTombstoneSpawnerProcessor();
    public static final Codec<ZombieTombstoneSpawnerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.BLACK_STAINED_GLASS) {
            // First initialize NBT if it's null for some reason
            if (blockInfoGlobal.nbt == null) {
                CompoundTag newNBT = new CompoundTag();
                newNBT.putShort("SpawnCount", (short) 4);
                newNBT.putString("id", "minecraft:mob_spawner");
                newNBT.putShort("MinSpawnDelay", (short) 200);
                ((StructureBlockInfoAccessor)blockInfoGlobal).setNbt(newNBT);
            }

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SPAWNER.defaultBlockState(), blockInfoGlobal.nbt);

            // Update the spawner block's NBT
            // SpawnData
            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", "minecraft:skeleton");
            // HandDropChances
            ListTag handDropChances = new ListTag();
            handDropChances.add(FloatTag.valueOf(.2f));
            handDropChances.add(FloatTag.valueOf(0f));
            spawnData.put("HandDropChances", handDropChances);
            // HandItems
            ListTag handItems = new ListTag();
            ItemStack itemStack = new ItemStack(Items.IRON_SWORD);
            CompoundTag ironSwordNBT = new CompoundTag();
            itemStack.save(ironSwordNBT);
            handItems.add(ironSwordNBT);
            handItems.add(new CompoundTag());
            spawnData.put("HandItems", handItems);

            blockInfoGlobal.nbt.put("SpawnData", spawnData);

            // SpawnPotentials
            CompoundTag spawnPotentials = new CompoundTag();
            CompoundTag spawnPotentialsEntity = new CompoundTag();
            spawnPotentialsEntity.putString("id", "minecraft:skeleton");
            spawnPotentials.put("Entity", spawnPotentialsEntity);
            spawnPotentials.put("Weight", IntTag.valueOf(1));
            blockInfoGlobal.nbt.getList("SpawnPotentials", spawnPotentials.getId()).clear();
            blockInfoGlobal.nbt.getList("SpawnPotentials", spawnPotentials.getId()).add(0, spawnPotentials);

            // Player range (default 16)
            blockInfoGlobal.nbt.putShort("RequiredPlayerRange", (short)16);

            // Range at which skeletons can spawn from spawner (default 4?)
            blockInfoGlobal.nbt.putShort("SpawnRange", (short)4);

            // Max nearby entities allowed (default 6)
            blockInfoGlobal.nbt.putShort("MaxNearbyEntities", (short)6);

            // Time between spawn attempts (default 800)
            blockInfoGlobal.nbt.putShort("MaxSpawnDelay", (short)800);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR;
    }
}
