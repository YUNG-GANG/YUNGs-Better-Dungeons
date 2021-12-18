package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.BaseSpawnerAccessor;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureBlockInfoAccessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
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
 * Sets mob spawners to spawn the proper mob based on its spawner_mob JSON entry.
 */
public class MobSpawnerProcessor extends StructureProcessor {
    public static final Codec<MobSpawnerProcessor> CODEC = RecordCodecBuilder.create(codecBuilder -> codecBuilder
        .group(
            ResourceLocation.CODEC
                .fieldOf("spawner_mob")
                .forGetter(MobSpawnerProcessor::getSpawnerMob))
        .apply(codecBuilder, codecBuilder.stable(MobSpawnerProcessor::new)));

    private MobSpawnerProcessor(ResourceLocation spawnerMob) {
        this.spawnerMob = spawnerMob;
    }

    private final ResourceLocation spawnerMob;
    public ResourceLocation getSpawnerMob() {
        return this.spawnerMob;
    }

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
                    Util.make(new CompoundTag(), (compoundTag) -> compoundTag.putString("id", spawnerMob.toString())),
                    Optional.empty()));
            ((BaseSpawnerAccessor)spawner).setSpawnPotentials(spawnData);
            spawner.setEntityId(Registry.ENTITY_TYPE.get(spawnerMob));

            // Save spawner data to NBT
            CompoundTag nbt = new CompoundTag();
            spawner.save(nbt);

            // Update blockstate
            ((StructureBlockInfoAccessor)blockInfoGlobal).setNbt(nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.MOB_SPAWNER_PROCESSOR;
    }
}