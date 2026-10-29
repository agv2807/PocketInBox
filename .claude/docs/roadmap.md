# Roadmap

## Текущий этап

**1. Каркас KMP + заметки** — завершён 2026-10-29: приложение со списком заметок работает на Android и iOS.

Модули: `build-logic`, `androidApp`, `iosApp`, `composeApp`, `core:common`, `core:designsystem`, `core:database`, `core:domain`, `core:data`, `feature:notes`. Экран описан в [product/overview.md](product/overview.md).

## Завершено

- **0. Стек** — 2026-10-29 ([architecture/tech-stack.md](architecture/tech-stack.md)).

## Следующие шаги

1. ✅ `build-logic` и convention plugins, перенос `:app` → `androidApp` + `composeApp`, тема → `core:designsystem`. Android запускается, iOS framework собирается.
2. ✅ `iosApp` (Xcode-проект, framework через build phase), экран запускается на Android и iOS.
3. ✅ `core:common` (MVI), `core:domain`, `core:database` (Room), `core:data` (репозиторий), Koin-граф, тесты.
4. ✅ `feature:notes`: список, пустое состояние, добавление через диалог; навигация и Koin ViewModel.

## Не проверено вручную

- Диалог добавления заметки на iOS: нажатия в симуляторе не автоматизированы. Чтение из Room на iOS проверено, логика добавления общая с Android и покрыта тестами.

## Открытые вопросы

- Развитие продукта после заметок.
