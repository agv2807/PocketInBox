# Сборка и запуск

Все Gradle-команды — из корня репозитория через wrapper.

## Android

```bash
./gradlew :androidApp:assembleDebug   # debug APK
./gradlew :androidApp:installDebug    # установить на подключённое устройство/эмулятор
```

## iOS

Xcode-проект `iosApp/iosApp.xcodeproj`, схема `iosApp`. Kotlin-framework `ComposeApp` собирается build phase «Compile Kotlin Framework» (`:composeApp:embedAndSignAppleFrameworkForXcode`), отдельно запускать Gradle не нужно. Если в окружении Xcode нет JDK, build phase берёт JBR из Android Studio.

- **Симулятор:** открыть проект в Xcode и запустить, либо из CLI:
  ```bash
  xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build
  ```
- **Устройство:** указать Apple Developer Team ID в `TEAM_ID` в `iosApp/Configuration/Config.xcconfig` (пустой — сборка только для симулятора). Там же `BUNDLE_ID` и `APP_NAME`.
- **Только framework** (быстрая проверка, что Kotlin-код компилируется под iOS):
  ```bash
  ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
  ```

## Проверки

```bash
scripts/verify.sh    # lint, тесты изменённых модулей, обе сборки; после правок агента запускается хуком — см. verification.md
./gradlew allTests   # unit-тесты всех KMP-модулей: Android host + iOS-симулятор, см. testing.md
./gradlew lint       # Android lint
./gradlew testAndroidHostTest  # только JVM-тесты commonTest, как в CI — см. ci.md
```
