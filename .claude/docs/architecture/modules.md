# Модули

> Статус: создано всё, кроме `core:datastore`. Обоснование — [ADR-005](../decisions/ADR-005-multimodule.md).

```
build-logic/convention   — convention plugins: pocketinbox.kmp.library, .cmp.library, .cmp.feature, .room, .android.application, .lint (корневой проект)
androidApp               — тонкий Android application: Activity, Application
iosApp                   — Xcode-проект: SwiftUI-обёртка над MainViewController, Config.xcconfig (TEAM_ID, BUNDLE_ID)
composeApp               — корневой UI, NavHost, сборка Koin-графа, iOS framework
core/
  common                 — MviViewModel, CollectEffects
  designsystem           — тема (PocketInBoxTheme), общие компоненты
  database               — Room: Database, DAO, entity
  datastore              — настройки (создаётся, когда понадобится)
  domain                 — модели и интерфейсы репозиториев
  data                   — реализации репозиториев, маппинг entity → domain
feature/
  notes                  — список заметок и добавление (образец фичи: conventions/feature-module.md)
```

## Правила

- Конфигурация сборки — только в convention plugins. В `build.gradle.kts` модуля: плагин и зависимости (исключение — iOS framework в `composeApp`).
- Android namespace выводится из пути модуля автоматически.
- Модуль создаётся, когда у него появляется реальное содержимое. Пустые заготовки не заводим.
- Нейминг: Gradle-путь `:core:database`, пакет `com.vazdautsan.pocketinbox.core.database`.
- Фича — один модуль `feature:<name>`: UI, ViewModel, Koin-модуль, маршруты. На `api`/`impl` делим только при реальной необходимости.
- Use cases не обязательны: ViewModel работает с репозиторием напрямую ([ADR-006](../decisions/ADR-006-no-mandatory-usecases.md)).

## Правила зависимостей

- `feature:*` не зависят друг от друга. Переходы между фичами собираются в `composeApp`.
- `feature:*` → `core:domain`, `core:designsystem`, `core:common`. Не напрямую в `core:database`.
- `core:data` → `core:database`, `core:domain` (и `core:datastore`, когда он появится).
- `composeApp` → все `feature:*` и `core:data` (для DI-графа).
- `androidApp` → только `composeApp`.
