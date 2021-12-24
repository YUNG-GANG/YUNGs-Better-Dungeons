package com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.util.Spawner;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

/**
 * Sets mob spawners to spawn skeletons.
 * Also buffs spawners to spawn more enemies more frequently, at a greater distance.
 */
public class SkeletonMobSpawnerProcessor extends StructureProcessor {
    public static final SkeletonMobSpawnerProcessor INSTANCE = new SkeletonMobSpawnerProcessor();
    public static final Codec<SkeletonMobSpawnerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() instanceof SpawnerBlock) {
            // Create spawner & populate with data
            Spawner spawner = new Spawner();
            spawner.spawnPotentials = SimpleWeightedRandomList.single(new SpawnData(
                    Util.make(new CompoundTag(), (compoundTag) -> compoundTag.putString("id", "minecraft:skeleton")),
                    Optional.empty()));
            spawner.requiredPlayerRange = 18; // Default is 16
            spawner.maxNearbyEntities = 8; // Default is 6
            spawner.maxSpawnDelay = 650; // Default is 800
            spawner.setEntityId(Registry.ENTITY_TYPE.get(new ResourceLocation("minecraft:skeleton")));

            // Save spawner data to NBT
            CompoundTag nbt = new CompoundTag();
            spawner.save(nbt);

            // Update blockstate
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SPAWNER.defaultBlockState(), nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.SKELETON_MOB_SPAWNER_PROCESSOR;
    }
}
