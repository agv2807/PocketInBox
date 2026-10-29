# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project knowledge base ("дока")

All project knowledge lives in `.claude/docs/` — the user calls it "дока". Start from `.claude/docs/README.md`: it describes the sections (product, architecture, decisions/ADR, conventions, roadmap) and the rules for maintaining them. Read the relevant sections before design work, and record new decisions and knowledge there (not in personal memory), following those rules.

## Project state

The code is still the generated Android Studio "Empty Compose Activity" template (single `:app` module). The target KMP + CMP architecture is described in the дока (`.claude/docs/architecture/`) and has not been implemented yet.

## Commands

Run from the repo root with the Gradle wrapper:

```bash
./gradlew assembleDebug                 # build debug APK
./gradlew installDebug                  # install on a connected device/emulator
./gradlew test                          # JVM unit tests (app/src/test)
./gradlew connectedAndroidTest          # instrumented tests (app/src/androidTest), needs a device/emulator
./gradlew lint                          # Android lint

# single test class / method
./gradlew :app:testDebugUnitTest --tests "com.vazdautsan.pocketinbox.ExampleUnitTest"
./gradlew :app:testDebugUnitTest --tests "com.vazdautsan.pocketinbox.ExampleUnitTest.addition_isCorrect"
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.vazdautsan.pocketinbox.ExampleInstrumentedTest
```

## Build setup

- **AGP 9.x with built-in Kotlin**: there is no `org.jetbrains.kotlin.android` plugin. Only `com.android.application` and `org.jetbrains.kotlin.plugin.compose` are applied. Don't add the standalone Kotlin Android plugin.
- Dependencies and plugin versions live in the version catalog `gradle/libs.versions.toml`. Add new libraries there and reference them as `libs.…` in `app/build.gradle.kts`. Compose library versions come from the Compose BOM.
- `compileSdk` uses the new `release(36) { minorApiLevel = 1 }` DSL. `minSdk` is 24, `targetSdk` is 36, and Java/JVM target is 11.
- Namespace and applicationId: `com.vazdautsan.pocketinbox`. Sources are in `app/src/main/java/` (Kotlin files in the `java` source dir).
- UI is Jetpack Compose only (no XML layouts). `PocketInBoxTheme` (`ui/theme/Theme.kt`) uses dynamic color on Android 12+.
- `settings.gradle.kts` sets `RepositoriesMode.FAIL_ON_PROJECT_REPOS`, so repositories must be declared there and not in module build files.
