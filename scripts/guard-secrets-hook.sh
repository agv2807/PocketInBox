#!/usr/bin/env bash
# Claude Code PreToolUse hook for Bash: blocks commands that mention files with keys/secrets.
# Read/Edit permission deny rules in .claude/settings.json don't cover shell commands
# (cat, sed, cp ...), so this closes that gap. Keep the list in sync with those rules.
# See .claude/docs/conventions/secrets.md.
set -uo pipefail

command=$(jq -r '.tool_input.command // empty')

pattern='(^|[/[:space:]'"'"'"=])('
pattern+='\.env(\.[[:alnum:]_.-]+)?'
pattern+='|local\.properties|secrets\.properties|keystore\.properties'
pattern+='|google-services\.json|GoogleService-Info\.plist'
pattern+='|[^[:space:]/'"'"'"]*\.(jks|keystore|p12|p8|pem|key|mobileprovision)'
pattern+=')([[:space:]'"'"'";|&)>]|$)'

if match=$(grep -oE "$pattern" <<<"$command" | head -1) && [[ -n "$match" ]]; then
    echo "Blocked: the command touches a secrets file (${match//[[:space:]]/}). Access to key files is denied for Claude; ask the user to do it." >&2
    exit 2
fi
exit 0
