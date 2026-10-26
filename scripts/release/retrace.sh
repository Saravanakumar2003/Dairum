#!/usr/bin/env bash
# De-obfuscate an Android crash stack trace with the R8 mapping of the build that crashed.
#
#   scripts/release/retrace.sh <stacktrace.txt> [archive-dir-or-mapping.txt]
#   adb logcat -d -b crash | scripts/release/retrace.sh - ~/dairum-releases/1.0.0-vc1
#
# The mapping must be the one archived for the exact APK that crashed (match the versionCode).
# With no second argument the mapping of the most recent local build is used.
set -euo pipefail
trace="${1:?usage: $0 <stacktrace.txt|-> [archive-dir|mapping.txt]}"
repo="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd -P)"
m="${2:-$repo/app/build/outputs/mapping/release}"
[ -d "$m" ] && m="$m/mapping.txt"
[ -f "$m" ] || { echo "mapping not found: $m" >&2; exit 1; }
sdk="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home}"
if [ "$trace" = "-" ]; then "$sdk/cmdline-tools/latest/bin/retrace" "$m"; else "$sdk/cmdline-tools/latest/bin/retrace" "$m" "$trace"; fi
