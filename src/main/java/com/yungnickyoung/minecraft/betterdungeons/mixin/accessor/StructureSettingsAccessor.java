package com.yungnickyoung.minecraft.betterdungeons.mixin.accessor;

import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(StructureSettings.class)
public interface StructureSettingsAccessor {
    @Mutable
    @Accessor("structureConfig")
    void setStructureConfig(Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig);
}

