package com.yungnickyoung.minecraft.betterdungeons.world.processor;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.ModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.util.Banner;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

/**
 * Replaces red wall banners with a random banner from a pool of banners.
 */
@MethodsReturnNonnullByDefault
public class BannerProcessor extends StructureProcessor {
    public static final BannerProcessor INSTANCE = new BannerProcessor();
    public static final Codec<BannerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    // All banners
    public static final Banner ENDERMAN_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.MAGENTA_WALL_BANNER.getDefaultState())
        .pattern("ss", 0)
        .pattern("ts", 15)
        .pattern("hhb", 15)
        .pattern("bo", 15)
        .pattern("ms", 15)
        .pattern("cs", 15)
        .build();

    public static final Banner WITHER_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.BLACK_WALL_BANNER.getDefaultState())
        .pattern("bs", 7)
        .pattern("cs", 15)
        .pattern("hh", 7)
        .pattern("cre", 15)
        .pattern("sku", 15)
        .build();

    public static final Banner PORTAL_WALL_BANNER = new Banner.Builder()
        .blockState(Blocks.PURPLE_WALL_BANNER.getDefaultState())
        .pattern("ss", 2)
        .pattern("bri", 10)
        .pattern("cbo", 2)
        .pattern("bo", 15)
        .build();

    public static final List<Banner> WALL_BANNERS = Lists.newArrayList(
        ENDERMAN_WALL_BANNER,
        WITHER_WALL_BANNER,
        PORTAL_WALL_BANNER
    );

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader worldReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() instanceof AbstractBannerBlock) {
            // Make sure we only operate on the placeholder banners
            if (blockInfoGlobal.state.getBlock() == Blocks.GRAY_WALL_BANNER && (blockInfoGlobal.nbt.get("Patterns") == null || blockInfoGlobal.nbt.getList("Patterns", 10).size() == 0)) {
                Banner banner = getRandomBanner(structurePlacementData.getRandom(blockInfoGlobal.pos));
                Direction facing = blockInfoGlobal.state.get(BlockStateProperties.HORIZONTAL_FACING);
                BlockState newState = banner.getState().with(BlockStateProperties.HORIZONTAL_FACING, facing);
                CompoundNBT newNBT = copyNBT(banner.getNbt());

                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, newState, newNBT);
            }
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.SMALL_DUNGEON_BANNER_PROCESSOR;
    }

    private Banner getRandomBanner(Random random) {
        return WALL_BANNERS.get(random.nextInt(WALL_BANNERS.size()));
    }

    private CompoundNBT copyNBT(CompoundNBT other) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.merge(other);
        return nbt;
    }
}
