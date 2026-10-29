# Tech stack

> Статус: внедряется. Отмечено, что уже подключено ✅.

| Область | Решение | ADR |
|---|---|---|
| Платформа | ✅ Kotlin 2.4.21, Compose Multiplatform 1.12.2, material3 1.9.0 | [001](../decisions/ADR-001-kmp-cmp.md) |
| Android | ✅ minSdk 24, targetSdk 36, compileSdk 37 (минимум для Compose 1.12) | — |
| iOS | ✅ Минимум iOS 16, таргеты `iosArm64` и `iosSimulatorArm64` (без `iosX64`) | — |
| Сборка | ✅ AGP 9.1.1 (built-in Kotlin), Gradle 9.3.1, version catalog, convention plugins в `build-logic`, configuration cache | [001](../decisions/ADR-001-kmp-cmp.md), [005](../decisions/ADR-005-multimodule.md) |
| DI | ✅ Koin 4.2.2 (`koin-core`, `koin-android`, `koin-compose-viewmodel`). Версии в каталоге напрямую, без BOM | — |
| БД | ✅ Room 2.8.5 + KSP 2.3.12 + `BundledSQLiteDriver` (sqlite 2.6.2) | — |
| Настройки | DataStore Preferences (KMP) | — |
| Навигация | ✅ Navigation Compose 2.10.0 (multiplatform), type-safe маршруты через kotlinx.serialization 1.12.0 | [002](../decisions/ADR-002-navigation.md) |
| Презентация | ✅ MVI своей реализации поверх KMP `androidx.lifecycle.ViewModel` 2.11.0 | [003](../decisions/ADR-003-mvi.md) |
| Асинхронность | ✅ kotlinx.coroutines 1.11.0, Flow | — |
| Сериализация | ✅ kotlinx.serialization (пока только маршруты навигации) | — |
| Дата/время | ✅ `kotlin.time.Instant`/`Clock` из stdlib; kotlinx-datetime 0.8.0 для часовых поясов и форматирования | — |
| Изображения | Coil 3 | — |
| Ресурсы | ✅ Compose Multiplatform Resources: строки (en по умолчанию + ru), векторные иконки | — |
| Логирование | Kermit | — |
| Сеть | Нет (офлайн). В будущем — Ktor Client | [004](../decisions/ADR-004-offline-first.md) |
| Тесты | ✅ `kotlin.test`, kotlinx-coroutines-test, Turbine 1.2.1 ([conventions/testing.md](../conventions/testing.md)) | — |
| iOS-интеграция | ✅ Статический framework `ComposeApp` через build phase Xcode (`embedAndSignAppleFrameworkForXcode`), без CocoaPods | — |
