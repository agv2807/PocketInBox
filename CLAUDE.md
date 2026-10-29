# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project knowledge base ("дока")

All project knowledge lives in `.claude/docs/` — the user calls it "дока". Start from `.claude/docs/README.md`: it describes the sections (product, architecture, decisions/ADR, conventions, roadmap) and the rules for maintaining them. Read the relevant sections before design work, and record new decisions and knowledge there (not in personal memory), following those rules. Current progress is in `.claude/docs/roadmap.md`.

## Commands

Run from the repo root with the Gradle wrapper:

```bash
./gradlew :androidApp:assembleDebug                         # build debug APK
./gradlew :androidApp:installDebug                          # install on a connected device/emulator
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64   # build the iOS framework for the simulator
# iOS app: open iosApp/iosApp.xcodeproj in Xcode, or build from CLI:
#   xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build
./gradlew allTests                                          # unit tests of KMP modules (Android host + iOS simulator), see .claude/docs/conventions/testing.md
./gradlew lint                                              # Android lint
```

## Build setup

- Modules: `iosApp` (Xcode project, SwiftUI host; its "Compile Kotlin Framework" build phase runs `:composeApp:embedAndSignAppleFrameworkForXcode`, set `TEAM_ID` in `iosApp/Configuration/Config.xcconfig` for device builds) and thin `androidApp` (Android application) → `composeApp` (shared root UI, iOS framework `ComposeApp`) → `core:*` / `feature:*` KMP libraries. See `.claude/docs/architecture/modules.md`.
- **Convention plugins** in the included build `build-logic/convention` hold all shared configuration. Module build files only apply one of them and declare dependencies:
  - `pocketinbox.kmp.library` — KMP + `com.android.kotlin.multiplatform.library`, targets android, `iosArm64`, `iosSimulatorArm64`. Android namespace is derived from the module path (`:core:designsystem` → `com.vazdautsan.pocketinbox.core.designsystem`).
  - `pocketinbox.cmp.library` — the above + Compose Multiplatform and base Compose libraries.
  - `pocketinbox.android.application` — `com.android.application` + Compose compiler.
  - `pocketinbox.room` — kmp.library + KSP (all targets) + Room plugin, `BundledSQLiteDriver`, schemas exported to `<module>/schemas`.
- **AGP 9 with built-in Kotlin**: never add `org.jetbrains.kotlin.android`, and never apply `com.android.application` together with KMP in one module.
- Versions live in `gradle/libs.versions.toml` (also used by `build-logic`). Compose Multiplatform artifacts are referenced by coordinates there (`compose-*`); `compose.*` accessors from the Compose plugin are not used. Plugins are declared in the root `build.gradle.kts` with `apply false`.
- Declare library versions directly in the catalog; don't use BOMs (Koin's BOM lost versions for `api` deps in the iOS framework build).
- `compileSdk` 37 (required by Compose 1.12), `targetSdk` 36, `minSdk` 24, JVM target 11.
- Sources live in `src/<sourceSet>/kotlin` (`commonMain`, `androidMain`, `iosMain`; `main` in `androidApp`). Platform specifics go through `expect`/`actual`.
- Swift sees top-level Kotlin functions as `<FileName>Kt`, so iOS-facing files must not use dotted names like `Foo.ios.kt` (they become `Foo_iosKt`).
- Inter-module dependencies use type-safe project accessors (`projects.core.designsystem`).
- `settings.gradle.kts` sets `RepositoriesMode.FAIL_ON_PROJECT_REPOS`, so repositories must be declared there and not in module build files.
