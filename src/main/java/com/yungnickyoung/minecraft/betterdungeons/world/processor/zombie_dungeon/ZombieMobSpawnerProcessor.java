package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.BaseSpawnerAccessor;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureBlockInfoAccessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

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
            // Create spawner & populate with data
            BaseSpawner spawner = new BaseSpawner() {
                @Override
                public void broadcastEvent(Level level, BlockPos blockPos, int i) {
                    // no-op
                }
            };

            SimpleWeightedRandomList<SpawnData> spawnData = SimpleWeightedRandomList.single(new SpawnData(
                    Util.make(new CompoundTag(), (compoundTag) -> compoundTag.putString("id", "minecraft:zombie")),
                    Optional.empty()));
            ((BaseSpawnerAccessor)spawner).setSpawnPotentials(spawnData);
            ((BaseSpawnerAccessor)spawner).setMaxNearbyEntities(8); // Default is 6
            spawner.setEntityId(Registry.ENTITY_TYPE.get(new ResourceLocation("minecraft:zombie")));

            // Save spawner data to NBT
            CompoundTag nbt = new CompoundTag();
            spawner.save(nbt);

            // Update blockstate
            ((StructureBlockInfoAccessor)blockInfoGlobal).setNbt(nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_MOB_SPAWNER_PROCESSOR;
    }
}