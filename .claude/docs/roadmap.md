# Roadmap

## Текущий этап

**1. Каркас KMP + заметки** — scope утверждён 2026-10-29, реализация не начата.

Модули: `build-logic`, `androidApp`, `iosApp`, `composeApp`, `core:common`, `core:designsystem`, `core:database`, `core:domain`, `core:data`, `feature:notes`. Экран описан в [product/overview.md](product/overview.md).

## Завершено

- **0. Стек** — 2026-10-29 ([architecture/tech-stack.md](architecture/tech-stack.md)).

## Следующие шаги

1. ✅ `build-logic` и convention plugins, перенос `:app` → `androidApp` + `composeApp`, тема → `core:designsystem`. Android запускается, iOS framework собирается.
2. `iosApp`, запуск пустого экрана на обеих платформах.
3. `core:*`: MVI-контракт, Room, репозиторий.
4. `feature:notes`: список + добавление.

## Открытые вопросы

- Развитие продукта после заметок.
