# CI

GitHub Actions, workflow `.github/workflows/ci.yml`. Запускается на push в `main`, на pull request и вручную (`workflow_dispatch`). Новый push в ту же ветку отменяет предыдущий прогон.

| Job | Runner | Что делает |
|---|---|---|
| `ktlint` | `ubuntu-latest` | `./gradlew ktlintFiles` по всему проекту, правила в `.editorconfig` ([verification.md](verification.md)) |
| `detekt` | `ubuntu-latest` | `./gradlew detektFiles` по всему проекту, конфиг `config/detekt/detekt.yml` |
| `unit-tests` | `ubuntu-latest` | `./gradlew testAndroidHostTest --continue`: `commonTest` всех KMP-модулей на Android host (JVM). При падении отчёты доступны как artifact `unit-test-reports` |
| `ios-build` | `macos-26` | `xcodebuild` схемы `iosApp` под симулятор (`generic/platform=iOS Simulator`, arm64, без подписи). Kotlin-framework собирает build phase «Compile Kotlin Framework» |

- JDK — Temurin 17 через `actions/setup-java`. Кэш Gradle — `gradle/actions/setup-gradle`. Кэш Kotlin/Native (`~/.konan`) обновляется при изменении `gradle/libs.versions.toml`.
- На Linux iOS-таргеты не собираются, поэтому `iosTest` (DAO-тесты `core:database`, см. [testing.md](testing.md)) в CI пока не запускаются. Чтобы включить их, добавьте в `ios-build` шаг `./gradlew iosSimulatorArm64Test`.
- Job'ы независимы и идут параллельно, каждый виден в PR отдельной проверкой.
