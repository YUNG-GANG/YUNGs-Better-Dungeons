package com.yungnickyoung.minecraft.betterdungeons.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LocateCommand.class)
public class LocateSmallNetherDungeonCommandMixin {
    @Unique
    private static final SimpleCommandExceptionType DUNGEON_DISABLED_EXCEPTION =
            new SimpleCommandExceptionType(Component.translatable("Small Nether Dungeons are currently disabled by default. You can enable them in the mod's config."));

    @Inject(method = "locateStructure", at = @At(value = "HEAD"))
    private static void betterdungeons_overrideLocateSmallNetherDungeon (CommandSourceStack cmdSource,
                                                       ResourceOrTagKeyArgument.Result<Structure> result,
                                                       CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        Optional<ResourceKey<Structure>> optional = result.unwrap().left();
        if (!BetterDungeonsCommon.CONFIG.smallNetherDungeons.enabled && optional.isPresent() && optional.get().location().equals(ResourceLocation.fromNamespaceAndPath(BetterDungeonsCommon.MOD_ID, "small_nether_dungeon"))) {
            throw DUNGEON_DISABLED_EXCEPTION.create();
        }
    }
}
