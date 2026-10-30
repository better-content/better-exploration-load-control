package com.bettercontent.betterexplorationloadcontrol.performance;

import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BoundedIncubatorSpawnSearchTest {
    @Test void unloadedChunksAreNeverInspectedOrGeneratedAndBudgetIsFinite() {
        AtomicInteger checks = new AtomicInteger();
        BoundedIncubatorSpawnSearch.WorldProbe unloaded = new BoundedIncubatorSpawnSearch.WorldProbe() {
            @Override public boolean loaded(BlockPos pos) { checks.incrementAndGet(); return false; }
            @Override public boolean flesh(BlockPos pos) { fail("unloaded block state was read"); return false; }
            @Override public boolean clear(BlockPos pos) { fail("unloaded block state was read"); return false; }
        };
        assertNull(BoundedIncubatorSpawnSearch.find(BlockPos.ZERO, 125, new Random(1), unloaded));
        assertEquals(BoundedIncubatorSpawnSearch.MAX_PROBES, checks.get());
    }

    @Test void candidateRemainsOnFleshWithinOriginalEnvelopeWithClearSpawnSpace() {
        AtomicInteger fleshChecks = new AtomicInteger();
        BoundedIncubatorSpawnSearch.WorldProbe loaded = new BoundedIncubatorSpawnSearch.WorldProbe() {
            @Override public boolean loaded(BlockPos pos) { return Math.abs(pos.getX()) <= 126 && Math.abs(pos.getZ()) <= 126; }
            @Override public boolean flesh(BlockPos pos) {
                fleshChecks.incrementAndGet();
                return pos.getY() == 0 && pos.getX() % 2 == 0;
            }
            @Override public boolean clear(BlockPos pos) { return pos.getY() == 1; }
        };
        BlockPos candidate = BoundedIncubatorSpawnSearch.find(BlockPos.ZERO, 75, new Random(19), loaded);
        assertNotNull(candidate);
        assertEquals(0, candidate.getY());
        assertEquals(0, candidate.getX() % 2);
        assertTrue(Math.abs(candidate.getX()) <= 75 && Math.abs(candidate.getZ()) <= 75);
        assertTrue(fleshChecks.get() <= BoundedIncubatorSpawnSearch.MAX_PROBES);
        assertThrows(IllegalArgumentException.class, () ->
            BoundedIncubatorSpawnSearch.find(BlockPos.ZERO, 126, new Random(1), loaded));
    }
}
