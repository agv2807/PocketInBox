# ADR-001: KMP + Compose Multiplatform

- **Статус:** Accepted
- **Дата:** 2026-10-29

## Контекст

Проект создан из Android-шаблона (AGP 9, built-in Kotlin, один модуль `:app`). Нужны Android и iOS с общей логикой и UI.

## Решение

Kotlin Multiplatform для общей логики, Compose Multiplatform для общего UI. Таргеты: Android, iOS.

AGP 9 не позволяет совмещать `com.android.application` и KMP в одном модуле, поэтому:
- общие модули — KMP-библиотеки с плагином `com.android.kotlin.multiplatform.library`;
- `androidApp` — отдельный тонкий application-модуль;
- `iosApp` — Xcode-проект, подключающий framework из `composeApp`.

Текущий `:app` будет заменён этой структурой.

## Последствия

- Один UI-код на обе платформы.
- Платформенный код — через `expect/actual` или интерфейсы с реализацией в DI.
