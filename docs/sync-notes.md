# Dairum sync notes (phone side)

Input for the Dairum <-> Kith protocol spec. Facts here come from the code at tag
`pre-sync-baseline` (Room schema v5, identity hash `1837df50fe8ecbfea4301570eff7b145`, 35 tables).
Nothing in this file is implemented yet unless it says "done".

## Decisions already taken

- The phone owns diary content. Kith owns people.
- Phone-created people are proposals to Kith; quick-create in the editor must work offline.
- Last-write-wins, with a server-assigned monotonic cursor.
- Tombstones; a synced row is never hard-deleted.
- Embeddings and all AI/chat data never sync. `locationData`, `weatherData` and
  `additionalLocationsData` never sync.
- The diary body (`content` spans JSON, `plainContent`) is optional in the protocol; metadata first.
- Transport: two routes, both HTTPS, **no domain and no cleartext**: (1) the LAN, with a pinned
  self-signed certificate; (2) Tailscale. The client must trust exactly the pinned certificate.
  (OkHttp's `CertificatePinner` alone is not enough: it still requires a chain the platform
  trusts, which a self-signed certificate does not have; a custom trust manager or
  `network_security_config` that trusts only that certificate is needed.)
- Single write path enforced with SQLite triggers (planned for the 5->6 migration).

## Schema facts the protocol needs

- Every primary key is a local `Long` autoincrement (AUTOINCREMENT, so ids are never reused). Two
  tables have String keys, `ai_models` and `chat_sessions`, and neither syncs. A UUID `syncId` column
  is to be added; local ids stay as primary keys.
- All timestamps are epoch milliseconds (`Long`). Core tables use `System.currentTimeMillis()` /
  the injected clock. AI/chat entities use `LocalDateTime` stored as epoch millis in the system zone.
- `updatedAt` exists on `journal_entries`, `reminder_configs`, `memories`, `mood_checkin_config`,
  `location_nicknames` and the AI/chat tables. It is missing on people, tags, categories, media,
  trackers, tracker values and all join tables.
- `journal_entries.deletedAt` already exists and means "in the trash". Today "Delete forever" and
  "Empty trash" hard-delete rows, so no tombstone survives.
- Rich text: `journal_entries.content` is a JSON array of `{start,end,style}` spans (offsets and
  style names only); `plainContent` is the plain text. Mention spans carry no person id; the link
  to a person is `entry_people`.
- People fields: `name`, `relationship`, `photoUri` (a local file path), `notes`, `email`, `phone`,
  `birthday`, `importantDates` (JSON text), `isFavorite`. `mentionCount` is a local derived counter.
  `photoUri` is excluded from v1 (see Decisions).
- Other references to people that are NOT `entry_people`: `memories.peopleInvolved` (comma-separated
  local ids), `smart_reminders.relatedPersonId` (device-local, never syncs), and
  `tracker_values.value` for PEOPLE trackers, which stores person **names**. The first and third
  are excluded from v1 (see Decisions).
- Derived local counters, never sync: `tags.usageCount`, `people.mentionCount`, memory
  `entryCount`/`wordCount`/`photoCount`/`videoCount`.
- Media files live at `filesDir/{photo,video,voice,attachment}/<uuid>.<ext>`; `media.uri` holds the
  absolute path; there is no content hash. Media files are excluded from v1 (see Decisions).

## Writers by method (audited at `pre-sync-baseline`)

Method: every DAO method annotated `@Insert`, `@Update`, `@Delete`, or `@Query` starting
`INSERT`/`UPDATE`/`DELETE`, on tables that may sync. Callers were resolved by receiver type. There
is no `@RawQuery`, no `openHelper` write, and no `execSQL` outside `Migrations.kt`. Callers are
repository classes only, except `DatabaseCallback` (seeds default categories through a second Room
instance). Receivers, widgets and workers do not write these tables.

"(P2)" marks a method changed in Step 2 (it now stamps `updatedAt`, or runs in a transaction).

| DAO method | Kind | Caller |
|---|---|---|
| `JournalEntryDao.insert` | INSERT REPLACE | `JournalRepo.createEntry` |
| `JournalEntryDao.update` | UPDATE (full row) | `JournalRepo.updateEntry` (P2: keeps `deletedAt`) |
| `JournalEntryDao.deleteById` | DELETE | `JournalRepo.deleteEntry` (P2: also deletes media files) |
| `JournalEntryDao.emptyTrash` | DELETE | `JournalRepo.emptyTrash` (P2: also deletes media files) |
| `JournalEntryDao.updateFavorite` / `updateArchived` / `updatePinned` | UPDATE | `JournalRepo.toggleFavorite` / `archiveEntry` / `togglePin` (P2: bump `updatedAt`) |
| `JournalEntryDao.softDelete` / `restoreEntry` | UPDATE | `JournalRepo.softDelete` / `restoreEntry` (P2: bump `updatedAt`) |
| `JournalEntryDao.touch` | UPDATE | new in P2; used by the three `set*ForEntry` methods |
| `JournalEntryDao.delete`, `emptyOldTrash` | DELETE | no callers |
| `PersonDao.insert` | INSERT REPLACE | `PersonRepo.create` (P2: stamps `createdAt`) |
| `PersonDao.update` | UPDATE (full row) | `PersonRepo.update` (P2: keeps `createdAt`) |
| `PersonDao.deleteById` | DELETE | `PersonRepo.delete` |
| `PersonDao.updateFavorite` | UPDATE | `PersonRepo.toggleFavorite` |
| `PersonDao.incrementMentionCount` / `decrementMentionCount` | UPDATE | `JournalRepo.setPeopleForEntry` |
| `PersonDao.insertEntryPerson` | INSERT IGNORE | `JournalRepo.setPeopleForEntry` |
| `PersonDao.deleteAllPeopleForEntry` | DELETE | `JournalRepo.setPeopleForEntry` |
| `PersonDao.delete`, `deleteEntryPerson` | DELETE | no callers |
| `TagDao.insert` | INSERT REPLACE | `TagRepo.create`, `TagRepo.createOrGet` |
| `TagDao.update` / `deleteById` | UPDATE / DELETE | `TagRepo` |
| `TagDao.incrementUsageCount` / `decrementUsageCount` | UPDATE | `JournalRepo.setTagsForEntry` |
| `TagDao.insertEntryTag` | INSERT IGNORE | `JournalRepo.setTagsForEntry` |
| `TagDao.deleteAllTagsForEntry` | DELETE | `JournalRepo.setTagsForEntry` |
| `TagDao.insertOrIgnore`, `delete`, `deleteEntryTag` | - | no callers |
| `CategoryDao.insert` | INSERT REPLACE | `CategoryRepo.create`, `DatabaseCallback.onCreate` |
| `CategoryDao.update` / `deleteById` / `clearDefaultFlag` / `setAsDefault` / `updateSortOrder` | UPDATE / DELETE | `CategoryRepo` |
| `CategoryDao.delete` | DELETE | no callers |
| `TrackerDao.insertTracker` | INSERT REPLACE | `TrackerRepo` |
| `TrackerDao.updateTracker` / `deleteTrackerById` / `updateTrackerActive` / `updateTrackerSortOrder` | UPDATE / DELETE | `TrackerRepo` |
| `TrackerDao.insertValue` | INSERT REPLACE | `JournalRepo.setTrackerValues`, `TrackerRepo` |
| `TrackerDao.deleteAllValuesForEntry` | DELETE | `JournalRepo.setTrackerValues` |
| `TrackerDao.deleteValueForEntryAndTracker` | DELETE | `TrackerRepo` |
| `TrackerDao.deleteTracker`, `insertValues`, `updateValue`, `deleteValue`, `deleteValueById` | - | no callers |
| `MediaDao.insert` / `insertAll` | INSERT REPLACE | `MediaRepo.addMedia` / `addMediaList` |
| `MediaDao.update` / `deleteById` / `deleteAllForEntry` | UPDATE / DELETE | `MediaRepo` |
| `MediaDao.delete`, `updateSortOrder` | - | no callers |
| `MemoryDao.insertMemory`, `insertMemoryEntries`, `insertMilestone`, `insertMemoryTracker` | INSERT REPLACE | `MemoryRepo` |
| `MemoryDao.updateMemory`, `deleteMemory`, `removeEntryFromMemory`, `updateMilestone`, `deleteMilestone`, `updateMemoryTracker`, `deleteMemoryTracker`, `updateFavorite`, `updateMemoryStats` | UPDATE / DELETE | `MemoryRepo` |
| `MemoryDao.insertMemoryEntry`, `deleteMemoryEntry`, `deleteAllEntriesFromMemory` | - | no callers |
| `LocationNicknameDao.upsert` | INSERT REPLACE | `LocationNicknameRepo.setNickname` |
| `LocationNicknameDao.deleteByKey` | DELETE | `LocationNicknameRepo.removeNickname` |

Non-DAO writers:

- Backup restore and `importCompleteExport` are **whole-file swaps**: close the singleton database,
  delete the live `.db`/`-wal`/`-shm`, copy the backup file over. `restoreBackup` also wipes the
  media directories; `importCompleteExport` copies attachments over the existing ones. Neither
  checks the schema version, and the "merge" flag is ignored.
- Callers of the repositories: `EditorViewModel` (save, autosave, quick-create person/tag, media),
  `HomeViewModel`, `EntryViewViewModel`, `FavoritesViewModel`, `CategoryEntriesViewModel`,
  `TagEntriesViewModel`, `StoryViewModel`, `ContextualSearchViewModel`, `TrashViewModel`, the Manage
  People/Tags/Categories/Trackers view models, `MemoriesViewModel`, `MemoryDetailViewModel`.

Still not bumping a parent's `updatedAt`: media add/update/delete (the editor save bumps it through
`updateEntry`), and any change to tags/categories/trackers/memories rows (those tables have no
`updatedAt` yet).

## REPLACE and CASCADE findings

Foreign keys with `ON DELETE CASCADE`:

- `journal_entries` -> `entry_tags`, `entry_people`, `media`, `tracker_values`, `memory_entries`,
  `journal_embeddings`
- `people` -> `entry_people`; `tags` -> `entry_tags`; `custom_trackers` -> `tracker_values`
- `memories` -> `memory_entries`, `memory_milestones`, `memory_trackers`
- `journal_entries.categoryId` has **no** foreign key: deleting a category leaves a dangling id.
  `smart_reminders.relatedPersonId`/`relatedEntryId`, `memories.peopleInvolved` also have none.

`@Insert(onConflict = REPLACE)` is used on `journal_entries`, `people`, `media`, `tags`,
`categories`, `custom_trackers`, `tracker_values`, `memories`, `memory_entries`,
`memory_milestones`, `memory_trackers`, `location_nicknames` (and the AI/chat/gratitude tables).
SQLite implements REPLACE as delete-then-insert, and with foreign keys on the delete cascades.
Every current caller passes `id = 0`, so each insert is a plain insert today. A REPLACE with an
existing id on a parent row would silently delete its children. `JournalRepo.createEntry` is the
riskiest because the domain object carries an id.

Remaining hard deletes that cascade: `JournalRepo.deleteEntry` and `emptyTrash`, `PersonRepo.delete`,
`TagRepo.delete`, `TrackerRepo.deleteTracker`, `MemoryRepo.deleteMemory`, `MediaRepo.deleteMedia`.
`emptyOldTrash` is unused although the Trash screen says items are deleted after 30 days.

Also, before Step 2: the three `set*ForEntry` methods deleted rows then re-inserted them with no
transaction (done: now atomic), and several `toEntity()` mappers were lossy (done: `deletedAt`,
`createdAt`). One lossy projection remains by design, `JournalRepositoryImpl.PersonEntity.toDomain`
(display only; verified that nothing saves it back).

## Apply-DAO design (server changes -> local database)

Not implemented. A dedicated `SyncApplyDao` plus an apply service. It must never call the
repositories or the existing REPLACE DAOs.

1. **One transaction per batch**, via `database.withTransaction`. The cursor update and the rows it
   covers commit together, so a crash re-applies the batch idempotently.
2. **`PRAGMA defer_foreign_keys = ON`** at the start of the transaction (it resets at commit), so
   rows may arrive in any order within a batch. Run `PRAGMA foreign_key_check` before commit and
   roll back the batch if it reports anything.
3. **Update path:** look up the local row with `SELECT id FROM <table> WHERE syncId = :syncId`. If
   present, run a **partial `UPDATE <table> SET <synced columns> WHERE id = :id`**. Only columns
   present in the payload are written, so local-only data (`locationData`, `weatherData`,
   `additionalLocationsData`, the body when the payload is metadata-only, local counters) is never
   touched. No full-row `@Update`, which would clobber them.
4. **Insert path:** a plain `@Insert(onConflict = ABORT)` with `id = 0` (autogenerated local id).
   The unique index on `syncId` prevents duplicates; if the insert races and aborts, retry as an
   update. **No REPLACE anywhere.**
5. **Tombstones only:** a server deletion is `UPDATE ... SET deletedAt = :t, updatedAt = :u`
   (plus the purge below for entries). Never `DELETE` a synced row.
6. **Link sets:** replace an entry's person list as a set inside the transaction (delete the rows
   for that entry, insert the resolved ones). A link arrives as a Kith person id when the phone
   knows one, otherwise as a candidate `syncId`; resolve it to a local row by `kithPersonId`, then by
   `syncId`. Apply people before entries. An unresolvable person id fails the batch rather than
   dropping the link.
7. **Echo suppression:** the planned write-path triggers check a flag in `sync_state` in their
   `WHEN` clause. The apply service sets it inside the transaction, so applying server data does not
   enqueue it back into the outbox.
8. **Last-write-wins:** compare incoming `updatedAt` with the stored one and ignore older
   changes. The cursor is server-assigned and monotonic; the phone stores it in `sync_state`
   **inside the database**, so a restored database file carries a consistent (data, cursor) pair.
9. **People:** Kith-owned fields are overwritten by pulls into rows that have a `kithPersonId`.
   Candidate rows (no `kithPersonId` yet) are pushed as proposals and keep their own `syncId`.
   `mentionCount` is a local derived counter and never syncs. A Kith deletion or a `merged_into`
   message is applied as described under Decisions; it never deletes rows or links.

## Purge / tombstone proposal (deferred to the 5->6 migration)

Replace "Delete forever" / "Empty trash" (and the unused 30-day `emptyOldTrash`) with a purge that
keeps a tombstone row so the server can learn the entry is gone.

- Schema: `ALTER TABLE journal_entries ADD COLUMN purgedAt INTEGER DEFAULT NULL`, plus
  `val purgedAt: Long? = null` on `JournalEntryEntity`.
- Purge, in one transaction:
  1. Read the entry's media rows (files are deleted after commit, as now).
  2. Blank the body and local data: `title`, `content`, `plainContent`, `locationData`,
     `weatherData`, `additionalLocationsData`, and `wordCount = 0`.
  3. Set `purgedAt` and `updatedAt` to the current time; keep `deletedAt`.
  4. Delete child rows: media, tags, people links, tracker values, memory links, embeddings.
- The Trash screen's query gains `AND purgedAt IS NULL`. Every other query already filters
  `deletedAt IS NULL`, and a purged row keeps `deletedAt`, so it stays hidden everywhere.
- Counting queries that do not filter `deletedAt` (e.g. `getEntryCount()`, used for backup
  metadata) will include tombstones; review when implementing.
- The same migration adds `syncId` (unique) and `updatedAt`/`deletedAt` to the other synced
  tables, the `sync_state` and `sync_outbox` tables, and the triggers.
- `syncId` backfill: deterministic UUIDv5 over (table, local id, `createdAt`), computed in Kotlin in
  the migration, so restoring a pre-sync backup reproduces the same ids instead of duplicating rows.
  Tables without `createdAt` need a fallback; see Decisions.
- `people` also gains a nullable `kithPersonId` (see Decisions). It is not a replacement for `syncId`.

## Decisions on the former open questions

**Entries**

- `title` and mood (`moodLevel`, `moodIcon`) are metadata and sync. A separate **"Send titles"**
  switch, default on, controls whether `title` is sent. When it is off the field must be absent from
  the payload, and an absent field must never clear a value the server already has (partial update).
- Trashed entries (`deletedAt` set, not purged) sync as **hidden**. Purged entries (`purgedAt` set)
  are **tombstones**.

**Media**

- `photoUri` (people) and media files are **excluded from v1**. The `media` table has no v1 sync
  role; a metadata-only `media` row sync is not part of v1 and would need its own decision.

**People**

- A phone-created person is a **candidate** with its own deterministic `syncId` (same scheme as the
  other backfills). Add a nullable **`kithPersonId`** column to `people`. Many local rows may map to
  one Kith person. **`syncId` is never reused as Kith's id.**
- Entry-person links sync as the **Kith person id if known, else the candidate `syncId`**.
- A **Kith person deletion hides the person on the phone and keeps the link rows**: set
  `people.deletedAt` and do not delete anything, so `entry_people` rows stay. Implication: every
  query on `people` must then filter `deletedAt IS NULL` (none does today: `PersonDao.getAllFlow`,
  `getAll`, `search`, `getFavoritePeople`, the three `...WithCount`/`getTop...` queries, and
  `getPeopleForEntry*`), or hidden people reappear in pickers.
- A **merge arrives as `merged_into`**; the phone rewrites `kithPersonId` on the affected local rows
  to the surviving Kith person id. Links keep pointing at the same local rows.
- `tracker_values` for PEOPLE trackers (they store names) and `memories.peopleInvolved` (local ids)
  are **excluded from v1**.

**Identifiers and triggers**

- UUIDv5 over (table, local id, `createdAt`) needs a **fallback for tables without `createdAt`**:
  `memory_entries` (has `addedAt`), `memory_milestones`, `memory_trackers`, `location_nicknames`
  (has `updatedAt`) and the two join tables, which have no id of their own. Use a unique natural key
  where one exists (`tags.name`, `location_nicknames.locationKey`); otherwise (table, local id),
  which is stable because AUTOINCREMENT ids are never reused. Join rows are covered by their parent
  (`entry_people`/`entry_tags` sync as a set on the entry).
- **Triggers need a recursion guard plus the sync-apply flag.** A trigger that updates its own table
  (to bump `updatedAt`) must not re-fire itself: guard with `WHEN NEW.updatedAt IS OLD.updatedAt`
  (SQLite's `recursive_triggers` is off by default, but do not depend on that), and also check the
  `sync_state` flag so applying server data does not enqueue it again.
- **Counting queries that ignore `deletedAt` will include tombstones.** Today these ignore it:
  `JournalEntryDao.getEntryCount()` (`SELECT COUNT(*) FROM journal_entries`, used for backup
  metadata), the `LEFT JOIN journal_entries` counts in `PersonDao` and `TagDao`, the
  `INNER JOIN journal_entries` lists in `MediaDao` (the Files screen) and `TrackerDao`. Trashed
  entries are already counted there today; purged tombstone rows would be too unless the queries
  gain `deletedAt IS NULL` (or `purgedAt IS NULL`). Purging removes a tombstone's children first, so
  the join queries stop returning its media, tags and people, but the plain count still includes it.

**Restore**

- A restored database carries an older cursor, which converges by re-pulling. Restoring a pre-sync
  backup relies on the deterministic `syncId` backfill. Restore and sync-state policy beyond that is
  left to the restore step.
