package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.spawner.MobSpawnerData;

import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

/**
 * Sets mob spawners to spawn zombies.
 * Also tweaks the default spawner parameters.
 */


public class ZombieMobSpawnerProcessor implements StructureProcessor {
    public static final ZombieMobSpawnerProcessor INSTANCE = new ZombieMobSpawnerProcessor();
    public static final MapCodec<ZombieMobSpawnerProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos templateRelativePos,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() instanceof SpawnerBlock) {
            // Create spawner & populate with data
            MobSpawnerData spawner = MobSpawnerData.builder()
                    .spawnPotentials(WeightedList.of(new SpawnData(
                            Util.make(new CompoundTag(), (compoundTag) -> compoundTag.putString("id", "minecraft:zombie")),
                            Optional.empty(),
                            Optional.empty())))
                    .maxNearbyEntities(8)
                    .setEntityType(net.minecraft.world.entity.EntityTypes.ZOMBIE)
                    .build();
            CompoundTag nbt = spawner.save();
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.SPAWNER.defaultBlockState(), nbt);
        }
        return blockInfoGlobal;
    }

    public MapCodec<? extends StructureProcessor> codec() {
        return StructureProcessorTypeModule.ZOMBIE_MOB_SPAWNER_PROCESSOR;
    }
}
