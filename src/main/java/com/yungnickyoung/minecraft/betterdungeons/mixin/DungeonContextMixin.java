package com.yungnickyoung.minecraft.betterdungeons.mixin;

import com.yungnickyoung.minecraft.betterdungeons.world.DungeonContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * Dynamically saves the Dungeon Context for structures,
 * allowing processors to have behavior dependent on the type of dungeon.
 */
@Mixin(Template.class)
public class DungeonContextMixin {
    @Inject(method = "func_237146_a_", at = @At(value = "HEAD"))
    private void saveDungeonContext(IServerWorld serverWorld, BlockPos structurePiecePos, BlockPos structurePieceBottomCenterPos, PlacementSettings placementSettings, Random random, int p_237146_6_, CallbackInfoReturnable<Boolean> cir) {
        DungeonContext.initialize();
    }
}
