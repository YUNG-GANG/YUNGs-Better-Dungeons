package com.yungnickyoung.minecraft.betterdungeons.world.processor.small_nether_dungeon;

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
public class SmallNetherDungeonBannerProcessor extends StructureProcessor {
    public static final Codec<SmallNetherDungeonBannerProcessor> CODEC = RecordCodecBuilder.create(codecBuilder -> codecBuilder
        .group(
            Codec.STRING
                .fieldOf("dungeon_type")
                .forGetter(processor -> processor.getDungeonType().getName()))
        .apply(codecBuilder, codecBuilder.stable(SmallNetherDungeonBannerProcessor::new)));

    private SmallNetherDungeonBannerProcessor(String dungeonType) {
        this.dungeonType = DungeonType.fromString(dungeonType);
    }

    private final DungeonType dungeonType;
    public DungeonType getDungeonType() {
        return this.dungeonType;
    }

    // All banners
    public static final Banner SKELETON_BANNER = new Banner.Builder()
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

    public static final Banner WITHER_SKELETON_BANNER = new Banner.Builder()
            .blockState(Blocks.RED_WALL_BANNER.defaultBlockState())
            .pattern("cbo", 15)
            .pattern("cs", 15)
            .pattern("bs", 14)
            .pattern("cre", 15)
            .pattern("ts", 15)
            .pattern("gru", 15)
            .customName("Banner of Decay")
            .customColor("dark_purple")
            .build();

    public static final Banner ZOMBIFIED_PIGLIN_BANNER = new Banner.Builder()
            .blockState(Blocks.PINK_WALL_BANNER.defaultBlockState())
            .pattern("ls", 13)
            .pattern("tts", 15)
            .pattern("tts", 6)
            .pattern("cs", 6)
            .pattern("vhr", 8)
            .pattern("rd", 13)
            .pattern("cre", 0)
            .pattern("hhb", 6)
            .pattern("mr", 6)
            .pattern("pig", 15)
            .pattern("gru", 6)
            .pattern("bs", 15)
            .customName("Banner of Pork")
            .customColor("light_purple")
            .build();

    public static final Banner BLAZE_BANNER = new Banner.Builder()
            .blockState(Blocks.RED_WALL_BANNER.defaultBlockState())
            .pattern("ss", 4)
            .pattern("tt", 14)
            .pattern("tt", 14)
            .pattern("flo", 1)
            .pattern("sku", 4)
            .pattern("cbo", 14)
            .pattern("gru", 15)
            .customName("Banner of Rage")
            .customColor("gold")
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
            if (blockInfoGlobal.state().getBlock() == Blocks.GRAY_WALL_BANNER && (blockInfoGlobal.nbt().get("Patterns") == null || blockInfoGlobal.nbt().getList("Patterns", 10).size() == 0)) {
                // Fetch thread-local dungeon context
                DungeonContext context = DungeonContext.peek();

                // Check dungeon context to see if we have reached the max banner count for this structure piece
                if (context.getBannerCount() >= BetterDungeonsCommon.CONFIG.smallNetherDungeons.bannerMaxCount)
                    return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.AIR.defaultBlockState(), blockInfoGlobal.nbt());

                // Chance of a banner spawning
                RandomSource random = structurePlacementData.getRandom(blockInfoGlobal.pos());
                if (random.nextFloat() > .1f) {
                    return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.AIR.defaultBlockState(), blockInfoGlobal.nbt());
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
        return StructureProcessorTypeModule.SMALL_NETHER_DUNGEON_BANNER_PROCESSOR;
    }

    private Banner getBannerForType() {
        switch (this.dungeonType) {
            case SKELETON:
                return SKELETON_BANNER;
            case ZOMBIFIED_PIGLIN:
                return ZOMBIFIED_PIGLIN_BANNER;
            case WITHER_SKELETON:
                return WITHER_SKELETON_BANNER;
            case BLAZE:
                return BLAZE_BANNER;
            default:
                BetterDungeonsCommon.LOGGER.warn("Invalid DungeonType {} for small_nether_dungeon_banner_processor! This shouldn't happen!", this.dungeonType);
                return SKELETON_BANNER;
        }
    }

    private CompoundTag copyNBT(CompoundTag other) {
        CompoundTag nbt = new CompoundTag();
        nbt.merge(other);
        return nbt;
    }
}
