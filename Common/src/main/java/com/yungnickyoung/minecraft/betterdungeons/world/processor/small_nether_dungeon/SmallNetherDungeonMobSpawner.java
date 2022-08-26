package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.spawner.MobSpawnerData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

public class SmallNetherDungeonMobSpawner extends StructureProcessor {
    public static final Codec<SmallNetherDungeonMobSpawner> CODEC = RecordCodecBuilder.create(codecBuilder -> codecBuilder
            .group(
                    ResourceLocation.CODEC
                            .fieldOf("spawner_mob")
                            .forGetter(SmallNetherDungeonMobSpawner::getSpawnerMob))
            .apply(codecBuilder, codecBuilder.stable(SmallNetherDungeonMobSpawner::new)));

    private SmallNetherDungeonMobSpawner(ResourceLocation spawnerMob) {
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
            MobSpawnerData spawner = MobSpawnerData.builder()
                    .spawnPotentials(SimpleWeightedRandomList.single(new SpawnData(
                            Util.make(new CompoundTag(), (compoundTag) -> {
                                compoundTag.putString("id", spawnerMob.toString());
                                if (spawnerMob.toString().equals("minecraft:wither_skeleton")) {
                                    compoundTag.put("ArmorItems", Util.make(new ListTag(), (armorItemsNbt) -> {
                                        ItemStack itemStack = new ItemStack(Items.NETHERITE_BOOTS);

                                        // Boots
                                        CompoundTag bootsNbt = new CompoundTag();
                                        itemStack.save(bootsNbt);
                                        armorItemsNbt.add(bootsNbt);

                                        // Leggings
                                        itemStack = new ItemStack(Items.NETHERITE_LEGGINGS);
                                        CompoundTag leggingsNbt = new CompoundTag();
                                        itemStack.save(leggingsNbt);
                                        armorItemsNbt.add(leggingsNbt);

                                        // Chestplate
                                        itemStack = new ItemStack(Items.NETHERITE_CHESTPLATE);
                                        CompoundTag chestNbt = new CompoundTag();
                                        itemStack.save(chestNbt);
                                        armorItemsNbt.add(chestNbt);

                                        // Helmet
                                        itemStack = new ItemStack(Items.NETHERITE_HELMET);
                                        CompoundTag helmetNbt = new CompoundTag();
                                        itemStack.save(helmetNbt);
                                        armorItemsNbt.add(helmetNbt);
                                    }));
                                    compoundTag.put("ArmorDropChances", Util.make(new ListTag(), (armorDropChancesNbt) -> {
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                    }));
                                    compoundTag.put("HandItems", Util.make(new ListTag(), (handItemsNbt) -> {
                                        ItemStack itemStack = new ItemStack(Items.STONE_SWORD);
                                        CompoundTag stoneSwordNbt = new CompoundTag();
                                        itemStack.save(stoneSwordNbt);
                                        handItemsNbt.add(stoneSwordNbt);
                                    }));
                                }
                            }),
                            Optional.empty())))
                    .setEntityType(Registry.ENTITY_TYPE.get(spawnerMob))
                    .build();
            if (spawnerMob.toString().equals("minecraft:wither_skeleton")) {
                spawner.nextSpawnData.getEntityToSpawn().put("ArmorItems", Util.make(new ListTag(), (armorItemsNbt) -> {
                    ItemStack itemStack = new ItemStack(Items.NETHERITE_BOOTS);

                    // Boots
                    CompoundTag bootsNbt = new CompoundTag();
                    itemStack.save(bootsNbt);
                    armorItemsNbt.add(bootsNbt);

                    // Leggings
                    itemStack = new ItemStack(Items.NETHERITE_LEGGINGS);
                    CompoundTag leggingsNbt = new CompoundTag();
                    itemStack.save(leggingsNbt);
                    armorItemsNbt.add(leggingsNbt);

                    // Chestplate
                    itemStack = new ItemStack(Items.NETHERITE_CHESTPLATE);
                    CompoundTag chestNbt = new CompoundTag();
                    itemStack.save(chestNbt);
                    armorItemsNbt.add(chestNbt);

                    // Helmet
                    itemStack = new ItemStack(Items.NETHERITE_HELMET);
                    CompoundTag helmetNbt = new CompoundTag();
                    itemStack.save(helmetNbt);
                    armorItemsNbt.add(helmetNbt);
                }));
                spawner.nextSpawnData.getEntityToSpawn().put("ArmorDropChances", Util.make(new ListTag(), (armorDropChancesNbt) -> {
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                }));
                spawner.nextSpawnData.getEntityToSpawn().put("HandItems", Util.make(new ListTag(), (handItemsNbt) -> {
                    ItemStack itemStack = new ItemStack(Items.STONE_SWORD);
                    CompoundTag stoneSwordNbt = new CompoundTag();
                    itemStack.save(stoneSwordNbt);
                    handItemsNbt.add(stoneSwordNbt);
                }));
            }
            CompoundTag nbt = spawner.save();
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.SPAWNER.defaultBlockState(), nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.SMALL_NETHER_DUNGEON_MOB_SPAWNER_PROCESSOR;
    }
}
