# Better Exploration Load Control

Forge 1.20.1 / Java 17; mod ID `better_exploration_load_control`.

## Local verification

- Deterministic: `./gradlew verifyFast`.
- Runtime changes: `./gradlew verifyFull` (dedicated-server startup smoke, no authored GameTests).
- Stage: `./gradlew stageRuntimeJar`, `build/libs/better-exploration-load-control-<version>.jar`.

## Shared authority

Read [workspace policy](../../better-content-modpack/docs/policies/workspace.md),
[testing](../../better-content-modpack/docs/testing.md) and
[disposal](../../better-content-modpack/docs/policies/generated-data.md).
Docs-only changes use the shared documentation check and `git diff --check`.
