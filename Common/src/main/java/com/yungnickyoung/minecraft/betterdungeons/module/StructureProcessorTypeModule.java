package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.services.Services;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.RuinedStoneBrickProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonMobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorTypeModule {
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

    public static void init() {
        register("waterlogged_processor", WATERLOGGED_PROCESSOR);
        register("mob_spawner_processor", MOB_SPAWNER_PROCESSOR);
        register("head_processor", HEAD_PROCESSOR);
        register("nether_block_processor", NETHER_BLOCK_PROCESSOR);
        register("candle_processor", CANDLE_PROCESSOR);

        // Small dungeons
        register("small_dungeon_ceiling_prop_processor", SMALL_DUNGEON_CEILING_PROP_PROCESSOR);
        register("small_dungeon_ceiling_lamp_processor", SMALL_DUNGEON_CEILING_LAMP_PROCESSOR);
        register("small_dungeon_banner_processor", SMALL_DUNGEON_BANNER_PROCESSOR);
        register("small_dungeon_chest_processor", SMALL_DUNGEON_CHEST_PROCESSOR);
        register("small_dungeon_cobblestone_processor", SMALL_DUNGEON_COBBLE_PROCESSOR);
        register("small_dungeon_leg_processor", SMALL_DUNGEON_LEG_PROCESSOR);
        register("small_dungeon_ceiling_processor", SMALL_DUNGEON_CEILING_PROCESSOR);
        register("small_dungeon_ore_processor", SMALL_DUNGEON_ORE_PROCESSOR);

        // Skeleton dungeons
        register("skeleton_dungeon_ruined_stone_bricks_processor", SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR);
        register("skeleton_mob_spawner_processor", SKELETON_MOB_SPAWNER_PROCESSOR);
        register("skeleton_dungeon_leg_processor", SKELETON_DUNGEON_LEG_PROCESSOR);

        // Zombie dungeons
        register("zombie_dungeon_cubby_processor", ZOMBIE_DUNGEON_CUBBY_PROCESSOR);
        register("zombie_dungeon_stair_processor", ZOMBIE_DUNGEON_STAIR_PROCESSOR);
        register("zombie_mob_spawner_processor", ZOMBIE_MOB_SPAWNER_PROCESSOR);
        register("zombie_tombstone_spawner_processor", ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR);
        register("zombie_main_stairs_processor", ZOMBIE_MAIN_STAIRS_PROCESSOR);
        register("zombie_rot_processor", ZOMBIE_ROT_PROCESSOR);
        register("zombie_dungeon_leg_processor", ZOMBIE_DUNGEON_LEG_PROCESSOR);
        register("zombie_dungeon_flower_pot_processor", ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR);
    }

    private static void register(String name, StructureProcessorType<?> processorType) {
        Services.REGISTRY.registerStructureProcessorType(new ResourceLocation(BetterDungeonsCommon.MOD_ID, name), processorType);
    }
}
