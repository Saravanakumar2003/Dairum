#!/usr/bin/env bash
# Read-only health report for a Dairum database: user_version, Room identity hash, tables, row
# counts, integrity and foreign-key checks. Prints structure only: no titles, bodies or names.
# Works on a COPY, so the original and its -wal/-shm are never modified.
set -euo pipefail

src="${1:?usage: $0 path/to/dairum.db}"
[ -f "$src" ] || { echo "not found: $src" >&2; exit 1; }

work="$(mktemp -d)"; trap 'rm -rf "$work"' EXIT
cp "$src" "$work/db.sqlite"
for ext in -wal -shm; do [ -f "$src$ext" ] && cp "$src$ext" "$work/db.sqlite$ext"; done
q() { sqlite3 -batch "$work/db.sqlite" "$@"; }

echo "== file ==";            ls -l "$src" | awk '{print $5 " bytes  " $9}'
echo "== PRAGMA user_version =="; q "PRAGMA user_version;"
echo "== room_master_table (id, identity_hash) =="; q "SELECT id, identity_hash FROM room_master_table;" || echo "(no room_master_table)"
echo "== tables =="; q "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' ORDER BY name;"
echo "== row counts =="
for t in $(q "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT IN ('android_metadata') ORDER BY name;"); do
  printf '%-28s %s\n' "$t" "$(q "SELECT COUNT(*) FROM \"$t\";")"
done
echo "== PRAGMA integrity_check ==";   q "PRAGMA integrity_check;"
echo "== PRAGMA foreign_key_check (table|rowid|parent|fkid; empty = clean) =="; q "PRAGMA foreign_key_check;" | head -50
echo "== journal_entries.deletedAt present? =="; q "SELECT COUNT(*) FROM pragma_table_info('journal_entries') WHERE name='deletedAt';"
echo "== journal_entries soft-deleted / total =="; q "SELECT COUNT(*) FILTER (WHERE deletedAt IS NOT NULL), COUNT(*) FROM journal_entries;" 2>/dev/null || true
