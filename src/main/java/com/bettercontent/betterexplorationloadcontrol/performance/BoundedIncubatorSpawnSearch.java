package com.bettercontent.betterexplorationloadcontrol.performance;

import net.minecraft.core.BlockPos;

import java.util.Objects;
import java.util.Random;

/** A bounded, loaded-chunk-only search for a flesh-block-supported spawn site.
 * The third-party server-tick search scans 251*41*251 positions PER incubator and
 * synchronously loads chunks; one remote landing can hang the whole server tick.
 * This samples the same horizontal/vertical envelope without generating terrain. */
public final class BoundedIncubatorSpawnSearch {
    public static final int MAX_PROBES = 4096;

    public interface WorldProbe {
        boolean loaded(BlockPos pos);
        boolean flesh(BlockPos pos);
        boolean clear(BlockPos pos);
    }

    private BoundedIncubatorSpawnSearch() {}

    public static BlockPos find(BlockPos origin, int radius, Random random, WorldProbe world) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(random);
        Objects.requireNonNull(world);
        if (radius < 1 || radius > 125) throw new IllegalArgumentException("invalid incubator search radius");
        BlockPos selected = null;
        int candidates = 0;
        for (int i = 0; i < MAX_PROBES; i++) {
            BlockPos base = origin.offset(random.nextInt(2 * radius + 1) - radius,
                random.nextInt(41) - 20, random.nextInt(2 * radius + 1) - radius);
            BlockPos above = base.above();
            BlockPos north = above.north();
            BlockPos south = above.south();
            BlockPos east = above.east();
            BlockPos west = above.west();
            // Check all chunk boundaries before any block-state read: getBlockState
            // otherwise synchronously generates untouched terrain on the tick thread.
            if (!world.loaded(base) || !world.loaded(above) || !world.loaded(north) ||
                !world.loaded(south) || !world.loaded(east) || !world.loaded(west)) continue;
            if (!world.flesh(base) || !world.clear(above) || !world.clear(north) ||
                !world.clear(south) || !world.clear(east) || !world.clear(west)) continue;
            if (random.nextInt(++candidates) == 0) selected = base;
        }
        return selected;
    }
}
