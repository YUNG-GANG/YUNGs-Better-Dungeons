package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.spawner.MobSpawnerData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallNetherDungeonMobSpawner extends StructureProcessor {
    public static final MapCodec<SmallNetherDungeonMobSpawner> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder
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
        if (blockInfoGlobal.state().getBlock() instanceof SpawnerBlock) {
            // Create spawner & populate with data
            MobSpawnerData spawner = MobSpawnerData.builder()
                    .spawnPotentials(SimpleWeightedRandomList.single(new SpawnData(
                            Util.make(new CompoundTag(), (compoundTag) -> {
                                compoundTag.putString("id", spawnerMob.toString());
                                if (spawnerMob.toString().equals("minecraft:wither_skeleton")) {
                                    compoundTag.put("ArmorItems", Util.make(new ListTag(), (armorItemsNbt) -> {
                                        // Boots
                                        Tag bootsNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_BOOTS)).getOrThrow();
                                        armorItemsNbt.add(bootsNbt);

                                        // Leggings
                                        Tag leggingsNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_LEGGINGS)).getOrThrow();
                                        armorItemsNbt.add(leggingsNbt);

                                        // Chestplate
                                        Tag chestNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_CHESTPLATE)).getOrThrow();
                                        armorItemsNbt.add(chestNbt);

                                        // Helmet
                                        Tag helmetNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_HELMET)).getOrThrow();
                                        armorItemsNbt.add(helmetNbt);
                                    }));
                                    compoundTag.put("ArmorDropChances", Util.make(new ListTag(), (armorDropChancesNbt) -> {
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                        armorDropChancesNbt.add(FloatTag.valueOf(0));
                                    }));
                                    compoundTag.put("HandItems", Util.make(new ListTag(), (handItemsNbt) -> {
                                        Tag stoneSwordNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.STONE_SWORD)).getOrThrow();
                                        handItemsNbt.add(stoneSwordNbt);
                                    }));
                                    if (!BetterDungeonsCommon.CONFIG.smallNetherDungeons.witherSkeletonsDropWitherSkulls) {
                                        compoundTag.putString("DeathLootTable", "minecraft:empty");
                                    }
                                } else if (spawnerMob.toString().equals("minecraft:blaze")) {
                                    if (!BetterDungeonsCommon.CONFIG.smallNetherDungeons.blazesDropBlazeRods) {
                                        compoundTag.putString("DeathLootTable", "minecraft:empty");
                                    }
                                }
                            }),
                            Optional.empty(),
                            Optional.empty())))
                    .setEntityType(BuiltInRegistries.ENTITY_TYPE.get(spawnerMob))
                    .build();
            if (spawnerMob.toString().equals("minecraft:wither_skeleton")) {
                spawner.nextSpawnData.getEntityToSpawn().put("ArmorItems", Util.make(new ListTag(), (armorItemsNbt) -> {
                    // Boots
                    Tag bootsNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_BOOTS)).getOrThrow();
                    armorItemsNbt.add(bootsNbt);

                    // Leggings
                    Tag leggingsNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_LEGGINGS)).getOrThrow();
                    armorItemsNbt.add(leggingsNbt);

                    // Chestplate
                    Tag chestNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_CHESTPLATE)).getOrThrow();
                    armorItemsNbt.add(chestNbt);

                    // Helmet
                    Tag helmetNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.NETHERITE_HELMET)).getOrThrow();
                    armorItemsNbt.add(helmetNbt);
                }));
                spawner.nextSpawnData.getEntityToSpawn().put("ArmorDropChances", Util.make(new ListTag(), (armorDropChancesNbt) -> {
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                    armorDropChancesNbt.add(FloatTag.valueOf(0));
                }));
                spawner.nextSpawnData.getEntityToSpawn().put("HandItems", Util.make(new ListTag(), (handItemsNbt) -> {
                    Tag stoneSwordNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, new ItemStack(Items.STONE_SWORD)).getOrThrow();
                    handItemsNbt.add(stoneSwordNbt);
                }));
                if (!BetterDungeonsCommon.CONFIG.smallNetherDungeons.witherSkeletonsDropWitherSkulls) {
                    spawner.nextSpawnData.getEntityToSpawn().putString("DeathLootTable", "minecraft:empty");
                }
            } else if (spawnerMob.toString().equals("minecraft:blaze")) {
                if (!BetterDungeonsCommon.CONFIG.smallNetherDungeons.blazesDropBlazeRods) {
                    spawner.nextSpawnData.getEntityToSpawn().putString("DeathLootTable", "minecraft:empty");
                }
            }
            CompoundTag nbt = spawner.save();
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.SPAWNER.defaultBlockState(), nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.SMALL_NETHER_DUNGEON_MOB_SPAWNER_PROCESSOR;
    }
}
