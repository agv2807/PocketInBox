# Tech stack

> Статус: утверждён, ещё не внедрён (в репозитории пока Android-шаблон).

| Область | Решение | ADR |
|---|---|---|
| Платформа | Kotlin Multiplatform + Compose Multiplatform | [001](../decisions/ADR-001-kmp-cmp.md) |
| Android | minSdk 24, targetSdk 36 | — |
| iOS | Минимум iOS 16, таргеты `iosArm64` и `iosSimulatorArm64` (без `iosX64`) | — |
| Сборка | AGP 9 (built-in Kotlin), version catalog, convention plugins в `build-logic` | [001](../decisions/ADR-001-kmp-cmp.md), [005](../decisions/ADR-005-multimodule.md) |
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
