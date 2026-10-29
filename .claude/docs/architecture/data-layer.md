# Слой данных

```
feature → NotesRepository (core:domain)
              ↑ реализует
          OfflineNotesRepository (core:data) → NoteDao → PocketInBoxDatabase (core:database)
```

## Room (`core:database`)

- `PocketInBoxDatabase` — единственная база, файл `pocketinbox.db`. Схемы экспортируются в `core/database/schemas/` и коммитятся — они нужны для будущих миграций.
- Builder создаётся платформенно (`platformDatabaseModule`: Android — `getDatabasePath`, iOS — `Documents/`), общая настройка — `buildDatabase()`: `BundledSQLiteDriver` и `Dispatchers.IO`.
- Entity хранят примитивы (время — epoch millis, `Long`), без `TypeConverter`. Преобразование в доменные типы — в `core:data`.
- Entity и DAO не выходят за пределы `core:data`: фичи видят только `core:domain`.

## Репозитории (`core:data`)

- Реализации `internal`, наружу — только Koin-модуль `dataModule`.
- Префикс `Offline…` — источник данных только локальный ([ADR-004](../decisions/ADR-004-offline-first.md)).
- Текущее время берётся из инжектируемого `kotlin.time.Clock`.

## DI-граф (Koin)

```
startKoinApp()                 composeApp — единая точка старта
  ├ dataModule                 core:data
  │   └ databaseModule         core:database
  │       └ platformDatabaseModule (expect/actual)
  └ notesModule                feature:notes — ViewModel
```

- Android: `PocketInBoxApplication.onCreate` → `startKoinApp { androidContext(...) }`.
- iOS: `iOSApp.init` → `KoinIosKt.startKoinIos()`.
- Каждый модуль отдаёт один публичный Koin-модуль и сам подключает (`includes`) то, от чего зависит.
