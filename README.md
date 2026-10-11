# Better Exploration Load Control

## Scope and authority

This repository owns its mod-specific behavior and authoring inputs. Read [local instructions](AGENTS.md)
and the [shared documentation/policy index](../../better-content-modpack/docs/README.md).


Measures active server tick p50/p95/p99/max over a configurable window. Under sustained load it pauses only its own Distant Horizons distant-generation API override, then clears that override after a recovery window. It does not change Distant Horizons config or enable a user-disabled feature. Use `/better_exploration_load_control performance_status` to inspect status.

Distant Horizons 2.4.5-b is optional; tick metrics remain available without it. Settings are in `config/better_exploration_load_control-common.toml`.

Build with `./gradlew verifyFast`, run full validation with `./gradlew verifyFull`, and stage the runtime jar with `./gradlew stageRuntimeJar`.
