package com.bettercontent.betterexplorationloadcontrol.mixin;

import net.mcreator.thefleshthathates.FleshWorldData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** The pack's TFTH incubator habitat is exclusively the Deep Void. */
@Pseudo
@Mixin(targets = "net.mcreator.thefleshthathates.IncubatorSpawn", remap = false)
public abstract class TFTHIncubatorHabitatMixin {
    private static boolean betterContent$deepVoid(ServerLevel level) {
        return level.dimension().location().toString().equals("the_deep_void:deep_void");
    }

    @Inject(method = "processFirstTickEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$firstIncubatorOnlyInDeepVoid(ServerLevel level, Player player,
            FleshWorldData data, CallbackInfo ci) {
        if (!betterContent$deepVoid(level)) ci.cancel();
    }

    @Inject(method = "processSecondTickEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$secondIncubatorOnlyInDeepVoid(ServerLevel level, Player player,
            FleshWorldData data, CallbackInfo ci) {
        if (!betterContent$deepVoid(level)) ci.cancel();
    }

    @Inject(method = "spawnIncubatorStart", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$incubatorOnlyInDeepVoid(ServerLevel level,
            double x, double y, double z, FleshWorldData data, CallbackInfo ci) {
        if (!betterContent$deepVoid(level)) ci.cancel();
    }
}
