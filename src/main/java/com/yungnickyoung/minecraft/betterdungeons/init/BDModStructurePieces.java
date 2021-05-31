package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonPiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

import java.util.Locale;

public class BDModStructurePieces {
    public static IStructurePieceType SPIDER_DUNGEON_PIECE;

    public static void init() {
        SPIDER_DUNGEON_PIECE = registerPiece("spider_dungeon_piece", SpiderDungeonPiece::new);
    }

    private static IStructurePieceType registerPiece(String name, IStructurePieceType piece) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(BetterDungeons.MOD_ID, name.toLowerCase(Locale.ROOT)), piece);
    }
}
