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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/** Keep The Flesh That Hates' authored Lost Cities incubator ecology without
 * letting its 2.6-million-position synchronous scan load unvisited chunks.
 * Its other dimensions and the original JAR are untouched. */
@Pseudo
@Mixin(targets = "net.mcreator.thefleshthathates.IncubatorMobSpawn", remap = false)
public abstract class TFTHIncubatorSpawnMixin {
    @Inject(method = "getRandomFleshBlockPosition", at = @At("HEAD"), cancellable = true, remap = false)
    private static void betterContent$loadedBoundedSpawn(ServerLevel level, LivingEntity incubator,
            CallbackInfoReturnable<BlockPos> result) {
        if (!level.dimension().location().toString().equals("lostcities:lostcity")) return;
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
