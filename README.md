# Better Exploration Load Control

## Native source fixture safety

The dedicated startup smoke uses a fresh `build/gametest/<UUID>/` ForgeGradle RunConfig,
not the shared old `run-gametest/` tree or an early JavaExec default. It requires actual
native startup/shutdown, own structured startup status and zero ERROR/FATAL records.
This remains a startup-only smoke with zero authored GameTests, not gameplay acceptance.
Preparation creates only absent comment-only settings (native defaults), leaving old
worlds/logs and existing settings intact. `verifyFast` exercises settings preservation,
diagnostic rejection and native-count negative controls without Minecraft. Retain
complete old builds, libraries, fixtures and evidence before any authorized clean/reset.

## Playtest diagnostics

`bc.playtest.better_exploration_load_control.governor` reports actual policy/override
transition observations and native resume return versus failure during shutdown.
`metrics` snapshots the already measured window at most once per30 seconds, with the
DEBUG guard and due check before percentile snapshot work. Scope is explicitly this
mod's own DH override, never all distant generation. Shutdown failures retain the real
WARN/exception outside verbose suppression while remaining nonthrowing.

INFO startup supplies effective enabled state, version, DH presence and exact own-artifact
source metadata when available (UNKNOWN otherwise). The repository-local Java17 helper
shares120 DEBUG/TRACE details/minute (burst20),256 keys and2048-character records. Native
END ticks summarize30-second windows; stop clears aggregation without renewing TRACE.
Standalone stays native INFO; pack profile selects scoped DEBUG/`bc.playtest.level`.
Common six-case and scope/metric projection tests run with the source gate. Logging is
not gameplay-performance acceptance and does not alter policy thresholds or user config.

## Scope and authority

This repository owns its mod-specific behavior and authoring inputs. Read [local instructions](AGENTS.md)
and the [shared documentation/policy index](../../better-content-modpack/docs/README.md).


Measures active server tick p50/p95/p99/max over a configurable window. Under sustained load it pauses only its own Distant Horizons distant-generation API override, then clears that override after a recovery window. It does not change Distant Horizons config or enable a user-disabled feature. Use `/better_exploration_load_control performance_status` to inspect status.

Distant Horizons 2.4.5-b is optional; tick metrics remain available without it. Settings are in `config/better_exploration_load_control-common.toml`.

Build with `./gradlew verifyFast`, run full validation with `./gradlew verifyFull`, and stage the runtime jar with `./gradlew stageRuntimeJar`.
