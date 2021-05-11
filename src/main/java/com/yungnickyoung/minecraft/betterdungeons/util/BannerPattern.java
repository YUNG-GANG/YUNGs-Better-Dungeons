package com.yungnickyoung.minecraft.betterdungeons.util;

/**
 * Represents a single banner pattern.
 * A banner pattern consists of a String for the name of the pattern
 * and an integer color value.
 */
public class BannerPattern {
    private String pattern;
    private int color;

    public BannerPattern(String _pattern, int _color) {
        this.pattern = _pattern;
        this.color = _color;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}