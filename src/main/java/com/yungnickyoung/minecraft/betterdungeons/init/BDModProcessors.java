package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon.*;
import com.yungnickyoung.minecraft.betterdungeons.world.processor.CobwebProcessor;
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
    public static IStructureProcessorType<CobwebProcessor> COBWEB_PROCESSOR = () -> CobwebProcessor.CODEC;

    // Small dungeons
    public static IStructureProcessorType<SmallDungeonCeilingPropProcessor> SMALL_DUNGEON_CEILING_PROP_PROCESSOR = () -> SmallDungeonCeilingPropProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonCeilingLampPropProcessor> SMALL_DUNGEON_CEILING_LAMP_PROCESSOR = () -> SmallDungeonCeilingLampPropProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonBannerProcessor> SMALL_DUNGEON_BANNER_PROCESSOR = () -> SmallDungeonBannerProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonChestProcessor> SMALL_DUNGEON_CHEST_PROCESSOR = () -> SmallDungeonChestProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonCobblestoneProcessor> SMALL_DUNGEON_COBBLE_PROCESSOR = () -> SmallDungeonCobblestoneProcessor.CODEC;
    public static IStructureProcessorType<SmallDungeonLegProcessor> SMALL_DUNGEON_LEG_PROCESSOR = () -> SmallDungeonLegProcessor.CODEC;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModProcessors::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // General use
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "waterlogged_processor"), WATERLOGGED_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "mob_spawner_processor"), MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "head_processor"), HEAD_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "cobweb_processor"), COBWEB_PROCESSOR);

            // Small dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_prop_processor"), SMALL_DUNGEON_CEILING_PROP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_ceiling_lamp_processor"), SMALL_DUNGEON_CEILING_LAMP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_banner_processor"), SMALL_DUNGEON_BANNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_chest_processor"), SMALL_DUNGEON_CHEST_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_cobblestone_processor"), SMALL_DUNGEON_COBBLE_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_leg_processor"), SMALL_DUNGEON_LEG_PROCESSOR);
        });
    }
}
