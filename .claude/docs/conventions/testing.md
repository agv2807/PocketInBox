# Тестирование

## Запуск

`./gradlew allTests` — тесты всех KMP-модулей на Android (host/JVM) и на iOS-симуляторе.

## Где писать тесты

| Что | Source set | Почему |
|---|---|---|
| ViewModel, репозитории, маппинг, чистая логика | `commonTest` | Запускаются на обеих платформах |
| DAO и Room-запросы | `iosTest` (`core:database`) | `BundledSQLiteDriver` не содержит нативных библиотек для Android host-тестов, а инструментальные тесты медленные |

## Инструменты

- `kotlin.test`, `kotlinx-coroutines-test` (`runTest`), Turbine для Flow. Подключены ко всем KMP-модулям конвенцией `pocketinbox.kmp.library`.
- ViewModel: в `@BeforeTest` — `Dispatchers.setMain(UnconfinedTestDispatcher())`, в `@AfterTest` — `Dispatchers.resetMain()`.
- Зависимости подменяются фейками-реализациями интерфейсов (`FakeNoteDao`), без mock-библиотек.
- Время — через инжектируемый `kotlin.time.Clock`, в тестах — фиксированный.
- Room в тестах: `Room.inMemoryDatabaseBuilder<…>().buildDatabase()` — та же конфигурация драйвера, что и в проде.
