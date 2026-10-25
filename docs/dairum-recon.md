# Dairum recon (Room schema, data model, backup, network)

Facts only, written from the Step 0 recon and brought up to date with the code at tag
`pre-sync-baseline` (commit `520be228`). Decisions and the sync design are in `docs/sync-notes.md`.

## 1. Room

- Database class: `app/src/main/java/com/dairum/data/local/database/DairumDatabase.kt`.
  Version **5**, `exportSchema = true`, **35 entities**, database file name `dairum.db`.
- Schema JSON: `app/schemas/com.dairum.data.local.database.DairumDatabase/5.json` is the only
  committed schema file: the frozen v5 baseline, identity hash
  `1837df50fe8ecbfea4301570eff7b145`. Location comes from the ksp arg `room.schemaLocation` in
  `app/build.gradle.kts`. Room rewrites `<version>.json` on every build, so a JSON only describes the
  entities at the last build of that version (see `CLAUDE.md`). `1.json` to `4.json` (regenerated
  from later entities) and `6.json` to `9.json` (from an earlier version numbering) were removed.
- Builder: `di/DatabaseModule.kt` adds `DatabaseCallback` and `Migrations.ALL_MIGRATIONS`. There is
  no `fallbackToDestructiveMigration`.
- `DatabaseCallback.onCreate` seeds six default categories (General, Personal, Work, Travel,
  Health, Goals) by building a **second** `Room.databaseBuilder` on the same file, without
  migrations, on `Dispatchers.IO`.
- A fresh install creates version 5 with identity hash `1837df50...` (verified on an emulator: 35
  tables, `integrity_check` ok, six categories). There is no pre-existing real-device database;
  the app is used from a fresh install.
- Debug and release builds have separate databases: `com.dairum.debug` (`applicationIdSuffix`)
  and `com.dairum`.

### Migrations (`data/local/database/Migrations.kt`)

| Migration | Change |
|---|---|
| 1 -> 2 | `ALTER TABLE journal_entries ADD COLUMN deletedAt INTEGER` |
| 2 -> 3 | create `gratitude_folders`, `gratitude_prompts`, `affirmation_folders`, `affirmations` (+ indexes) |
| 3 -> 4 | `journal_entries.additionalLocationsData`, `media.inlineKey` |
| 4 -> 5 | create `location_nicknames` (+ unique index on `locationKey`) |

No migration creates these 11 tables: `ai_models`, `ai_conversations`, `journal_embeddings`,
`weekly_reflections`, `pattern_results`, `smart_summaries`, `chat_sessions`, `chat_messages`,
`chat_embeddings`, `reflection_memories`, `conversation_context`. A fresh install creates them
through Room; a database created by an older build that lacked them would fail Room's identity
check on open. (Not applicable to a fresh install.)

### Entities

All primary keys are `Long` autoincrement unless noted.

| Table | Columns (type) | Foreign keys | Indexes |
|---|---|---|---|
| `journal_entries` | title?, content (spans JSON), plainContent, categoryId?, createdAt, updatedAt, moodLevel?, moodIcon?, weatherData? (JSON), locationData? (JSON), additionalLocationsData? (JSON), isFavorite, isArchived, isDraft, isPinned, wordCount, backgroundColor?, deletedAt? | none (`categoryId` is unconstrained) | categoryId, createdAt, moodLevel, isFavorite, isArchived |
| `categories` | name, color (Int ARGB), icon, sortOrder, isDefault, createdAt | none | none |
| `tags` | name, color?, usageCount, createdAt | none | name (unique) |
| `entry_tags` | PK (entryId, tagId) | entry, tag; CASCADE | entryId, tagId |
| `media` | entryId, type (enum), uri, thumbnailUri?, fileName, mimeType, sizeBytes, duration?, width?, height?, sortOrder, caption?, inlineKey?, createdAt | entry; CASCADE | entryId, type, createdAt |
| `custom_trackers` | name, type (enum), icon, unit?, minValue?, maxValue?, options? (JSON), isBuiltIn, isActive, sortOrder, createdAt | none | none |
| `tracker_values` | entryId, trackerId, value (String), createdAt | entry, tracker; CASCADE | entryId, trackerId |
| `people` | name, relationship?, photoUri?, notes?, email?, phone?, birthday?, importantDates? (JSON), isFavorite, mentionCount, createdAt | none | name (non-unique) |
| `entry_people` | PK (entryId, personId) | entry, person; CASCADE | entryId, personId |
| `memories` | title, description?, coverImageUri?, startDate, endDate, location?, locationData?, isFavorite, color?, icon?, tags?, moodSummary?, totalTimeSpent, entryCount, wordCount, photoCount, videoCount, peopleInvolved? (comma-separated ids), highlights?, trackerSummary?, createdAt, updatedAt | none | startDate, endDate, isFavorite |
| `memory_entries` | PK (memoryId, entryId), sortOrder, addedAt | memory, entry; CASCADE | memoryId, entryId |
| `memory_milestones` | memoryId, title, description?, milestoneDate, relatedEntryId?, icon?, sortOrder | memory; CASCADE | memoryId |
| `memory_trackers` | memoryId, name, type (enum), icon?, unit?, goalValue?, currentValue?, progress | memory; CASCADE | memoryId |
| `export_configs` | name, format, schedule fields, isEnabled, exportPath?, includeMedia, lastExportAt?, lastExportStatus?, createdAt | none | none |
| `export_history` | configId?, format, filePath, fileName, entriesCount, fileSize, isSuccess, errorMessage?, exportedAt | none | exportedAt |
| `reminder_configs` | type, name, description?, isEnabled, schedule fields, notificationTitle, notificationMessage, priority, createdAt, updatedAt | none | none |
| `smart_reminders` | reminderType, title, description?, reminderDate, repeatYearly, relatedPersonId?, relatedEntryId?, isEnabled, notifyDaysBefore, lastNotifiedAt?, metadata?, createdAt | none | reminderDate, reminderType |
| `notification_history` | type, sourceId, title, message, wasTapped, sentAt | none | sentAt |
| `mood_checkin_config` | isEnabled, intervalHours, startHour, endHour, lastPromptAt?, enabledDays, createdAt, updatedAt | none | none |
| `location_nicknames` | locationKey, nickname, latitude, longitude, placeName?, address?, updatedAt | none | locationKey (unique) |
| `gratitude_folders`, `affirmation_folders` | name, emoji, description?, (`backgroundAudioUri?`), createdAt | none | none |
| `gratitude_prompts`, `affirmations` | folderId, text/prompt, (audioUri?, useTts, sortOrder), createdAt | folder; CASCADE | folderId |
| `ai_models` | id (String PK), name, description, filePath, sizeBytes, modelType, quantization?, contextLength, isDefault, isDownloaded, downloadUrl?, createdAt, lastUsedAt? | none | modelType, isDefault |
| `journal_embeddings` | entryId, embedding (BLOB), embeddingDimension, model, version, createdAt, updatedAt | entry; CASCADE | entryId (unique), model |
| `weekly_reflections`, `pattern_results`, `smart_summaries`, `ai_conversations` | LLM outputs and chat transcripts (text and JSON columns) | none | see source |
| `chat_sessions` | id (String PK), title, summary?, isSaved, isArchived, messageCount, lastMessageAt?, createdAt, updatedAt | none | createdAt, isSaved, isArchived |
| `chat_messages`, `chat_embeddings`, `conversation_context` | message text/metadata, embedding BLOB, per-session context | messages and context -> session; embeddings -> message | see source |
| `reflection_memories` | content, category, sourceMessageIds?, sourceSessionIds?, embedding? (BLOB), embeddingModel?, ... | none | see source |

Per-index detail for the AI/chat group is in `entity/AIEntities.kt` and
`entity/ConversationEntities.kt`; those tables never sync.

### Tests

`app/src/androidTest` (run with `./gradlew :app:connectedDebugAndroidTest`): a baseline test that
opens a scrubbed copy of a real database (skipped when no fixture is present), an `@Ignore`d
`MigrationChainTest` (waits for the 5->6 migration), in-memory Room tests for the repository write
paths, and Compose tests for the Trash screen. `room-testing` is a dependency and `app/schemas` is an
assets directory for `androidTest`.

## 2. People

- `PersonEntity` fields are listed in the table above; `Person` (domain) mirrors them and, since
  Step 2, carries `createdAt` (0 = not read from the database).
- Attachment to entries: join table `entry_people` (composite PK, CASCADE on both sides).
  `JournalRepositoryImpl.setPeopleForEntry` replaces the set inside one transaction, adjusts
  `people.mentionCount` (a local counter) with ++/--, and bumps the entry's `updatedAt`.
- Creating a person: the editor's quick-create (`EditorViewModel.createAndAddPerson`,
  `createMentionPersonAndInsert`) creates a person with a name only; the Manage People screen creates
  and edits full records. Picking: `PeoplePickerDialog`, the `@mention` autocomplete, and
  `TrackerPeoplePickerDialog` for PEOPLE trackers.
- Mention spans in the body store offset and style only (`MENTION_PERSON`), no person id.
- Other references to people: `memories.peopleInvolved` (comma-separated local ids, no FK),
  `smart_reminders.relatedPersonId` (no FK), and `tracker_values.value` of PEOPLE trackers, which
  stores person **names**.
- Delete: `PersonRepo.delete` is a hard delete that cascades to `entry_people`.
- `JournalRepositoryImpl.PersonEntity.toDomain()` is a lossy projection (drops email, phone,
  birthday, importantDates); nothing saves it back (checked).

## 3. Timestamps, deletes, write paths

- Unit: epoch milliseconds (`Long`). Core entities use the system clock or the injected
  `java.time.Clock` (`di/ClockModule.kt`); AI/chat entities use `LocalDateTime`, stored by
  `Converters` as epoch millis in the system zone. The domain layer converts with
  `ZoneId.systemDefault()`.
- `updatedAt` exists on `journal_entries`, `reminder_configs`, `memories`, `mood_checkin_config`,
  `location_nicknames` and the AI/chat tables only. Tables without `createdAt`: `entry_tags`,
  `entry_people`, `export_history`, `notification_history`, `memory_entries` (has `addedAt`),
  `memory_milestones`, `memory_trackers`, `location_nicknames`.
- `journal_entries.updatedAt` is bumped by editor saves (value from the editor's clock), and, since
  Step 2, by favorite, archive, pin, soft delete, restore, and the three `set*ForEntry` methods.
  Media changes and changes to tags/categories/trackers/memories do not bump it.
- Autosave inserts a real non-draft row on the first autosave and updates it continuously.
- Entry deletes: soft delete sets `deletedAt` (the user-visible Trash). "Delete permanently" and
  "Empty trash" are **hard deletes** (cascade), now also deleting the media files and thumbnails
  after the commit; both ask for confirmation. `emptyOldTrash` exists but is never called, although
  the Trash screen says items are deleted after 30 days. `updateEntry` never changes `deletedAt`.
- People, tags, categories, media, trackers and memories are hard-deleted with cascade, no tombstones.
- `@Insert(onConflict = REPLACE)` is on almost every DAO; all current callers pass `id = 0`. See
  `docs/sync-notes.md` for the REPLACE/CASCADE findings and the writer-by-method table.
- `withTransaction` is used by `JournalRepositoryImpl` for entry updates, hard deletes and the
  `set*ForEntry` methods; no other repository uses it.

## 4. Rich text

- `journal_entries.content` holds a JSON array of `{start, end, style}` spans (`TextFormatting` in
  `presentation/screens/editor/TextFormatting.kt`): offsets and style names only, no text.
  Styles include bold, italic, underline, highlights, font variants, and chip styles
  (`TIMESTAMP`, `MENTION_PERSON/LOCATION/TAG/EMOTION`, `INLINE_IMAGE`). Inline images link to
  `media.inlineKey`.
- `journal_entries.plainContent` is the plain text; it always exists and is what search uses.
- `weatherData`, `locationData`, `additionalLocationsData` are JSON text columns.

## 5. Media

- Files live at `filesDir/{photo,video,voice,attachment}/<uuid>.<ext>`; `media.uri` stores the
  absolute path. `thumbnailUri` exists but thumbnails are never generated (`createThumbnail` is a
  TODO). There is no content hash. `sizeBytes` comes from the content resolver.
- `MediaRepositoryImpl.deleteMedia` and `deleteAllForEntry` delete the files and the rows. Hard
  deleting an entry deletes its media rows by cascade and, since Step 2, its files too.
- The editor's save diffs current against stored media: removed items are deleted (file and row),
  new items are copied into app storage and inserted.

## 6. Backup and restore (`data/repository/BackupRepositoryImpl.kt`)

- Two formats. `createBackup` writes a ZIP with `database/dairum.db` (+ `-wal`, `-shm`), `media/...`,
  `config/preferences/*.xml` (all `shared_prefs`, including the encrypted API-key file),
  `config/datastore/**` and `metadata.json`. `createCompleteExport` writes `entries/database.db`,
  `config/manifest.json`, `attachments/{photo,video,audio,files}/...` plus the same config files.
- Both restores are **whole-file swaps**: close the singleton database, delete the live `.db`,
  `-wal` and `-shm`, copy the backup file over. `restoreBackup` also deletes the media directories
  first; `importCompleteExport` copies attachments over the existing files. The `mergeData`
  parameter is ignored (the code says merge is not supported). Neither checks the schema version.
- A restore replaces every local id with the backup's ids, so it behaves like a time machine.
- Android auto-backup rules (`backup_rules.xml`, `data_extraction_rules.xml`) include the database
  and shared prefs, and name `file` path `media/`, which does not match the real directories
  (`photo/`, `video/`, `voice/`, `attachment/`); media is therefore not covered by auto-backup.
- Backups keep five internal copies and save to `Documents/Data Exports/Dairum` through MediaStore.

## 7. AI tables (derived or private, never sync)

`journal_embeddings`, `chat_embeddings` and `reflection_memories.embedding` are derived from text
and tied to a model. `weekly_reflections`, `pattern_results`, `smart_summaries` are LLM outputs
(derived, but costly to regenerate on-device). `ai_conversations`, `chat_sessions`, `chat_messages`,
`conversation_context` hold chat content. `ai_models` holds device file paths and download state.
Local counters that are derived and never sync: `tags.usageCount`, `people.mentionCount`, the
`memories` count and sum columns.

## 8. Sync / exclude

Status after the decisions in `docs/sync-notes.md`.

| Table | v1 status | Reason |
|---|---|---|
| `journal_entries` | Sync, phone-owned, metadata only | Core slice; body optional; location/weather columns excluded; titles behind a "Send titles" switch |
| `people` | Sync, Kith-owned (+ phone candidates as proposals) | Kith owns people |
| `entry_people` | Sync as a set on the entry | Phone decides who is in an entry |
| `media`, `people.photoUri` | Not in v1 | Files and paths excluded |
| `tracker_values` of PEOPLE trackers, `memories.peopleInvolved` | Not in v1 | They reference people by name / local id |
| `categories`, `tags`, `entry_tags`, `custom_trackers`, other `tracker_values`, `memories` and sub-tables, `location_nicknames`, gratitude and affirmation tables | Not decided for v1 | Later tier; `journal_entries.categoryId` is an unconstrained local id |
| `export_*`, `reminder_configs`, `smart_reminders`, `notification_history`, `mood_checkin_config` | Never | Per-device configuration (`smart_reminders` holds a local person id) |
| the 11 AI/chat tables | Never | Per the ownership constraints |

## 9. Migration risks (adding sync columns)

- Primary keys are local `Long` ids. They stay; a `syncId` column is added.
- `ALTER TABLE ... ADD COLUMN` plus an index needs no table rebuild. Avoid rebuilds: with CASCADE
  foreign keys, dropping and recreating a parent can wipe children.
- Room validates nullability and defaults against the entity, so a column added in SQL must match
  the entity declaration (declare `syncId` as `String?`).
- Full-row `@Update` and REPLACE overwrite columns; see `docs/sync-notes.md`.
- Migration tests start from the frozen `5.json`. The recommended checks: a `MigrationTestHelper`
  run 5 -> 6 validated against `6.json`, plus a full-class test that opens a scrubbed real database
  with `createFromFile` and asserts row counts, `foreign_key_check` and `integrity_check` (helper
  scripts are in `scripts/db/`; never commit a real or scrubbed database).
- The existing 1 -> 5 chain cannot be tested: the regenerated `1.json` already contains columns
  that migration 1 -> 2 adds.

## 10. Network setup

- `di/NetworkModule.kt`: one singleton OkHttp 4.12 client (connect, read, write timeouts 30 s)
  with an `HttpLoggingInterceptor` at `BODY` in debug builds and `NONE` in release; Retrofit 2.9.0
  with the Moshi 1.15 converter (`KotlinJsonAdapterFactory`; codegen runs through kapt). The only
  Retrofit service is `WeatherApi`, with a base URL from `BuildConfig.WEATHER_API_BASE_URL`.
- Manifest: `INTERNET` and `ACCESS_NETWORK_STATE`; no `networkSecurityConfig`; no
  `usesCleartextTraffic`, so cleartext is blocked (target SDK 34). There is no certificate pinning
  or custom trust manager. `allowBackup="true"`.
- `ApiKeyRepositoryImpl` stores API keys in `EncryptedSharedPreferences`
  (`security-crypto 1.1.0-alpha06`, AES256) and validates keys with `HttpURLConnection`.
- WorkManager 2.9.0 with `HiltWorkerFactory` through `DairumApplication` (the default initializer is
  removed in the manifest). Three unique periodic jobs set up in `NotificationRepositoryImpl`
  (`ReminderWorker` every 15 minutes, `SmartReminderWorker` daily, `MoodCheckInWorker`); their
  constraints are battery-not-low or none, and none requires the network.
- Planned transport (see `docs/sync-notes.md`): HTTPS only, over the LAN with a pinned self-signed
  certificate or over Tailscale.
