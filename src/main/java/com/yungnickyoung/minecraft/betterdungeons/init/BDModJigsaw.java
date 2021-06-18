package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.jigsaw.piece.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BDModJigsaw {
    public static IJigsawDeserializer<YungMaxCountJigsawPiece> YUNG_MAX_COUNT_ELEMENT;
    public static IJigsawDeserializer<YungSingleJigsawPiece> YUNG_SINGLE_ELEMENT;
    public static IJigsawDeserializer<YungLegacySingleJigsawPiece> YUNG_LEGACY_SINGLE_ELEMENT;
    public static IJigsawDeserializer<YungFeatureJigsawPiece> YUNG_FEATURE_ELEMENT;
    public static IJigsawDeserializer<YungListJigsawPiece> YUNG_LIST_ELEMENT;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModJigsaw::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            YUNG_MAX_COUNT_ELEMENT = Registry.register(
                Registry.STRUCTURE_POOL_ELEMENT,
                new ResourceLocation(BetterDungeons.MOD_ID, "yung_max_count_single_element"),
                () -> YungMaxCountJigsawPiece.CODEC);

            YUNG_SINGLE_ELEMENT = Registry.register(
                Registry.STRUCTURE_POOL_ELEMENT,
                new ResourceLocation(BetterDungeons.MOD_ID, "yung_single_element"),
                () -> YungSingleJigsawPiece.CODEC);

            YUNG_LEGACY_SINGLE_ELEMENT = Registry.register(
                Registry.STRUCTURE_POOL_ELEMENT,
                new ResourceLocation(BetterDungeons.MOD_ID, "yung_legacy_single_element"),
                () -> YungLegacySingleJigsawPiece.CODEC);

            YUNG_FEATURE_ELEMENT = Registry.register(
                Registry.STRUCTURE_POOL_ELEMENT,
                new ResourceLocation(BetterDungeons.MOD_ID, "yung_feature_element"),
                () -> YungFeatureJigsawPiece.CODEC);

            YUNG_LIST_ELEMENT = Registry.register(
                Registry.STRUCTURE_POOL_ELEMENT,
                new ResourceLocation(BetterDungeons.MOD_ID, "yung_list_element"),
                () -> YungListJigsawPiece.CODEC);
        });
    }
}
