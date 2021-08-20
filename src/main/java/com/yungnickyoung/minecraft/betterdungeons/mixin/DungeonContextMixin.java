package com.yungnickyoung.minecraft.betterdungeons.mixin;

import com.yungnickyoung.minecraft.betterdungeons.world.DungeonContext;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * Dynamically saves the Dungeon Context for structures,
 * allowing processors to have behavior dependent on the type of dungeon.
 */
@Mixin(Structure.class)
public class DungeonContextMixin {
    @Inject(method = "Lnet/minecraft/structure/Structure;place(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/Random;I)Z", at = @At(value = "HEAD"))
    private void saveDungeonContext(ServerWorldAccess serverWorld, BlockPos structurePiecePos, BlockPos structurePieceBottomCenterPos, StructurePlacementData placementSettings, Random random, int i, CallbackInfoReturnable<Boolean> cir) {
        DungeonContext.initialize();
    }
}
