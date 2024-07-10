package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.CandleProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.HeadProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.MobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.NetherBlockProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.RuinedStoneBrickProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonMobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonBannerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonCeilingLampPropProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonCeilingProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonCeilingPropProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonChestProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonCobblestoneProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.SmallDungeonOreProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon.SmallNetherDungeonBannerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon.SmallNetherDungeonEntranceStairsProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon.SmallNetherDungeonHeadProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon.SmallNetherDungeonLavaBlockProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon.SmallNetherDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon.SmallNetherDungeonMobSpawner;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieDungeonCubbyProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieDungeonFlowerPotProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieDungeonStairProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieMainStairsProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieMobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieRotProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.ZombieTombstoneSpawnerProcessor;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

@AutoRegister(BetterDungeonsCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("mob_spawner_processor")
    public static StructureProcessorType<MobSpawnerProcessor> MOB_SPAWNER_PROCESSOR = () -> MobSpawnerProcessor.CODEC;

    @AutoRegister("head_processor")
    public static StructureProcessorType<HeadProcessor> HEAD_PROCESSOR = () -> HeadProcessor.CODEC;

    @AutoRegister("nether_block_processor")
    public static StructureProcessorType<NetherBlockProcessor> NETHER_BLOCK_PROCESSOR = () -> NetherBlockProcessor.CODEC;

    @AutoRegister("candle_processor")
    public static StructureProcessorType<CandleProcessor> CANDLE_PROCESSOR = () -> CandleProcessor.CODEC;

    /* Small dungeons */
    @AutoRegister("small_dungeon_ceiling_prop_processor")
    public static StructureProcessorType<SmallDungeonCeilingPropProcessor> SMALL_DUNGEON_CEILING_PROP_PROCESSOR = () -> SmallDungeonCeilingPropProcessor.CODEC;

    @AutoRegister("small_dungeon_ceiling_lamp_processor")
    public static StructureProcessorType<SmallDungeonCeilingLampPropProcessor> SMALL_DUNGEON_CEILING_LAMP_PROCESSOR = () -> SmallDungeonCeilingLampPropProcessor.CODEC;

    @AutoRegister("small_dungeon_banner_processor")
    public static StructureProcessorType<SmallDungeonBannerProcessor> SMALL_DUNGEON_BANNER_PROCESSOR = () -> SmallDungeonBannerProcessor.CODEC;

    @AutoRegister("small_dungeon_chest_processor")
    public static StructureProcessorType<SmallDungeonChestProcessor> SMALL_DUNGEON_CHEST_PROCESSOR = () -> SmallDungeonChestProcessor.CODEC;

    @AutoRegister("small_dungeon_cobblestone_processor")
    public static StructureProcessorType<SmallDungeonCobblestoneProcessor> SMALL_DUNGEON_COBBLE_PROCESSOR = () -> SmallDungeonCobblestoneProcessor.CODEC;

    @AutoRegister("small_dungeon_leg_processor")
    public static StructureProcessorType<SmallDungeonLegProcessor> SMALL_DUNGEON_LEG_PROCESSOR = () -> SmallDungeonLegProcessor.CODEC;

    @AutoRegister("small_dungeon_ceiling_processor")
    public static StructureProcessorType<SmallDungeonCeilingProcessor> SMALL_DUNGEON_CEILING_PROCESSOR = () -> SmallDungeonCeilingProcessor.CODEC;

    @AutoRegister("small_dungeon_ore_processor")
    public static StructureProcessorType<SmallDungeonOreProcessor> SMALL_DUNGEON_ORE_PROCESSOR = () -> SmallDungeonOreProcessor.CODEC;

    /* Small Nether Dungeons */
    @AutoRegister("small_nether_dungeon_banner_processor")
    public static StructureProcessorType<SmallNetherDungeonBannerProcessor> SMALL_NETHER_DUNGEON_BANNER_PROCESSOR = () -> SmallNetherDungeonBannerProcessor.CODEC;

    @AutoRegister("small_nether_dungeon_head_processor")
    public static StructureProcessorType<SmallNetherDungeonHeadProcessor> SMALL_NETHER_DUNGEON_HEAD_PROCESSOR = () -> SmallNetherDungeonHeadProcessor.CODEC;

    @AutoRegister("small_nether_dungeon_lava_block_processor")
    public static StructureProcessorType<SmallNetherDungeonLavaBlockProcessor> SMALL_NETHER_DUNGEON_LAVA_BLOCK_PROCESSOR = () -> SmallNetherDungeonLavaBlockProcessor.CODEC;

    @AutoRegister("small_nether_dungeon_entrance_stairs_processor")
    public static StructureProcessorType<SmallNetherDungeonEntranceStairsProcessor> SMALL_NETHER_DUNGEON_ENTRANCE_STAIRS_PROCESSOR = () -> SmallNetherDungeonEntranceStairsProcessor.CODEC;

    @AutoRegister("small_nether_dungeon_leg_processor")
    public static StructureProcessorType<SmallNetherDungeonLegProcessor> SMALL_NETHER_DUNGEON_LEG_PROCESSOR = () -> SmallNetherDungeonLegProcessor.CODEC;

    @AutoRegister("small_nether_dungeon_mob_spawner_processor")
    public static StructureProcessorType<SmallNetherDungeonMobSpawner> SMALL_NETHER_DUNGEON_MOB_SPAWNER_PROCESSOR = () -> SmallNetherDungeonMobSpawner.CODEC;

    /* Skeleton dungeons */
    @AutoRegister("skeleton_dungeon_ruined_stone_bricks_processor")
    public static StructureProcessorType<RuinedStoneBrickProcessor> SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR = () -> RuinedStoneBrickProcessor.CODEC;

    @AutoRegister("skeleton_mob_spawner_processor")
    public static StructureProcessorType<SkeletonMobSpawnerProcessor> SKELETON_MOB_SPAWNER_PROCESSOR = () -> SkeletonMobSpawnerProcessor.CODEC;

    @AutoRegister("skeleton_dungeon_leg_processor")
    public static StructureProcessorType<SkeletonDungeonLegProcessor> SKELETON_DUNGEON_LEG_PROCESSOR = () -> SkeletonDungeonLegProcessor.CODEC;

    /* Zombie dungeons */

    @AutoRegister("zombie_dungeon_cubby_processor")
    public static StructureProcessorType<ZombieDungeonCubbyProcessor> ZOMBIE_DUNGEON_CUBBY_PROCESSOR = () -> ZombieDungeonCubbyProcessor.CODEC;

    @AutoRegister("zombie_dungeon_stair_processor")
    public static StructureProcessorType<ZombieDungeonStairProcessor> ZOMBIE_DUNGEON_STAIR_PROCESSOR = () -> ZombieDungeonStairProcessor.CODEC;

    @AutoRegister("zombie_mob_spawner_processor")
    public static StructureProcessorType<ZombieMobSpawnerProcessor> ZOMBIE_MOB_SPAWNER_PROCESSOR = () -> ZombieMobSpawnerProcessor.CODEC;

    @AutoRegister("zombie_tombstone_spawner_processor")
    public static StructureProcessorType<ZombieTombstoneSpawnerProcessor> ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR = () -> ZombieTombstoneSpawnerProcessor.CODEC;

    @AutoRegister("zombie_main_stairs_processor")
    public static StructureProcessorType<ZombieMainStairsProcessor> ZOMBIE_MAIN_STAIRS_PROCESSOR = () -> ZombieMainStairsProcessor.CODEC;

    @AutoRegister("zombie_rot_processor")
    public static StructureProcessorType<ZombieRotProcessor> ZOMBIE_ROT_PROCESSOR = () -> ZombieRotProcessor.CODEC;

    @AutoRegister("zombie_dungeon_leg_processor")
    public static StructureProcessorType<ZombieDungeonLegProcessor> ZOMBIE_DUNGEON_LEG_PROCESSOR = () -> ZombieDungeonLegProcessor.CODEC;

    @AutoRegister("zombie_dungeon_flower_pot_processor")
    public static StructureProcessorType<ZombieDungeonFlowerPotProcessor> ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR = () -> ZombieDungeonFlowerPotProcessor.CODEC;
}
