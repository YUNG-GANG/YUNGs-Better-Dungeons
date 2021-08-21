package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
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
            if (blockInfoGlobal.nbt == null) {
                NbtCompound newNBT = new NbtCompound();
                newNBT.putShort("SpawnCount", (short) 4);
                newNBT.putString("id", "minecraft:mob_spawner");
                newNBT.putShort("MinSpawnDelay", (short) 200);
                blockInfoGlobal.nbt = newNBT;
            }

            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SPAWNER.getDefaultState(), blockInfoGlobal.nbt);

            // Update the spawner block's NBT
            // SpawnData
            NbtCompound spawnData = new NbtCompound();
            spawnData.putString("id", "minecraft:skeleton");
            // HandDropChances
            NbtList handDropChances = new NbtList();
            handDropChances.add(NbtFloat.of(.2f));
            handDropChances.add(NbtFloat.of(0f));
            spawnData.put("HandDropChances", handDropChances);
            // HandItems
            NbtList handItems = new NbtList();
            ItemStack itemStack = new ItemStack(Items.IRON_SWORD);
            NbtCompound ironSwordNBT = new NbtCompound();
            itemStack.writeNbt(ironSwordNBT);
            handItems.add(ironSwordNBT);
            handItems.add(new NbtCompound());
            spawnData.put("HandItems", handItems);

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
