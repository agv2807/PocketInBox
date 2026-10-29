# Tech stack

> Статус: внедряется. Отмечено, что уже подключено ✅.

| Область | Решение | ADR |
|---|---|---|
| Платформа | ✅ Kotlin 2.4.21, Compose Multiplatform 1.12.2, material3 1.9.0 | [001](../decisions/ADR-001-kmp-cmp.md) |
| Android | ✅ minSdk 24, targetSdk 36, compileSdk 37 (минимум для Compose 1.12) | — |
| iOS | ✅ Минимум iOS 16, таргеты `iosArm64` и `iosSimulatorArm64` (без `iosX64`) | — |
| Сборка | ✅ AGP 9.1.1 (built-in Kotlin), Gradle 9.3.1, version catalog, convention plugins в `build-logic`, configuration cache | [001](../decisions/ADR-001-kmp-cmp.md), [005](../decisions/ADR-005-multimodule.md) |
| DI | Koin (`koin-core`, `koin-compose`, `koin-compose-viewmodel`) | — |
| БД | Room KMP + KSP + `BundledSQLiteDriver` | — |
| Настройки | DataStore Preferences (KMP) | — |
| Навигация | Navigation Compose (multiplatform), type-safe маршруты через kotlinx.serialization | [002](../decisions/ADR-002-navigation.md) |
| Презентация | MVI своей реализации поверх KMP `androidx.lifecycle.ViewModel` | [003](../decisions/ADR-003-mvi.md) |
| Асинхронность | kotlinx.coroutines, Flow | — |
| Сериализация | kotlinx.serialization | — |
| Дата/время | kotlinx-datetime | — |
| Изображения | Coil 3 | — |
| Ресурсы | Compose Multiplatform Resources | — |
| Логирование | Kermit | — |
| Сеть | Нет (офлайн). В будущем — Ktor Client | [004](../decisions/ADR-004-offline-first.md) |
| Тесты | `kotlin.test`, Turbine | — |
| iOS-интеграция | Framework через build phase Xcode, без CocoaPods | — |
