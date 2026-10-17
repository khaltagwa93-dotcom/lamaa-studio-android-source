#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

passed=0
failed=0
check() {
  local label="$1"; shift
  if "$@"; then printf 'PASS — %s\n' "$label"; passed=$((passed+1)); else printf 'FAIL — %s\n' "$label" >&2; failed=$((failed+1)); fi
}

check 'JavaScript syntax: app.js' node --check app.js
check 'JavaScript syntax: service worker' node --check sw.js
check 'PWA manifest is valid JSON' python3 -c 'import json; json.load(open("manifest.webmanifest", encoding="utf-8"))'
check 'HTML static checks' python3 scripts/html-static-check.py

if [[ "$failed" -gt 0 ]]; then
  printf '\n%d checks failed, %d passed\n' "$failed" "$passed" >&2
  exit 1
fi
printf '\nAll %d checks passed\n' "$passed"
