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
- Transport: HTTPS over a Tailscale URL, no cleartext.
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
  `photoUri` needs a decision in the spec (transfer the file, or do not sync it).
- Other references to people that are NOT `entry_people`: `memories.peopleInvolved` (comma-separated
  local ids), `smart_reminders.relatedPersonId`, and `tracker_values.value` for PEOPLE trackers,
  which stores person **names**. All three need remapping or exclusion.
- Derived local counters, never sync: `tags.usageCount`, `people.mentionCount`, memory
  `entryCount`/`wordCount`/`photoCount`/`videoCount`.
- Media files live at `filesDir/{photo,video,voice,attachment}/<uuid>.<ext>`; `media.uri` holds the
  absolute path; there is no content hash. Media is metadata-only in the first protocol version.

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
   for that entry, insert the resolved ones). Resolve person `syncId` -> local id first; apply
   people before entries. An unresolvable person id fails the batch rather than dropping the link.
7. **Echo suppression:** the planned write-path triggers check a flag in `sync_state` in their
   `WHEN` clause. The apply service sets it inside the transaction, so applying server data does not
   enqueue it back into the outbox.
8. **Last-write-wins:** compare incoming `updatedAt` with the stored one and ignore older
   changes. The cursor is server-assigned and monotonic; the phone stores it in `sync_state`
   **inside the database**, so a restored database file carries a consistent (data, cursor) pair.
9. **People are inbound-only:** Kith-owned fields are overwritten by pulls. Local `isFavorite` and
   `mentionCount` handling is a spec decision.

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

## Open questions for the spec

- Does `title` count as metadata or body? Is `moodIcon`/`mood` metadata (assumed yes)?
- `people.photoUri` and `media`: transfer files, or exclude them from v1?
- How a phone-created person proposal is represented, and what the phone does when Kith rejects or
  merges it (rewrite `entry_people` to the surviving person).
- What a Kith-side person deletion does to `entry_people`, `memories.peopleInvolved` and PEOPLE
  tracker values (which store names).
- Restore policy: a restored database carries an older cursor, which converges by re-pulling;
  restoring a pre-sync backup relies on the deterministic `syncId` backfill above.
