# Secrets: файлы с ключами

Claude не читает и не редактирует файлы с ключами. Если для задачи нужно что-то в них поменять, Claude просит сделать это пользователя.

## Какие файлы

- `.env`, `.env.*`
- `local.properties`, `secrets.properties`, `keystore.properties`
- `google-services.json`, `GoogleService-Info.plist`
- `*.jks`, `*.keystore`, `*.p12`, `*.p8`, `*.pem`, `*.key`, `*.mobileprovision`

Ключи и пароли кладём только в такие файлы (и они в `.gitignore`), а не в код или `*.gradle.kts`.

## Как это устроено

Две защиты в `.claude/settings.json`, список файлов в них должен совпадать:

1. `permissions.deny`: правила `Read(...)` и `Edit(...)` закрывают Read, Edit, Write, Grep и Glob.
2. PreToolUse-хук на Bash (`scripts/guard-secrets-hook.sh`) блокирует shell-команды, в которых упоминается такой файл (`cat`, `sed`, `cp` и т.д.). Правила `permissions` shell-команды не покрывают.

Gradle и Xcode сами читают эти файлы при сборке, это не блокируется. Хук смотрит только на текст команды: `./gradlew assembleDebug` проходит, а `cat local.properties` нет.

Новый тип файла с секретами добавляем в оба места и в этот список.
