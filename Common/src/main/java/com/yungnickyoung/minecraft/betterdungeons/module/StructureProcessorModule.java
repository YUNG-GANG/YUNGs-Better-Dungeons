package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.world.processor.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.RuinedStoneBrickProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonMobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorModule {
    public static StructureProcessorType<WaterloggedProcessor> WATERLOGGED_PROCESSOR = () -> WaterloggedProcessor.CODEC;
    public static StructureProcessorType<MobSpawnerProcessor> MOB_SPAWNER_PROCESSOR = () -> MobSpawnerProcessor.CODEC;
    public static StructureProcessorType<HeadProcessor> HEAD_PROCESSOR = () -> HeadProcessor.CODEC;
    public static StructureProcessorType<NetherBlockProcessor> NETHER_BLOCK_PROCESSOR = () -> NetherBlockProcessor.CODEC;
    public static StructureProcessorType<CandleProcessor> CANDLE_PROCESSOR = () -> CandleProcessor.CODEC;

    // Small dungeons
    public static StructureProcessorType<SmallDungeonCeilingPropProcessor> SMALL_DUNGEON_CEILING_PROP_PROCESSOR = () -> SmallDungeonCeilingPropProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonCeilingLampPropProcessor> SMALL_DUNGEON_CEILING_LAMP_PROCESSOR = () -> SmallDungeonCeilingLampPropProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonBannerProcessor> SMALL_DUNGEON_BANNER_PROCESSOR = () -> SmallDungeonBannerProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonChestProcessor> SMALL_DUNGEON_CHEST_PROCESSOR = () -> SmallDungeonChestProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonCobblestoneProcessor> SMALL_DUNGEON_COBBLE_PROCESSOR = () -> SmallDungeonCobblestoneProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonLegProcessor> SMALL_DUNGEON_LEG_PROCESSOR = () -> SmallDungeonLegProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonCeilingProcessor> SMALL_DUNGEON_CEILING_PROCESSOR = () -> SmallDungeonCeilingProcessor.CODEC;
    public static StructureProcessorType<SmallDungeonOreProcessor> SMALL_DUNGEON_ORE_PROCESSOR = () -> SmallDungeonOreProcessor.CODEC;

    // Skeleton dungeons
    public static StructureProcessorType<RuinedStoneBrickProcessor> SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR = () -> RuinedStoneBrickProcessor.CODEC;
    public static StructureProcessorType<SkeletonMobSpawnerProcessor> SKELETON_MOB_SPAWNER_PROCESSOR = () -> SkeletonMobSpawnerProcessor.CODEC;
    public static StructureProcessorType<SkeletonDungeonLegProcessor> SKELETON_DUNGEON_LEG_PROCESSOR = () -> SkeletonDungeonLegProcessor.CODEC;

    // Zombie dungeons
    public static StructureProcessorType<ZombieDungeonCubbyProcessor> ZOMBIE_DUNGEON_CUBBY_PROCESSOR = () -> ZombieDungeonCubbyProcessor.CODEC;
    public static StructureProcessorType<ZombieDungeonStairProcessor> ZOMBIE_DUNGEON_STAIR_PROCESSOR = () -> ZombieDungeonStairProcessor.CODEC;
    public static StructureProcessorType<ZombieMobSpawnerProcessor> ZOMBIE_MOB_SPAWNER_PROCESSOR = () -> ZombieMobSpawnerProcessor.CODEC;
    public static StructureProcessorType<ZombieTombstoneSpawnerProcessor> ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR = () -> ZombieTombstoneSpawnerProcessor.CODEC;
    public static StructureProcessorType<ZombieMainStairsProcessor> ZOMBIE_MAIN_STAIRS_PROCESSOR = () -> ZombieMainStairsProcessor.CODEC;
    public static StructureProcessorType<ZombieRotProcessor> ZOMBIE_ROT_PROCESSOR = () -> ZombieRotProcessor.CODEC;
    public static StructureProcessorType<ZombieDungeonLegProcessor> ZOMBIE_DUNGEON_LEG_PROCESSOR = () -> ZombieDungeonLegProcessor.CODEC;
    public static StructureProcessorType<ZombieDungeonFlowerPotProcessor> ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR = () -> ZombieDungeonFlowerPotProcessor.CODEC;
}
