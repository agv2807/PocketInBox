#!/usr/bin/env bash
# Claude Code Stop hook: runs scripts/verify.sh only if non-Markdown files changed since the
# previous check. See .claude/docs/conventions/verification.md.
#
# The fingerprint is a hash of the working tree contents (tracked + untracked, honoring
# .gitignore, without *.md), so it ignores commits and only reacts to actual edits.
# On failure exits 2 so the output goes back to the agent; a failed state is remembered too,
# so the hook does not loop when the agent stops without changing anything.
set -uo pipefail

cd "$(git rev-parse --show-toplevel)" || exit 0
cat >/dev/null # hook input is not needed

state_file=$(git rev-parse --git-path claude-verify-fingerprint)
log_file=$(git rev-parse --git-path claude-verify.log)

tmp_index=$(mktemp)
trap 'rm -f "$tmp_index"' EXIT
cp "$(git rev-parse --git-path index)" "$tmp_index" 2>/dev/null || true
GIT_INDEX_FILE=$tmp_index git add -A >/dev/null 2>&1
fingerprint=$(GIT_INDEX_FILE=$tmp_index git ls-files -s | grep -vE $'\t.*\\.md$' | shasum | cut -d' ' -f1)

if [[ -f "$state_file" && "$(cat "$state_file")" == "$fingerprint" ]]; then
    exit 0
fi

scripts/verify.sh >"$log_file" 2>&1
status=$?
echo "$fingerprint" >"$state_file"

if [[ $status -ne 0 ]]; then
    {
        echo "scripts/verify.sh failed (exit $status). Fix the problems below; full log: $log_file"
        echo
        tail -n 80 "$log_file"
    } >&2
    exit 2
fi

echo '{"systemMessage": "verify.sh: all checks passed"}'
