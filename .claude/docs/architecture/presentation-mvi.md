# MVI-контракт

Обоснование — [ADR-003](../decisions/ADR-003-mvi.md). Базовый класс — `MviViewModel<S, I, E>` в `core:common`.

| Элемент | Тип | Как работает |
|---|---|---|
| State `S` | `StateFlow<S>` (`state`) | Неизменяемый data class. Меняется только через `updateState { copy(...) }` |
| Intent `I` | `onIntent(intent)` | Единственная публичная точка входа из UI. Обычно `sealed interface` |
| Effect `E` | `Flow<E>` (`effects`) | Одноразовые события: навигация, сообщения. `Channel(BUFFERED)`: эффект, отправленный до подписки, не теряется и доставляется ровно одному подписчику |

## Правила

- UI не вызывает методы ViewModel, кроме `onIntent`.
- Подписка на `effects` в Compose — `CollectEffects(viewModel.effects) { ... }` (`core:common`): собирает эффекты, пока экран в состоянии STARTED, на `Dispatchers.Main.immediate`.
- State в Compose — `collectAsStateWithLifecycle()`.
- Ошибки операций — эффектом (например, snackbar), а не полем state. `CancellationException` всегда пробрасывается дальше.
- Как устроен экран фичи — [conventions/feature-module.md](../conventions/feature-module.md).
- Асинхронная работа — в `viewModelScope`.
