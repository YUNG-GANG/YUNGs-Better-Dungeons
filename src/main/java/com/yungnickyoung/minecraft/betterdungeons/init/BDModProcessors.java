package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.RuinedStoneBrickProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonDungeonLegProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.skeleton_dungeon.SkeletonMobSpawnerProcessor;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BDModProcessors {
    // General
    public static IStructureProcessorType<WaterloggedProcessor> WATERLOGGED_PROCESSOR = () -> WaterloggedProcessor.CODEC;
    public static IStructureProcessorType<MobSpawnerProcessor> MOB_SPAWNER_PROCESSOR = () -> MobSpawnerProcessor.CODEC;
    public static IStructureProcessorType<HeadProcessor> HEAD_PROCESSOR = () -> HeadProcessor.CODEC;
    public static IStructureProcessorType<NetherBlockProcessor> NETHER_BLOCK_PROCESSOR = () -> NetherBlockProcessor.CODEC;

    // Small dungeons
    public static IStructureProcessorType<SmallDungeonCeilingPropProcessor> SMALL_DUNGEON_CEILING_PROP_PROCESSOR = () -> SmallDungeonCeilingPropProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonCeilingLampPropProcessor> SMALL_DUNGEON_CEILING_LAMP_PROCESSOR = () -> SmallDungeonCeilingLampPropProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonBannerProcessor> SMALL_DUNGEON_BANNER_PROCESSOR = () -> SmallDungeonBannerProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonChestProcessor> SMALL_DUNGEON_CHEST_PROCESSOR = () -> SmallDungeonChestProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonCobblestoneProcessor> SMALL_DUNGEON_COBBLE_PROCESSOR = () -> SmallDungeonCobblestoneProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonLegProcessor> SMALL_DUNGEON_LEG_PROCESSOR = () -> SmallDungeonLegProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonCeilingProcessor> SMALL_DUNGEON_CEILING_PROCESSOR = () -> SmallDungeonCeilingProcessor.CODEC;

    // Skeleton dungeons
    public static IStructureProcessorType<RuinedStoneBrickProcessor> SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR = () -> RuinedStoneBrickProcessor.CODEC;
    public static IStructureProcessorType<SkeletonMobSpawnerProcessor> SKELETON_MOB_SPAWNER_PROCESSOR = () -> SkeletonMobSpawnerProcessor.CODEC;
    public static IStructureProcessorType<SkeletonDungeonLegProcessor> SKELETON_DUNGEON_LEG_PROCESSOR = () -> SkeletonDungeonLegProcessor.CODEC;

    // Zombie dungeons
    public static IStructureProcessorType<ZombieDungeonCubbyProcessor> ZOMBIE_DUNGEON_CUBBY_PROCESSOR = () -> ZombieDungeonCubbyProcessor.CODEC;
    public static IStructureProcessorType<ZombieDungeonStairProcessor> ZOMBIE_DUNGEON_STAIR_PROCESSOR = () -> ZombieDungeonStairProcessor.CODEC;
    public static IStructureProcessorType<ZombieMobSpawnerProcessor> ZOMBIE_MOB_SPAWNER_PROCESSOR = () -> ZombieMobSpawnerProcessor.CODEC;
    public static IStructureProcessorType<ZombieTombstoneSpawnerProcessor> ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR = () -> ZombieTombstoneSpawnerProcessor.CODEC;
    public static IStructureProcessorType<ZombieMainStairsProcessor> ZOMBIE_MAIN_STAIRS_PROCESSOR = () -> ZombieMainStairsProcessor.CODEC;
    public static IStructureProcessorType<ZombieRotProcessor> ZOMBIE_ROT_PROCESSOR = () -> ZombieRotProcessor.CODEC;
    public static IStructureProcessorType<ZombieDungeonLegProcessor> ZOMBIE_DUNGEON_LEG_PROCESSOR = () -> ZombieDungeonLegProcessor.CODEC;
    public static IStructureProcessorType<ZombieDungeonFlowerPotProcessor> ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR = () -> ZombieDungeonFlowerPotProcessor.CODEC;

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

            // Small dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_prop_processor"), SMALL_DUNGEON_CEILING_PROP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_lamp_processor"), SMALL_DUNGEON_CEILING_LAMP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_banner_processor"), SMALL_DUNGEON_BANNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_chest_processor"), SMALL_DUNGEON_CHEST_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_cobblestone_processor"), SMALL_DUNGEON_COBBLE_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_leg_processor"), SMALL_DUNGEON_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_processor"), SMALL_DUNGEON_CEILING_PROCESSOR);

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
