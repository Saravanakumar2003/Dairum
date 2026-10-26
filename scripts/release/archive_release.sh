#!/usr/bin/env bash
# Archive a built release OUTSIDE the repo: the APK, the R8 mapping (needed to read crash traces),
# the merged R8 configuration, a SHA-256 of the APK and the signing certificate fingerprint.
#
#   scripts/release/archive_release.sh [archive-root]     default root: ~/dairum-releases
#
# Run it right after `./gradlew :app:assembleRelease`: app/build/outputs/mapping/release/mapping.txt
# is overwritten by every build, so a mapping that is not archived is lost. The mapping is not a
# secret, but it is useless without the APK it belongs to, so keep them together.
set -euo pipefail

repo="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd -P)"
root="${1:-$HOME/dairum-releases}"
apk="$repo/app/build/outputs/apk/release/app-release.apk"
mapdir="$repo/app/build/outputs/mapping/release"
[ -f "$apk" ] || { echo "No release APK at $apk. Build it first." >&2; exit 1; }
[ -f "$mapdir/mapping.txt" ] || { echo "No R8 mapping at $mapdir/mapping.txt (is minification on?)" >&2; exit 1; }
case "$root" in "$repo"/*) echo "Archive root must be outside the repo: $root" >&2; exit 1 ;; esac

sdk="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
bt="$(ls -d "$sdk"/build-tools/* | sort -V | tail -1)"
aapt2="$bt/aapt2"
vname="$("$aapt2" dump badging "$apk" | sed -n "s/.*versionName='\([^']*\)'.*/\1/p" | head -1)"
vcode="$("$aapt2" dump badging "$apk" | sed -n "s/.*versionCode='\([^']*\)'.*/\1/p" | head -1)"
dest="$root/${vname}-vc${vcode}"
[ ! -e "$dest" ] || { echo "Refusing to overwrite $dest (versionCode $vcode is already archived; bump it)." >&2; exit 1; }
mkdir -p "$dest"

cp "$apk" "$dest/dairum-${vname}-vc${vcode}.apk"
cp "$mapdir/mapping.txt" "$dest/mapping.txt"
[ -f "$mapdir/configuration.txt" ] && cp "$mapdir/configuration.txt" "$dest/r8-configuration.txt"
( cd "$dest" && shasum -a 256 "dairum-${vname}-vc${vcode}.apk" mapping.txt > SHA256SUMS )

jh="${JAVA_HOME:-/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home}"
JAVA_HOME="$jh" "$bt/apksigner" verify --print-certs "$apk" 2>&1 | grep -vE "^WARNING" | tee "$dest/signature.txt"
git -C "$repo" rev-parse HEAD > "$dest/git-commit.txt" 2>/dev/null || true
chmod -R go-rwx "$dest"

echo
echo "Archived to: $dest"
ls -l "$dest" | awk 'NR>1 {print "  " $5 "\t" $9}'
echo "Retrace a crash with: scripts/release/retrace.sh <stacktrace.txt> \"$dest\""
