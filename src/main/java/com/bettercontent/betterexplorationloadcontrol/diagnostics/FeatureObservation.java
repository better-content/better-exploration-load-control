package com.bettercontent.betterexplorationloadcontrol.diagnostics;

/** Pure projection of values already observed by the owning native decision. */
public final class FeatureObservation {
    private FeatureObservation() {}
    public static String fields(double p95, double max, boolean base, boolean override) { return PlaytestLog.fields("p95_ms", p95, "max_ms", max, "base_enabled", base, "override_active", override, "scope", "own_override_only"); }
    
}
