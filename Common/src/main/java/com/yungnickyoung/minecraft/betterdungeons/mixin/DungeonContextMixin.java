package com.yungnickyoung.minecraft.betterdungeons.mixin;

import com.yungnickyoung.minecraft.betterdungeons.world.DungeonContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Dynamically saves the Dungeon Context for structures,
 * allowing processors to have behavior dependent on the type of dungeon.
 */
@Mixin(StructureTemplate.class)
public class DungeonContextMixin {
    @Inject(method = "placeInWorld", at = @At(value = "HEAD"))
    private void saveDungeonContext(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings, RandomSource randomSource, int i, CallbackInfoReturnable<Boolean> cir) {
        DungeonContext.initialize();
    }
}
