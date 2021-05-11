package com.yungnickyoung.minecraft.betterdungeons.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Banner in Minecraft.
 * Includes fields for patterns, BlockState, and NBT tags.
 * Includes an internal Builder for easy Banner construction.
 */
public class Banner {
    private List<BannerPattern> patterns;
    private BlockState state;
    private CompoundNBT nbt;
    private boolean isWallBanner;

    public Banner(List<BannerPattern> _patterns, BlockState _state, CompoundNBT _nbt) {
        this.patterns = _patterns;
        this.state = _state;
        this.nbt = _nbt;
        this.isWallBanner = state.getBlock() instanceof WallBannerBlock;
    }

    public Banner(List<BannerPattern> _patterns, BlockState _state, CompoundNBT _nbt, boolean _isWallBanner) {
        this.patterns = _patterns;
        this.state = _state;
        this.nbt = _nbt;
        this.isWallBanner = _isWallBanner;
    }

    public List<BannerPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<BannerPattern> patterns) {
        this.patterns = patterns;
    }

    public BlockState getState() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public CompoundNBT getNbt() {
        return nbt;
    }

    public void setNbt(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public boolean isWallBanner() {
        return isWallBanner;
    }

    public void setWallBanner(boolean wallBanner) {
        isWallBanner = wallBanner;
    }

    public static class Builder {
        private final List<BannerPattern> patterns = new ArrayList<>();
        private BlockState state = Blocks.BLACK_WALL_BANNER.getDefaultState();

        public Builder() {
        }

        public Builder blockState(BlockState state) {
            this.state = state;
            return this;
        }

        public Builder pattern(BannerPattern pattern) {
            patterns.add(pattern);
            return this;
        }

        public Builder pattern(String pattern, int color) {
            patterns.add(new BannerPattern(pattern, color));
            return this;
        }

        public Banner build() {
            CompoundNBT nbt = createBannerNBT(patterns);
            return new Banner(patterns, state, nbt);
        }

        /**
         * Helper function that creates a complete CompoundNBT for a banner BlockState
         * with the provided patterns.
         */
        private static CompoundNBT createBannerNBT(List<BannerPattern> patterns) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT patternList = new ListNBT();

            // Construct list of patterns from args
            for (BannerPattern pattern : patterns) {
                CompoundNBT patternNBT = new CompoundNBT();
                patternNBT.putString("Pattern", pattern.getPattern());
                patternNBT.putInt("Color", pattern.getColor());
                patternList.add(patternNBT);
            }

            // Add tags to NBT
            nbt.put("Patterns", patternList);
            nbt.putString("id", "minecraft:banner");

            return nbt;
        }
    }
}
