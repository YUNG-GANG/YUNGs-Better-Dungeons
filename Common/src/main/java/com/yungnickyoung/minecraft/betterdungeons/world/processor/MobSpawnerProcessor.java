package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.yungsapi.world.spawner.MobSpawnerData;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
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
 * Sets mob spawners to spawn the proper mob based on its spawner_mob JSON entry.
 */


public class MobSpawnerProcessor implements StructureProcessor {
    public static final MapCodec<MobSpawnerProcessor> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder
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
                            Util.make(new CompoundTag(), (compoundTag) -> compoundTag.putString("id", spawnerMob.toString())),
                            Optional.empty(),
                            Optional.empty())))
                    .setEntityType(BuiltInRegistries.ENTITY_TYPE
                            .get(spawnerMob)
                            .orElseGet(() -> {
                                BetterDungeonsCommon.LOGGER.error("Unable to find entity type for spawner: {}. Defaulting to zombie...", spawnerMob);
                                return BuiltInRegistries.ENTITY_TYPE.get(Identifier.withDefaultNamespace("zombie")).get();
                            })
                            .value())
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