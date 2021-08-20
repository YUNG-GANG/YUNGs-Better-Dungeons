package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Sets mob spawners to spawn skeletons w/ swords.
 */
public class ZombieTombstoneSpawnerProcessor extends StructureProcessor {
    public static final ZombieTombstoneSpawnerProcessor INSTANCE = new ZombieTombstoneSpawnerProcessor();
    public static final Codec<ZombieTombstoneSpawnerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.BLACK_STAINED_GLASS) {
            // First initialize NBT if it's null for some reason
            if (blockInfoGlobal.tag == null) {
                CompoundTag newNBT = new CompoundTag();
                newNBT.putShort("SpawnCount", (short) 4);
                newNBT.putString("id", "minecraft:mob_spawner");
                newNBT.putShort("MinSpawnDelay", (short) 200);
                blockInfoGlobal.tag = newNBT;
            }

            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SPAWNER.getDefaultState(), blockInfoGlobal.tag);

            // Update the spawner block's NBT
            // SpawnData
            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", "minecraft:skeleton");
            // HandDropChances
            ListTag handDropChances = new ListTag();
            handDropChances.add(FloatTag.of(.2f));
            handDropChances.add(FloatTag.of(0f));
            spawnData.put("HandDropChances", handDropChances);
            // HandItems
            ListTag handItems = new ListTag();
            ItemStack itemStack = new ItemStack(Items.IRON_SWORD);
            CompoundTag ironSwordNBT = new CompoundTag();
            itemStack.setTag(ironSwordNBT);
            handItems.add(ironSwordNBT);
            handItems.add(new CompoundTag());
            spawnData.put("HandItems", handItems);

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
            blockInfoGlobal.tag.putShort("RequiredPlayerRange", (short)16);

            // Range at which skeletons can spawn from spawner (default 4?)
            blockInfoGlobal.tag.putShort("SpawnRange", (short)4);

            // Max nearby entities allowed (default 6)
            blockInfoGlobal.tag.putShort("MaxNearbyEntities", (short)6);

            // Time between spawn attempts (default 800)
            blockInfoGlobal.tag.putShort("MaxSpawnDelay", (short)800);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR;
    }
}
