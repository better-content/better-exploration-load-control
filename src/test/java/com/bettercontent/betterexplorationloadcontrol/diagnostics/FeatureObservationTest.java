package com.bettercontent.betterexplorationloadcontrol.diagnostics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public final class FeatureObservationTest {
    @Test public void nativeScalarsHaveExplicitStage() { var fields = FeatureObservation.fields(70.0, 100.0, true, true); assertTrue(fields.contains("p95_ms=70.0")); assertTrue(fields.contains("override_active=true")); assertTrue(fields.contains("scope=own_override_only")); }
    @Test public void uncertaintyAndPrivacyStayExplicit() { var fields = FeatureObservation.fields(10.0, 20.0, false, false); assertTrue(fields.contains("base_enabled=false")); assertTrue(fields.contains("override_active=false")); assertFalse(fields.contains("all_generation_stopped")); }
}
