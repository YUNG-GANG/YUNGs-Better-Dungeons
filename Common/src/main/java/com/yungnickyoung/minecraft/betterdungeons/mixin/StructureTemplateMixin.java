package com.yungnickyoung.minecraft.betterdungeons.mixin;

import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureProcessorAccessor;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
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
 * Makes it so a block's waterlogged state is not based solely on the presence of water at the block's position.
 *
 * @author TelepathicGrunt
 */
@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    @Inject(method = "placeInWorld",
            at = @At(value = "HEAD"))
    private void betterdungeons_preventAutoWaterlogging(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos1, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings, RandomSource random, int flag, CallbackInfoReturnable<Boolean> cir) {
        if (structurePlaceSettings.getProcessors()
                .stream()
                .anyMatch(processor -> ((StructureProcessorAccessor)processor).callGetType() == StructureProcessorTypeModule.WATERLOGGED_PROCESSOR)) {
            structurePlaceSettings.setKeepLiquids(false);
        }
    }
}