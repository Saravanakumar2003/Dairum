#!/usr/bin/env python3
"""Make a shareable test fixture from a real Dairum database.

    scrub_db.py IN.db OUT.db

Default-deny: every TEXT and BLOB column is replaced with a placeholder unless it is on the
structural allow-list below (ids, enum names, icon names, the span-offset JSON). NULLs stay NULL
so the shape of the data is preserved. Embeddings become same-length zero blobs. Row counts,
foreign keys, ids and timestamps are untouched, so the result still exercises migrations.

Safety nets: secure_delete + VACUUM INTO (no freed pages holding old text), then a byte-level scan
of the output for any original long text value; if one survives, OUT.db is deleted and exit is 2.
Timestamps are kept, so the file still reveals *when* you wrote, just not *what*.

Writes OUT.db and OUT.counts.tsv (per-table row counts, used by BaselineDatabaseTest).
Run with:  python3 -I scripts/db/scrub_db.py IN.db OUT.db
"""
import json
import os
import shutil
import sqlite3
import sys
import tempfile

# TEXT columns kept verbatim, by name (any table). All structural: identifiers and enum names that
# Room parses. Review this list before trusting the output with anything new.
KEEP_ANY = {
    "id", "sessionId",                                  # TEXT primary/foreign keys (chat_sessions, ai_models)
    "type", "format", "priority", "reminderType",       # enum names parsed by Room
    "mimeType", "icon", "emoji", "moodIcon", "backgroundColor",
    "scheduleDays", "enabledDays", "role", "model", "embeddingModel", "modelType",
}
# (table, column) pairs kept verbatim. journal_entries.content is the span JSON: offsets and style names only.
KEEP_PAIRS = {("journal_entries", "content")}
# Non-text columns that are personal data.
NULL_COLUMNS = {"birthday"}
ZERO_REAL_COLUMNS = {"latitude", "longitude"}
SKIP_TABLES = {"android_metadata", "room_master_table"}
MIN_LEAK_LEN = 6


def user_tables(con):
    return [r[0] for r in con.execute(
        "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' ORDER BY name")]


def main():
    if len(sys.argv) != 3:
        sys.exit(__doc__)
    src, out = sys.argv[1], sys.argv[2]
    if sqlite3.sqlite_version_info < (3, 27):
        sys.exit(f"need SQLite >= 3.27 for VACUUM INTO, have {sqlite3.sqlite_version}")
    if os.path.abspath(src) == os.path.abspath(out):
        sys.exit("refusing to overwrite the input")

    work = tempfile.mkdtemp(prefix="dairum-scrub-")
    try:
        # Work on a copy (with -wal/-shm so uncheckpointed rows are included).
        wdb = os.path.join(work, "work.db")
        shutil.copyfile(src, wdb)
        for ext in ("-wal", "-shm"):
            if os.path.exists(src + ext):
                shutil.copyfile(src + ext, wdb + ext)

        con = sqlite3.connect(wdb)
        con.execute("PRAGMA secure_delete=ON")
        user_version = con.execute("PRAGMA user_version").fetchone()[0]
        identity = con.execute("SELECT identity_hash FROM room_master_table").fetchall() \
            if "room_master_table" in user_tables(con) else []

        # 1) Remember original long text values for the leak scan (never printed).
        originals = set()
        plan = []  # (table, column, kind)
        for t in user_tables(con):
            if t in SKIP_TABLES:
                continue
            for _cid, col, ctype, _nn, _dflt, _pk in con.execute(f'PRAGMA table_info("{t}")'):
                affinity = (ctype or "").upper()
                if col in NULL_COLUMNS:
                    plan.append((t, col, "null"))
                elif col in ZERO_REAL_COLUMNS:
                    plan.append((t, col, "zero"))
                elif "TEXT" in affinity and col not in KEEP_ANY and (t, col) not in KEEP_PAIRS:
                    plan.append((t, col, "text"))
                    for (v,) in con.execute(f'SELECT DISTINCT "{col}" FROM "{t}" WHERE "{col}" IS NOT NULL'):
                        if isinstance(v, str) and len(v) >= MIN_LEAK_LEN:
                            originals.add(v)
                elif "BLOB" in affinity:
                    plan.append((t, col, "blob"))

        # 2) Scrub.
        with con:
            for t, col, kind in plan:
                if kind == "text":
                    sql = f"UPDATE \"{t}\" SET \"{col}\" = 's' || rowid WHERE \"{col}\" IS NOT NULL"
                elif kind == "blob":
                    sql = f"UPDATE \"{t}\" SET \"{col}\" = zeroblob(length(\"{col}\")) WHERE \"{col}\" IS NOT NULL"
                elif kind == "null":
                    sql = f"UPDATE \"{t}\" SET \"{col}\" = NULL"
                else:
                    sql = f"UPDATE \"{t}\" SET \"{col}\" = 0.0"
                con.execute(sql)

        # 2b) journal_entries.content is kept only if it really is span JSON ([{start,end,style}]).
        #     Anything else (older formats, plain text) is replaced so no body text slips through.
        if "journal_entries" in user_tables(con):
            replaced = 0
            with con:
                for rid, raw in con.execute("SELECT rowid, content FROM journal_entries").fetchall():
                    if raw is None:
                        continue
                    try:
                        spans = json.loads(raw)
                        ok = isinstance(spans, list) and all(
                            isinstance(x, dict) and set(x) <= {"start", "end", "style"} for x in spans)
                    except ValueError:
                        ok = False
                    if not ok:
                        con.execute("UPDATE journal_entries SET content='[]' WHERE rowid=?", (rid,))
                        replaced += 1
            if replaced:
                print(f"note: {replaced} journal_entries.content value(s) were not span JSON and were blanked")

        # 3) Rewrite into a fresh single file so freed pages with old text are gone.
        if os.path.exists(out):
            os.remove(out)
        con.execute("VACUUM INTO ?", (out,))
        con.close()

        # 4) Verify the output.
        res = sqlite3.connect(out)
        res.execute(f"PRAGMA user_version={int(user_version)}")  # VACUUM INTO may not carry it; set explicitly
        res.commit()
        assert res.execute("PRAGMA user_version").fetchone()[0] == user_version
        if identity:
            assert res.execute("SELECT identity_hash FROM room_master_table").fetchall() == identity, "identity hash changed"
        assert res.execute("PRAGMA integrity_check").fetchall() == [("ok",)], "integrity_check failed"
        counts = {t: res.execute(f'SELECT COUNT(*) FROM "{t}"').fetchone()[0]
                  for t in user_tables(res) if t not in SKIP_TABLES and t != "sqlite_sequence"}
        res.close()
        for ext in ("-wal", "-shm", "-journal"):
            if os.path.exists(out + ext):
                os.remove(out + ext)

        data = open(out, "rb").read()
        leaks = sum(1 for v in originals if v.encode("utf-8") in data)
        if leaks:
            os.remove(out)
            sys.exit(f"LEAK CHECK FAILED: {leaks} original text value(s) still present; output deleted. "
                     "Extend the scrub rules before using this database.")

        counts_path = (out[:-3] if out.endswith(".db") else out) + ".counts.tsv"
        with open(counts_path, "w") as f:
            for t in sorted(counts):
                f.write(f"{t}\t{counts[t]}\n")

        scrubbed_cols = {}
        for t, col, kind in plan:
            scrubbed_cols.setdefault(kind, []).append(f"{t}.{col}")
        print(f"user_version={user_version}  tables={len(counts)}  rows={sum(counts.values())}")
        print(f"scrubbed: {sum(len(v) for v in scrubbed_cols.values())} columns "
              f"({', '.join(f'{k}:{len(v)}' for k, v in scrubbed_cols.items())}); "
              f"leak scan checked {len(originals)} distinct original values: none found")
        print(f"wrote {out}\nwrote {counts_path}")
    finally:
        shutil.rmtree(work, ignore_errors=True)


if __name__ == "__main__":
    main()
