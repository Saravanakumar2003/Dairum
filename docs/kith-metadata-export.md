# Dairum "Metadata for Kith" export (format `dairum-metadata`, version 1)

Where: More > Export Entries > **Metadata for Kith (.json)**, with the **Include titles** switch (on by
default, remembered). The file is written to `Documents/Data Exports/Dairum/dairum_metadata_<timestamp>.json`
like the other exports. It is a full snapshot, never a date range.

Selection: the same entries as the "All" export: not archived, not drafts, not in the Trash. Entries are
ordered by `createdAtEpochMs` ascending (ties by `id`). Compact JSON, UTF-8, strict (no trailing commas).

```json
{
  "format": "dairum-metadata",
  "version": 1,
  "exportedAtEpochMs": 1793113102140,
  "tz": "Asia/Kolkata",
  "appVersion": "1.0.0",
  "options": { "titles": true },
  "entries": [
    {
      "id": 4,
      "createdAtEpochMs": 1735668001005,
      "createdAtLocal": "2024-12-31T23:30:01.005",
      "title": "Year in review",
      "mood": { "label": "Grateful", "emoji": "🙏" },
      "category": "Goals",
      "tags": ["gratitude", "ideas"],
      "people": [{ "id": 1, "name": "Priya Sharma" }, { "id": 4, "name": "Meera Sharma" }]
    }
  ],
  "people": [{ "id": 1, "name": "Priya Sharma", "relationship": "Sister" }]
}
```

| Field | Notes |
|---|---|
| `format`, `version` | Check both on import. `version` changes only if the shape changes incompatibly. |
| `exportedAtEpochMs` | UTC epoch milliseconds. |
| `tz` | The phone's zone id at export time (for example `Asia/Kolkata`). |
| `appVersion` | Dairum's version name (a debug build ends in `-debug`). |
| `options.titles` | `false` means `title` is omitted from every entry. |
| `entries[].id` | Dairum's local database id. Not globally unique (see below). |
| `entries[].createdAtEpochMs` | UTC epoch ms, exactly as stored. Does not change with the phone's timezone. |
| `entries[].createdAtLocal` | ISO local date-time without offset, in `tz`. Display only; it changes if the phone's zone changes. |
| `entries[].title` | Omitted when titles are off; `null` when the entry has no title. |
| `entries[].mood` | `{label, emoji}` for Dairum's extended moods (stored ids 1-30), otherwise `null`. The stored number is never exported. |
| `entries[].category` | Category name or `null`. |
| `entries[].tags` | Tag names, sorted case-insensitively. |
| `entries[].people` | `{id, name}`, sorted by id. |
| `people[]` | Only people referenced by an exported entry: `{id, name, relationship}` (`relationship` may be `null`), sorted by id. Person ids match `entries[].people[].id`. |

Never in the file: entry text, formatting, media or file paths, weather, location, tracker values,
favorite/pinned/archived/draft flags, word counts, update times, contact details, notes, birthdays.

## Choosing an idempotency key

- Best: **`createdAtEpochMs`**. Stable across restores, reinstalls (when the data is restored), and
  timezone changes. Fails if the user edits an entry's date (backdating changes it, so the entry looks new)
  and could collide only for two entries created in the same millisecond.
- `id` alone is not safe: a fresh install restarts ids from 1, and restoring an older backup reuses ids for
  different entries. Using the pair (`createdAtEpochMs`, `id`) avoids false merges but treats a re-dated or
  re-numbered entry as new (a duplicate, never an overwrite).
- Do not use `createdAtLocal` as a key: it shifts with the phone's timezone.
