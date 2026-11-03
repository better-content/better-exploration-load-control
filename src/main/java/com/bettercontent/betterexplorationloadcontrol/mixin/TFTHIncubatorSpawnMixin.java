package com.bettercontent.betterexplorationloadcontrol.mixin;

import com.bettercontent.betterexplorationloadcontrol.performance.BoundedIncubatorSpawnSearch;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/** Keep all native incubator mob and servant creation in the Deep Void.
 * Their spawn-site searches read only loaded chunks and have fixed budgets. */
@Pseudo
@Mixin(targets = "net.mcreator.thefleshthathates.IncubatorMobSpawn", remap = false)
public abstract class TFTHIncubatorSpawnMixin {
    private static boolean betterContent$deepVoid(ServerLevel level) {
        return level.dimension().location().toString().equals("the_deep_void:deep_void");
    }

    @Inject(method = {"checkForIncubators", "spawnEntitiesNearActiveIncubators",
            "spawnFleshServantsNearIncubators"}, at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$incubatorTickOnlyInDeepVoid(ServerLevel level, CallbackInfo ci) {
        if (!betterContent$deepVoid(level)) ci.cancel();
    }

    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$mobOnlyInDeepVoid(ServerLevel level, LivingEntity incubator,
            net.minecraft.world.entity.EntityType<?> type, CallbackInfo ci) {
        if (!betterContent$deepVoid(level)) ci.cancel();
    }

    @Inject(method = "spawnEntityAtSafeDistance", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$servantOnlyInDeepVoid(ServerLevel level, LivingEntity incubator,
            net.minecraft.world.entity.EntityType<?> type, int radius, CallbackInfo ci) {
        if (!betterContent$deepVoid(level)) ci.cancel();
    }

    @Inject(method = "findHighestFleshBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$loadedBoundedServantSite(ServerLevel level, BlockPos base,
            CallbackInfoReturnable<BlockPos> result) {
        if (!betterContent$deepVoid(level) ||
                level.getChunkSource().getChunkNow(base.getX() >> 4, base.getZ() >> 4) == null ||
                level.isOutsideBuildHeight(base)) {
            result.setReturnValue(null);
            return;
        }
        Block flesh = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("the_flesh_that_hates", "flesh_block"));
        if (flesh == null || flesh == Blocks.AIR) {
            result.setReturnValue(null);
            return;
        }
        if (!level.getBlockState(base).is(flesh)) {
            BlockPos below = base.below();
            result.setReturnValue(!level.isOutsideBuildHeight(below) && level.getBlockState(below).is(flesh)
                ? below : null);
            return;
        }
        BlockPos top = base;
        // Native code walks upward without a height budget. Its caller tries at
        // most 100 columns; include the base read in this 32-read column budget.
        for (int i = 1; i < 32; i++) {
            BlockPos above = top.above();
            if (level.isOutsideBuildHeight(above)) {
                result.setReturnValue(top);
                return;
            }
            if (!level.getBlockState(above).is(flesh)) {
                result.setReturnValue(top);
                return;
            }
            top = above;
        }
        result.setReturnValue(null);
    }

    @Inject(method = "getRandomFleshBlockPosition", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$loadedBoundedSpawn(ServerLevel level, LivingEntity incubator,
            CallbackInfoReturnable<BlockPos> result) {
        if (!betterContent$deepVoid(level)) {
            result.setReturnValue(null);
            return;
        }
        Block flesh = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("the_flesh_that_hates", "flesh_block"));
        if (flesh == null || flesh == Blocks.AIR) {
            result.setReturnValue(null);
            return;
        }
        int radius = incubator.getType() == ForgeRegistries.ENTITY_TYPES.getValue(
            new ResourceLocation("the_flesh_that_hates", "plaqueincubatorstart")) ? 75 : 125;
        BlockPos spawn = BoundedIncubatorSpawnSearch.find(incubator.blockPosition(), radius,
            new Random(level.random.nextLong()), new BoundedIncubatorSpawnSearch.WorldProbe() {
                @Override public boolean loaded(BlockPos pos) {
                    return level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4) != null;
                }
                @Override public boolean flesh(BlockPos pos) { return level.getBlockState(pos).is(flesh); }
                @Override public boolean clear(BlockPos pos) { return level.isEmptyBlock(pos); }
            });
        result.setReturnValue(spawn); // null means no safe visible site this tick.
    }
}
