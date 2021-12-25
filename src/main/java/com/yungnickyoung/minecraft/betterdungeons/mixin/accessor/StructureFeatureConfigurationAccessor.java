package com.yungnickyoung.minecraft.betterdungeons.mixin.accessor;

import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureFeatureConfiguration.class)
public interface StructureFeatureConfigurationAccessor {
    @Mutable
    @Accessor
    void setSpacing(int spacing);

    @Mutable
    @Accessor
    void setSeparation(int separation);
}
