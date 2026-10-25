#!/usr/bin/env bash
# Pull the live Dairum database off a connected phone.
#
#   extract_db.sh debug   [outdir]   com.dairum.debug  (debuggable: uses adb run-as)
#   extract_db.sh release [outdir]   com.dairum        (not debuggable: uses the in-app Backup ZIP)
#
# Output goes OUTSIDE the repo by default (~/dairum-recon/<variant>). The result is your real,
# unscrubbed diary: do not commit it, do not paste it anywhere.
set -euo pipefail

variant="${1:-}"
case "$variant" in
  debug)   pkg="com.dairum.debug" ;;
  release) pkg="com.dairum" ;;
  *) echo "usage: $0 debug|release [outdir]" >&2; exit 64 ;;
esac
out="${2:-$HOME/dairum-recon/$variant}"
mkdir -p "$out"

ADB="${ADB:-adb}"
command -v "$ADB" >/dev/null || ADB="$HOME/Library/Android/sdk/platform-tools/adb"
"$ADB" get-state >/dev/null || { echo "No device. Enable USB debugging and run: $ADB devices" >&2; exit 1; }

if ! "$ADB" shell pm list packages "$pkg" | grep -q "package:$pkg\$"; then
  echo "Package $pkg is not installed on this device." >&2; exit 1
fi

if [ "$variant" = debug ]; then
  # run-as only works for debuggable builds. Copy the WAL and SHM too: recent writes may live there.
  for f in dairum.db dairum.db-wal dairum.db-shm; do
    if "$ADB" shell run-as "$pkg" test -f "databases/$f"; then
      "$ADB" exec-out run-as "$pkg" cat "databases/$f" > "$out/$f"
      echo "pulled $f ($(wc -c < "$out/$f" | tr -d ' ') bytes)"
    fi
  done
else
  cat <<MSG
Release builds are not debuggable, so adb run-as is refused. Use the app's own backup:
  1. On the phone: Settings -> Backup & Restore -> Create backup (include media NOT needed, but harmless).
  2. Press Enter here once it says the backup finished.
MSG
  read -r _
  dir="/sdcard/Documents/Data Exports/Dairum"
  zip="$("$ADB" shell "ls -t '$dir'/diarum_backup_*.zip 2>/dev/null | head -1" | tr -d '\r')"
  [ -n "$zip" ] || { echo "No diarum_backup_*.zip in $dir" >&2; exit 1; }
  echo "newest backup: $zip"
  "$ADB" pull "$zip" "$out/backup.zip"
  # Only the database files are needed; leave media and config (which includes API-key prefs) in the zip.
  unzip -o -j "$out/backup.zip" 'database/dairum.db*' -d "$out"
  echo "NOTE: $out/backup.zip also contains your app preferences (encrypted API keys). Delete it when done."
fi

[ -s "$out/dairum.db" ] || {
  echo "No dairum.db was extracted. If the app has never been opened on this device, open it once, then retry." >&2
  exit 1
}

echo
echo "Extracted into: $out"
ls -l "$out"
echo "Next: scripts/db/inspect_db.sh \"$out/dairum.db\""
