package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

/**
 * Sets mob spawners to spawn the proper mob based on its spawner_mob JSON entry.
 */
public class MobSpawnerProcessor extends StructureProcessor {
    public static final Codec<MobSpawnerProcessor> CODEC = RecordCodecBuilder.create(codecBuilder -> codecBuilder
        .group(
            Identifier.CODEC
                .fieldOf("spawner_mob")
                .forGetter(MobSpawnerProcessor::getSpawnerMob))
        .apply(codecBuilder, codecBuilder.stable(MobSpawnerProcessor::new)));

    private MobSpawnerProcessor(Identifier spawnerMob) {
        this.spawnerMob = spawnerMob;
    }

    private final Identifier spawnerMob;
    public Identifier getSpawnerMob() {
        return this.spawnerMob;
    }

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof SpawnerBlock) {
            // Update the spawner block's NBT
            // SpawnData
            CompoundTag spawnData = new CompoundTag();
            spawnData.putString("id", this.spawnerMob.toString());
            blockInfoGlobal.tag.remove("SpawnData");
            blockInfoGlobal.tag.put("SpawnData", spawnData);

            // SpawnPotentials
            CompoundTag spawnPotentials = new CompoundTag();
            CompoundTag spawnPotentialsEntity = new CompoundTag();
            spawnPotentialsEntity.putString("id", this.spawnerMob.toString());
            spawnPotentials.put("Entity", spawnPotentialsEntity);
            spawnPotentials.put("Weight", IntTag.of(1));
            blockInfoGlobal.tag.getList("SpawnPotentials", spawnPotentials.getType()).clear();
            blockInfoGlobal.tag.getList("SpawnPotentials", spawnPotentials.getType()).add(0, spawnPotentials);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.MOB_SPAWNER_PROCESSOR;
    }
}