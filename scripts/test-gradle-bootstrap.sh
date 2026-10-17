#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT/android"
if [[ -x ./gradlew ]]; then
  echo "gradlew present"
else
  echo "gradlew missing" >&2
  exit 1
fi
echo "bootstrap check placeholder — run with Android SDK available"
