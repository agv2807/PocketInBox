# Структура feature-модуля

Образец — `feature:notes`. В `build.gradle.kts` только плагин `pocketinbox.cmp.feature` и специфичные зависимости. Плагин уже подключает `core:common`, `core:domain`, `core:designsystem`, Koin ViewModel, навигацию и сериализацию.

```
feature/<name>/src/commonMain/
  kotlin/com/vazdautsan/pocketinbox/feature/<name>/
    <Name>Contract.kt        State (data class), Intent и Effect (sealed interface) — internal
    <Name>ViewModel.kt       наследник MviViewModel — internal
    <Name>Screen.kt          <Name>Screen (stateful) + <Name>Content (stateless) + превью
    navigation/<Name>Navigation.kt   @Serializable <Name>Route + NavGraphBuilder.<name>Screen()
    di/<Name>Module.kt       val <name>Module = module { viewModelOf(...) }
  composeResources/
    values/strings.xml       английский — по умолчанию
    values-ru/strings.xml    русский
    drawable/                векторные иконки (XML)
```

## Правила

- **Публичны только** маршрут, функция `NavGraphBuilder.<name>Screen()` и Koin-модуль. Всё остальное — `internal`.
- **`<Name>Screen`** получает ViewModel через `koinViewModel()`, собирает `state` через `collectAsStateWithLifecycle()`, эффекты — через `CollectEffects`.
- **`<Name>Content`** не знает о ViewModel: принимает `state` и `onIntent`. Превью и UI-тесты работают с ним.
- **Строки и иконки** — только через Compose Resources (`Res.string.*`, `Res.drawable.*`). Класс `Res` генерируется в пакете `<namespace>.resources`. Захардкоженный текст в UI запрещён.
- **Подключение фичи** — в `composeApp`: зависимость, `<name>Screen()` в `NavHost`, Koin-модуль в `startKoinApp`.
- **Тесты** ViewModel — в `commonTest` с фейковым репозиторием ([testing.md](testing.md)).
