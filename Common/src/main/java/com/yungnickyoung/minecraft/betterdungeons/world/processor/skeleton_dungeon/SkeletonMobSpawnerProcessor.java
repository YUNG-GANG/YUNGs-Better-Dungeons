package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import com.mojang.serialization.MapCodec;
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
 * Sets mob spawners to spawn skeletons.
 * Also buffs spawners to spawn more enemies more frequently, at a greater distance.
 */


public class SkeletonMobSpawnerProcessor implements StructureProcessor {
    public static final SkeletonMobSpawnerProcessor INSTANCE = new SkeletonMobSpawnerProcessor();
    public static final MapCodec<SkeletonMobSpawnerProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                                                                                          BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfo,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfo.state().getBlock() instanceof SpawnerBlock) {
            // Create spawner & populate with data
            MobSpawnerData spawner = MobSpawnerData.builder()
                    .spawnPotentials(WeightedList.of(new SpawnData(
                            Util.make(new CompoundTag(), (compoundTag) -> compoundTag.putString("id", "minecraft:skeleton")),
                            Optional.empty(),
                            Optional.empty())))
                    .requiredPlayerRange(18)
                    .maxNearbyEntities(8)
                    .maxSpawnDelay(650)
                    .setEntityType(BuiltInRegistries.ENTITY_TYPE.get(Identifier.fromNamespaceAndPath("minecraft", "skeleton")).orElseThrow().value())
                    .build();
            CompoundTag nbt = spawner.save();
            blockInfo = new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.SPAWNER.defaultBlockState(), nbt);
        }
        return blockInfo;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
