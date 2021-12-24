package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.RuinedStoneBrickProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonMobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BDModProcessors {
    // General
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModProcessors::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // General use
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "waterlogged_processor"), WATERLOGGED_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "mob_spawner_processor"), MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "head_processor"), HEAD_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "nether_block_processor"), NETHER_BLOCK_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "candle_processor"), CANDLE_PROCESSOR);

            // Small dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_prop_processor"), SMALL_DUNGEON_CEILING_PROP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_lamp_processor"), SMALL_DUNGEON_CEILING_LAMP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_banner_processor"), SMALL_DUNGEON_BANNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_chest_processor"), SMALL_DUNGEON_CHEST_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_cobblestone_processor"), SMALL_DUNGEON_COBBLE_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_leg_processor"), SMALL_DUNGEON_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_processor"), SMALL_DUNGEON_CEILING_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ore_processor"), SMALL_DUNGEON_ORE_PROCESSOR);

            // Skeleton dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon_ruined_stone_bricks_processor"), SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_mob_spawner_processor"), SKELETON_MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon_leg_processor"), SKELETON_DUNGEON_LEG_PROCESSOR);

            // Zombie dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon_cubby_processor"), ZOMBIE_DUNGEON_CUBBY_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon_stair_processor"), ZOMBIE_DUNGEON_STAIR_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_mob_spawner_processor"), ZOMBIE_MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_tombstone_spawner_processor"), ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_main_stairs_processor"), ZOMBIE_MAIN_STAIRS_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_rot_processor"), ZOMBIE_ROT_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon_leg_processor"), ZOMBIE_DUNGEON_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon_flower_pot_processor"), ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR);
        });
    }
}
