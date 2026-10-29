#!/usr/bin/env bash
# Post-task verification, see .claude/docs/conventions/verification.md:
#   1. ktlint + detekt on changed Kotlin files
#   2. allTests of changed modules (all modules if the build setup changed)
#   3. Android debug build
#   4. iOS simulator build
#
# Changes = diff against the merge base with the base branch (committed + staged + unstaged)
# plus untracked files.
#
# Usage: scripts/verify.sh [--base <ref>]   (default base: origin/main, falls back to main)
set -euo pipefail

cd "$(git rev-parse --show-toplevel)"

base_ref=""
while [[ $# -gt 0 ]]; do
    case "$1" in
        --base) base_ref="$2"; shift 2 ;;
        *) echo "Unknown argument: $1" >&2; exit 2 ;;
    esac
done
if [[ -z "$base_ref" ]]; then
    if git rev-parse --verify -q origin/main >/dev/null; then base_ref=origin/main; else base_ref=main; fi
fi
base=$(git merge-base HEAD "$base_ref")

step() { printf '\n==> %s\n' "$*"; }

changed=$( { git diff --name-only "$base"; git ls-files --others --exclude-standard; } | sort -u)
if [[ -z "$changed" ]]; then
    echo "No changes against $base_ref, nothing to verify."
    exit 0
fi

# Docs-only changes don't need builds
code_changed=$(grep -vE '\.md$' <<<"$changed" || true)
if [[ -z "$code_changed" ]]; then
    echo "Only Markdown changed, nothing to verify."
    exit 0
fi

# --- 1. Lint -------------------------------------------------------------------
kotlin_files=$(grep -E '\.kts?$' <<<"$changed" | while read -r f; do [[ -f "$f" ]] && echo "$f"; done || true)
if [[ -n "$kotlin_files" ]]; then
    step "ktlint + detekt: $(wc -l <<<"$kotlin_files" | tr -d ' ') file(s)"
    ./gradlew ktlintFiles detektFiles --continue -PlintFiles="$(paste -sd, - <<<"$kotlin_files")"
else
    step "ktlint + detekt: no Kotlin files changed, skipped"
fi

# --- 2. Tests of changed modules -------------------------------------------------
# Module paths from settings.gradle.kts, longest first so nested modules win the prefix match
modules=$(grep -oE 'include\("[^"]+"\)' settings.gradle.kts | sed -E 's/include\("(.*)"\)/\1/' \
    | awk '{ print length($0) "\t" $0 }' | sort -rn | cut -f2)
# Modules without KMP tests
no_tests=":androidApp"

module_of() {
    local file=$1 module dir
    for module in $modules; do
        dir=${module#:}
        dir=${dir//://}
        if [[ "$file" == "$dir/"* ]]; then
            echo "$module"
            return
        fi
    done
}

if grep -qE '^(build-logic/|gradle/|settings\.gradle\.kts$|build\.gradle\.kts$|gradle\.properties$)' <<<"$code_changed"; then
    test_modules=$modules
    reason="build setup changed, all modules"
else
    test_modules=$(while read -r f; do module_of "$f"; done <<<"$code_changed" | sort -u)
    reason="changed modules"
fi
test_tasks=$(for m in $test_modules; do [[ " $no_tests " == *" $m "* ]] || echo "$m:allTests"; done)

if [[ -n "$test_tasks" ]]; then
    step "Tests ($reason): $(echo $test_tasks)"
    # shellcheck disable=SC2086
    ./gradlew $test_tasks --continue
else
    step "Tests: no changed modules with tests, skipped"
fi

# --- 3. Android build ----------------------------------------------------------
step "Android: :androidApp:assembleDebug"
./gradlew :androidApp:assembleDebug

# --- 4. iOS build --------------------------------------------------------------
# Generic simulator destination; only arm64 since there is no iosX64 target
step "iOS: xcodebuild iosApp (simulator)"
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug \
    -destination 'generic/platform=iOS Simulator' ARCHS=arm64 -quiet build

step "All checks passed"
