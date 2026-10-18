# androidTest database fixtures

`BaselineDatabaseTest` looks for these files here (they are gitignored; never commit them):

- `dairum_scrubbed.db` - a scrubbed copy of a real Dairum database
- `dairum_scrubbed.counts.tsv` - per-table row counts written by the scrub script (optional)

Produce them from a real database:

```bash
scripts/db/extract_db.sh debug   ~/dairum-recon/debug      # or: release
scripts/db/inspect_db.sh         ~/dairum-recon/debug/dairum.db
scripts/db/scrub_db.py           ~/dairum-recon/debug/dairum.db \
    app/src/androidTest/assets/fixtures/dairum_scrubbed.db
```

Without the fixture the fixture test is skipped (JUnit assumption) and only the fresh-database
test runs.
