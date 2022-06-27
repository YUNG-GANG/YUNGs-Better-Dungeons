package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.services.Services;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonEggRoomPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonNestPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonSmallTunnelPiece;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public class StructurePieceTypeModule {
    public static StructurePieceType BIG_TUNNEL = (StructurePieceType.ContextlessType) SpiderDungeonBigTunnelPiece::new;
    public static StructurePieceType NEST = (StructurePieceType.ContextlessType) SpiderDungeonNestPiece::new;
    public static StructurePieceType SMALL_TUNNEL = (StructurePieceType.ContextlessType) SpiderDungeonSmallTunnelPiece::new;
    public static StructurePieceType EGG_ROOM = (StructurePieceType.ContextlessType) SpiderDungeonEggRoomPiece::new;

    public static void init() {
        register("spider_dungeon_big_tunnel_piece", BIG_TUNNEL);
        register("spider_dungeon_nest_piece", NEST);
        register("spider_dungeon_small_tunnel_piece", SMALL_TUNNEL);
        register("spider_dungeon_egg_room_piece", EGG_ROOM);
    }

    private static void register(String name, StructurePieceType structurePieceType) {
        Services.REGISTRY.registerStructurePieceType(new ResourceLocation(BetterDungeonsCommon.MOD_ID, name.toLowerCase(Locale.ROOT)), structurePieceType);
    }
}
