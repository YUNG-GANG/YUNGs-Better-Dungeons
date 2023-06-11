package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterdungeons.world.DungeonContext;
import com.yungnickyoung.minecraft.betterdungeons.world.DungeonType;
import com.yungnickyoung.minecraft.yungsapi.world.banner.Banner;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Replaces wall banners with a banner corresponding to the dungeon type.
 * Also removes existing banners to ensure the number of banners per structure
 * falls within the desired range.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallDungeonBannerProcessor extends StructureProcessor {
    public static final Codec<SmallDungeonBannerProcessor> CODEC = RecordCodecBuilder.create(codecBuilder -> codecBuilder
        .group(
            Codec.STRING
                .fieldOf("dungeon_type")
                .forGetter(smallDungeonBannerProcessor -> smallDungeonBannerProcessor.getDungeonType().getName()))
        .apply(codecBuilder, codecBuilder.stable(SmallDungeonBannerProcessor::new)));

    private SmallDungeonBannerProcessor(String dungeonType) {
        this.dungeonType = DungeonType.fromString(dungeonType);
    }

    private final DungeonType dungeonType;
    public DungeonType getDungeonType() {
        return this.dungeonType;
    }

    // All banners
    public static final Banner SMALL_DUNGEON_SKELETON_BANNER = new Banner.Builder()
        .blockState(Blocks.BLACK_WALL_BANNER.defaultBlockState())
        .pattern("cbo", 0)
        .pattern("cs", 0)
        .pattern("bs", 15)
        .pattern("ts", 0)
        .pattern("cre", 0)
        .pattern("gra", 15)
        .customName("Vengeful Banner")
        .customColor("dark_gray")
        .build();

    public static final Banner SMALL_DUNGEON_ZOMBIE_BANNER = new Banner.Builder()
        .blockState(Blocks.RED_WALL_BANNER.defaultBlockState())
        .pattern("bt", 6)
        .pattern("mc", 7)
        .pattern("gra", 15)
        .pattern("tts", 0)
        .pattern("bts", 0)
        .pattern("bo", 13)
        .customName("Foul Banner")
        .customColor("dark_green")
        .build();

    public static final Banner SMALL_DUNGEON_SPIDER_BANNER = new Banner.Builder()
        .blockState(Blocks.RED_WALL_BANNER.defaultBlockState())
        .pattern("flo", 7)
        .pattern("bo", 7)
        .pattern("sc", 7)
        .pattern("hh", 7)
        .pattern("bs", 7)
        .pattern("gra", 15)
        .customName("Haunted Banner")
        .customColor("dark_red")
        .build();

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() instanceof AbstractBannerBlock) {
            // Make sure we only operate on the placeholder banners
            if (blockInfoGlobal.state().getBlock() == Blocks.RED_WALL_BANNER && (blockInfoGlobal.nbt().get("Patterns") == null || blockInfoGlobal.nbt().getList("Patterns", 10).size() == 0)) {
                // Fetch thread-local dungeon context
                DungeonContext context = DungeonContext.peek();

                // Check dungeon context to see if we have reached the max banner count for this structure piece
                if (context.getBannerCount() >= BetterDungeonsCommon.CONFIG.smallDungeons.bannerMaxCount)
                    return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.CAVE_AIR.defaultBlockState(), blockInfoGlobal.nbt());

                // Chance of a banner spawning
                RandomSource random = structurePlacementData.getRandom(blockInfoGlobal.pos());
                if (random.nextFloat() > .1f) {
                    return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.CAVE_AIR.defaultBlockState(), blockInfoGlobal.nbt());
                }

                Banner banner = getBannerForType();
                Direction facing = blockInfoGlobal.state().getValue(BlockStateProperties.HORIZONTAL_FACING);
                BlockState newState = banner.getState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
                CompoundTag newNBT = copyNBT(banner.getNbt());

                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), newState, newNBT);
                context.incrementBannerCount();
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.SMALL_DUNGEON_BANNER_PROCESSOR;
    }

    private Banner getBannerForType() {
        switch (this.dungeonType) {
            case SKELETON:
                return SMALL_DUNGEON_SKELETON_BANNER;
            case ZOMBIE:
                return SMALL_DUNGEON_ZOMBIE_BANNER;
            case SPIDER:
                return SMALL_DUNGEON_SPIDER_BANNER;
            default:
                BetterDungeonsCommon.LOGGER.warn("Invalid DungeonType {} for BannerProcessor! This shouldn't happen!", this.dungeonType);
                return SMALL_DUNGEON_SKELETON_BANNER;
        }
    }

    private CompoundTag copyNBT(CompoundTag other) {
        CompoundTag nbt = new CompoundTag();
        nbt.merge(other);
        return nbt;
    }
}
