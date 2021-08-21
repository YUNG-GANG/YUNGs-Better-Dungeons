package com.yungnickyoung.minecraft.betterdungeons.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.TranslatableText;

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
    private NbtCompound nbt;
    private boolean isWallBanner;

    public Banner(List<BannerPattern> _patterns, BlockState _state, NbtCompound _nbt) {
        this.patterns = _patterns;
        this.state = _state;
        this.nbt = _nbt;
        this.isWallBanner = state.getBlock() instanceof WallBannerBlock;
    }

    public Banner(List<BannerPattern> _patterns, BlockState _state, NbtCompound _nbt, boolean _isWallBanner) {
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

    public NbtCompound getNbt() {
        return nbt;
    }

    public void setNbt(NbtCompound nbt) {
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
        private TranslatableText customNameTranslate;
        private String customColor;
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

        public Builder customName(String translatableNamePath) {
            this.customNameTranslate = new TranslatableText(translatableNamePath);
            return this;
        }

        public Builder customColor(String colorString) {
            this.customColor = colorString;
            return this;
        }

        public Banner build() {
            NbtCompound nbt = createBannerNBT();
            return new Banner(patterns, state, nbt);
        }

        /**
         * Helper function that creates a complete NbtCompound for a banner BlockState
         * with the provided patterns.
         */
        private NbtCompound createBannerNBT() {
            NbtCompound nbt = new NbtCompound();
            NbtList patternList = new NbtList();

            // Construct list of patterns from args
            for (BannerPattern pattern : this.patterns) {
                NbtCompound patternNBT = new NbtCompound();
                patternNBT.putString("Pattern", pattern.getPattern());
                patternNBT.putInt("Color", pattern.getColor());
                patternList.add(patternNBT);
            }

            // Custom name and color
            if (this.customColor != null || this.customNameTranslate != null) {
                String color = this.customColor == null ? "" : String.format("\"color\":\"%s\"", this.customColor);
                String name = this.customNameTranslate == null ? "" : String.format("\"translate\":\"%s\"", this.customNameTranslate.getKey());
                if (this.customColor != null && this.customNameTranslate != null) name = "," + name;
                String customNameString = "{" + color + name + "}";

                nbt.putString("CustomName", customNameString);
            }

            // Add tags to NBT
            nbt.put("Patterns", patternList);
            nbt.putString("id", "minecraft:banner");

            return nbt;
        }
    }
}
