package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.spawner.MobSpawnerData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Sets mob spawners to spawn skeletons w/ swords.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
            // Create spawner & populate with data
            MobSpawnerData spawner = MobSpawnerData.builder()
                    .setEntityType(EntityType.SKELETON)
                    .build();
            spawner.nextSpawnData.getEntityToSpawn().put("HandItems", Util.make(new ListTag(), (handItemsTag) -> {
                ItemStack itemStack = new ItemStack(Items.IRON_SWORD);
                CompoundTag ironSwordNBT = new CompoundTag();
                itemStack.save(ironSwordNBT);
                handItemsTag.add(ironSwordNBT);
            }));
            CompoundTag nbt = spawner.save();
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SPAWNER.defaultBlockState(), nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR;
    }
}
