# Проверка после задачи

## Когда запускается

`scripts/verify.sh` запускается автоматически Stop-хуком Claude Code (`.claude/settings.json` → `scripts/verify-hook.sh`), когда агент заканчивает ответ, — **но только если с прошлой проверки изменился код**:

- Хук хранит отпечаток содержимого рабочей копии: tracked и untracked файлы с учётом `.gitignore`, без `*.md`. Отпечаток лежит в `.git/claude-verify-fingerprint`. Если отпечаток не изменился (ответ без правок, правки только в доке, коммит без новых изменений), хук завершается мгновенно.
- Правки засчитываются независимо от того, кто их сделал: Edit/Write, shell-команды агента или изменения пользователя в IDE.
- Если проверка упала, хук возвращает агенту хвост вывода (полный лог — `.git/claude-verify.log`), и агент продолжает работу, чтобы исправить ошибки. Задача не считается выполненной, пока проверка падает. Если ошибку нельзя исправить или она не связана с задачей, агент прямо говорит об этом пользователю.
- Отпечаток запоминается и после неудачной проверки. Поэтому, если агент остановился, ничего не исправив, хук не зацикливается: повторная проверка будет только после новых правок.

Агенту не нужно запускать `verify.sh` вручную в конце задачи. Пока идёт работа, можно запускать отдельные шаги (см. ниже). Хук отключается через `/hooks`.

## Что делает `scripts/verify.sh`

Изменения — это diff от merge base с `origin/main` (если его нет — `main`): коммиты ветки, staged, unstaged и untracked-файлы. Другую базу можно передать через `--base <ref>`.

| Шаг | Что запускается | Область |
|---|---|---|
| 1. Lint | `./gradlew ktlintFiles detektFiles -PlintFiles=…` | Только изменённые `.kt`/`.kts` |
| 2. Тесты | `./gradlew :<module>:allTests …` | Только изменённые модули (см. ниже) |
| 3. Android | `./gradlew :androidApp:assembleDebug` | Всегда |
| 4. iOS | `xcodebuild … -destination 'generic/platform=iOS Simulator' ARCHS=arm64 build` | Всегда |

- Модуль определяется по пути файла и списку `include` в `settings.gradle.kts`. Изменения в `iosApp/` и в корне не относятся ни к одному модулю.
- Если изменились `build-logic/`, `gradle/` (каталог версий, wrapper), `settings.gradle.kts`, корневой `build.gradle.kts` или `gradle.properties`, тесты прогоняются во всех модулях.
- `androidApp` пропускается: KMP-тестов в нём нет.
- Если изменились только `.md`-файлы, скрипт ничего не запускает.

## Отдельные шаги

```bash
./gradlew ktlintFiles -PlintFiles=a.kt,b.kt        # ktlint по файлам (без -PlintFiles — весь проект)
./gradlew ktlintFormatFiles -PlintFiles=a.kt,b.kt  # автоисправление ktlint
./gradlew detektFiles -PlintFiles=a.kt,b.kt        # detekt по файлам (без -PlintFiles — весь проект)
./gradlew :feature:notes:allTests                  # тесты одного модуля
```

Сначала замечания ktlint исправляют через `ktlintFormatFiles`, а оставшиеся — вручную. Замечания detekt исправляют в коде. `@Suppress` допустим только с комментарием, почему правило здесь не подходит.

## Настройка линтеров

- Задачи регистрирует convention plugin `pocketinbox.lint` (применён к корневому проекту). Он запускает CLI ktlint и detekt, версии которых указаны в `gradle/libs.versions.toml`. Локальная установка не нужна.
- **ktlint** 1.8.0, правила — в `.editorconfig`: стиль `intellij_idea` (совпадает с форматтером IDE при `kotlin.code.style=official`), длина строки 120, `@Composable` разрешены в PascalCase. Правила `class-signature` и `function-signature` отключены, потому что они сворачивают многострочные списки параметров.
- **detekt** 1.23.8 (последний стабильный, 2.0 пока в alpha). Работает без type resolution. Конфиг `config/detekt/detekt.yml` дополняет конфиг по умолчанию: исключения для `@Composable`/`@Preview`, отключён `MatchingDeclarationName` (файл `navigation/<Feature>Navigation.kt`).
