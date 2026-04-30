# Dairum App Review Report

## Scope

This review focused on source-level bugs, future production risks, logical inconsistencies, incomplete implementations, and product improvement opportunities in the Android app.

Reviewed areas:

- Data model, Room schema, and migrations
- Entry editor save/autosave flow
- Backup, restore, and export flows
- Notifications and reminders
- Media storage handling
- AI/embedding integration
- Stats and reporting logic
- Build and release readiness

## Validation Notes

- Static review was completed across the main Kotlin codebase.
- A local build attempt with `./gradlew assembleDebug --no-daemon` did not complete because Gradle configuration cache loading failed on a missing local cache artifact: `~/.gradle/caches/jars-9/.../gradle-8.2.2.jar`.
- That build failure appears environment/cache-related, not a confirmed application-code defect, so the report below prioritizes code-backed issues only.

## Executive Summary

The app has a solid feature surface and a generally sensible MVVM + Room + Hilt structure, but several core flows are not production-safe yet.

The most important problems are:

1. Database upgrade safety is currently unreliable.
2. Rich-text content persistence is inconsistent between save/autosave/load paths.
3. Backup/export media handling is logically broken because media is written to one directory tree and backed up from another.
4. Recurring smart reminders for birthdays/anniversaries will not work reliably after the original year.
5. Some user-visible screens and statistics are wired to incomplete or misleading logic.

## Confirmed Findings

### Critical

#### 1. Database migrations are out of sync with the live schema

Files:

- [app/src/main/java/com/dairum/data/local/database/Migrations.kt](app/src/main/java/com/dairum/data/local/database/Migrations.kt)
- [app/src/main/java/com/dairum/data/local/database/DairumDatabase.kt](app/src/main/java/com/dairum/data/local/database/DairumDatabase.kt)
- [app/src/main/java/com/dairum/data/local/entity/Entities.kt](app/src/main/java/com/dairum/data/local/entity/Entities.kt)

What is wrong:

- The database is at version `9`, but the migration list only includes `1->2`, `2->3`, `3->4`, `4->5`, `6->7`, and `8->9`.
- There is no `5->6` migration and no `7->8` migration.
- Several migration table names do not match the current entity table names.
  - Migration creates `export_config`, but the entity uses `export_configs`.
  - Migration creates `reminder_config`, but the entity uses `reminder_configs`.
  - Migration creates `smart_reminder`, but the entity uses `smart_reminders`.
  - Migration creates `memory`, `memory_entry_cross_ref`, and `memory_tracker`, while entities use `memories`, `memory_entries`, and `memory_trackers`.

Impact:

- Upgrading from older installed versions is not trustworthy.
- Existing users can hit schema mismatch crashes or silent data loss when migration fallback is triggered.
- This is the highest-risk production issue in the app.

Recommendation:

- Rebuild the migration chain so it matches the exact current Room schema.
- Add migration tests for every historical version that has ever shipped.
- Remove migration definitions that reflect obsolete table names unless they are intentionally transforming into the new schema in later steps.

#### 2. Rich text / formatting persistence is internally inconsistent

Files:

- [app/src/main/java/com/dairum/presentation/screens/editor/EditorViewModel.kt](app/src/main/java/com/dairum/presentation/screens/editor/EditorViewModel.kt)
- [app/src/main/java/com/dairum/data/repository/JournalRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/JournalRepositoryImpl.kt)

What is wrong:

- Manual save writes formatting via `formattingJson = state.textFormatting.toJson()`.
- Autosave does not write formatting JSON at all.
- Repository read mapping recreates entries with `content = RichContent()` instead of parsing stored JSON.

Impact:

- Autosaved entries can lose formatting information.
- Readback behavior is inconsistent because structured content is never reconstructed from storage.
- This creates a long-term risk of user-visible text corruption, especially if autosave becomes the main persistence path.

Recommendation:

- Make manual save and autosave use the exact same serialization path.
- Parse stored rich-text JSON back into the domain model on load.
- Add regression tests covering save, autosave, reload, and edit-after-reload.

#### 3. Backup/export media support is broken by conflicting storage paths

Files:

- [app/src/main/java/com/dairum/data/repository/MediaRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/MediaRepositoryImpl.kt)
- [app/src/main/java/com/dairum/data/repository/BackupRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/BackupRepositoryImpl.kt)
- [app/src/main/java/com/dairum/data/repository/ExportRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/ExportRepositoryImpl.kt)

What is wrong:

- `MediaRepositoryImpl.saveMediaFile()` stores files under per-type folders like `filesDir/photo`, `filesDir/video`, `filesDir/voice`, and `filesDir/attachment`.
- Both backup and export repositories look for media under `filesDir/media`.

Impact:

- `includeMedia = true` backups will miss the actual saved media files.
- Export flows that rely on the same directory convention can also omit user media.
- Restore may appear successful while silently losing attachments.

Recommendation:

- Centralize media storage path rules in one shared component.
- Backups should enumerate media from the database or from the same canonical storage root used by `MediaRepositoryImpl`.
- Add an integration test for create entry -> attach media -> backup -> restore.

#### 4. Yearly smart reminders are filtered incorrectly and can stop recurring

Files:

- [app/src/main/java/com/dairum/data/local/dao/NotificationDao.kt](app/src/main/java/com/dairum/data/local/dao/NotificationDao.kt)
- [app/src/main/java/com/dairum/data/service/SmartReminderWorker.kt](app/src/main/java/com/dairum/data/service/SmartReminderWorker.kt)

What is wrong:

- `NotificationDao.getUpcomingSmartReminders()` filters by raw stored `reminderDate` timestamps.
- `SmartReminderWorker.checkBirthdaysAndAnniversaries()` only adjusts recurring reminders to the current year after that query returns results.

Impact:

- A yearly birthday or anniversary stored in a past year can be excluded before recurrence logic is even applied.
- The app can miss recurring reminders after the original year.

Recommendation:

- Do recurrence expansion before filtering, or store recurring reminders in a recurrence-aware format.
- Add tests for yearly reminders across year boundaries.

### High

#### 5. Default backups are not discoverable in the app after creation

Files:

- [app/src/main/java/com/dairum/data/repository/BackupRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/BackupRepositoryImpl.kt)
- [app/src/main/java/com/dairum/presentation/screens/settings/BackupRestoreViewModel.kt](app/src/main/java/com/dairum/presentation/screens/settings/BackupRestoreViewModel.kt)

What is wrong:

- Default backup creation saves to `Documents/dairum` via MediaStore or public Documents.
- `getAvailableBackups()` only scans the internal backup directory under app files.

Impact:

- A user can create a backup successfully and still not see it in the in-app backup list.
- The UI state suggests backup management is centralized, but discovery only works for internal files.

Recommendation:

- Either keep managed backups in app-controlled storage and expose explicit export/share separately, or index both app storage and MediaStore results.

#### 6. `mergeData` is not really implemented during restore

Files:

- [app/src/main/java/com/dairum/data/repository/BackupRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/BackupRepositoryImpl.kt)

What is wrong:

- `restoreBackup(backupFile, mergeData)` always closes the current database, deletes the live DB files, and overwrites them with the extracted DB.
- `mergeData` only influences whether the media directory is deleted first.

Impact:

- The API and user-facing language imply merge support, but the database behavior is full replacement.
- This can surprise users and cause irreversible overwrite of current journal data.

Recommendation:

- Either remove the merge option until it exists, or implement true row-level merge semantics.

#### 7. Reminder window logic is wrong near the end of the hour

Files:

- [app/src/main/java/com/dairum/data/service/ReminderWorker.kt](app/src/main/java/com/dairum/data/service/ReminderWorker.kt)

What is wrong:

- Time matching is implemented as:
  - `currentHour == reminder.scheduleHour`
  - `currentMinute >= reminder.scheduleMinute`
  - `currentMinute < reminder.scheduleMinute + 15`
- For reminders late in the hour, the intended 15-minute window crosses into the next hour, but the code forbids that because `currentHour` must remain equal.

Impact:

- A reminder scheduled at `23:55` can be skipped if the worker runs at `00:00` or `00:05`.
- This is especially risky because WorkManager execution time is approximate, not exact.

Recommendation:

- Compare absolute timestamps or minutes-since-midnight with wraparound handling.

#### 8. Tag and person usage counters drift upward over time

Files:

- [app/src/main/java/com/dairum/data/repository/JournalRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/JournalRepositoryImpl.kt)
- [app/src/main/java/com/dairum/data/local/dao/TagDao.kt](app/src/main/java/com/dairum/data/local/dao/TagDao.kt)
- [app/src/main/java/com/dairum/data/local/dao/PersonDao.kt](app/src/main/java/com/dairum/data/local/dao/PersonDao.kt)

What is wrong:

- `setTagsForEntry()` deletes all current tag relationships and then increments usage for every newly inserted tag.
- `setPeopleForEntry()` does the same for mention counts.
- There is no balancing decrement for removed relationships during edits, even though decrement DAO methods exist.

Impact:

- Top tags and top people statistics become inflated over time.
- Editing an entry repeatedly can keep increasing counts even when the underlying number of linked entries does not grow.

Recommendation:

- Diff old vs new relationships and increment/decrement counts accordingly, or stop storing denormalized counters and compute usage from relations.

#### 9. Export-to-external-URI flow is broken on Android 10+

Files:

- [app/src/main/java/com/dairum/data/repository/ExportRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/ExportRepositoryImpl.kt)

What is wrong:

- `exportToTxt()` and `exportToPdf()` save through MediaStore and return a `content://` URI string on modern Android.
- `exportToExternalUri()` then treats that returned value as `File(internalPath)`.

Impact:

- Export chaining can fail because `File("content://...")` is not a real filesystem path.
- The code path is logically inconsistent across API levels.

Recommendation:

- Keep a true temp file path for internal post-processing, or stream directly from the temp file to the destination URI before MediaStore publication.

#### 10. Time-range filtering in stats is only partially implemented

Files:

- [app/src/main/java/com/dairum/presentation/screens/stats/StatsViewModel.kt](app/src/main/java/com/dairum/presentation/screens/stats/StatsViewModel.kt)

What is wrong:

- `loadStatsForRange()` suggests all stat groups respond to the selected range.
- `loadMoodStats()` and `loadActivityStats()` explicitly contain TODOs saying range filtering is not implemented.
- `loadTagStats()` and `loadPeopleStats()` still use all-time aggregates.

Impact:

- The UI can present filtered journal counts next to unfiltered mood/tag/people/activity stats.
- That creates misleading analytics and undermines user trust in the stats screen.

Recommendation:

- Either filter all stat groups consistently or label which sections are all-time only.

### Medium

#### 11. Stats screen can accumulate duplicate tracker collectors

Files:

- [app/src/main/java/com/dairum/presentation/screens/stats/StatsViewModel.kt](app/src/main/java/com/dairum/presentation/screens/stats/StatsViewModel.kt)

What is wrong:

- `loadTrackerStats()` calls `collect` on a long-lived Flow.
- It is invoked from `loadStats()`, `loadStatsForRange()`, and `loadStatsForCustomRange()`.

Impact:

- Each refresh can add another active collector.
- That increases redundant work and can lead to repeated state updates and harder-to-debug UI behavior.

Recommendation:

- Start the collector once, or convert the tracker stats flow into a shared derived state.

#### 12. Existing media is not loaded into the editor state

Files:

- [app/src/main/java/com/dairum/presentation/screens/editor/EditorViewModel.kt](app/src/main/java/com/dairum/presentation/screens/editor/EditorViewModel.kt)
- [app/src/main/java/com/dairum/presentation/screens/editor/EditorScreen.kt](app/src/main/java/com/dairum/presentation/screens/editor/EditorScreen.kt)

What is wrong:

- `loadEntry()` restores title, content, category, tags, people, trackers, weather, and location, but not media.
- The editor UI preview only renders `uiState.media`.

Impact:

- Existing attachments are effectively invisible while editing.
- Users cannot confidently review or remove prior attachments from the editor screen.

Recommendation:

- Load persisted media into editor state and distinguish existing media from newly added media.

#### 13. Favorite toggle is wired as a no-op in tag results

Files:

- [app/src/main/java/com/dairum/presentation/screens/tag/TagEntriesScreen.kt](app/src/main/java/com/dairum/presentation/screens/tag/TagEntriesScreen.kt)

What is wrong:

- The `EntryCard` favorite callback is still `/* TODO */`.

Impact:

- User interactions are inconsistent across screens.
- This is a visible unfinished behavior.

Recommendation:

- Wire the same favorite action used by other entry list surfaces.

#### 14. Debug/destructive migration fallback hides real migration problems

Files:

- [app/src/main/java/com/dairum/di/DatabaseModule.kt](app/src/main/java/com/dairum/di/DatabaseModule.kt)

What is wrong:

- Debug builds enable `fallbackToDestructiveMigration()`.

Impact:

- Migration defects are easier to miss because the database can be silently recreated instead of failing loudly during development.

Recommendation:

- Prefer explicit failure in debug while migration work is still active.

#### 15. Sensitive and noisy logging is too verbose in repository/editor paths

Files:

- [app/src/main/java/com/dairum/data/repository/JournalRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/JournalRepositoryImpl.kt)
- [app/src/main/java/com/dairum/presentation/screens/editor/EditorViewModel.kt](app/src/main/java/com/dairum/presentation/screens/editor/EditorViewModel.kt)

What is wrong:

- The app logs entry IDs, location payloads, autosave state, and other detailed runtime data through `android.util.Log`.

Impact:

- Journal metadata is privacy-sensitive.
- Excessive logs also make debugging harder by burying important events.

Recommendation:

- Route logs through a controlled logger and strip sensitive data in non-debug builds.

#### 16. API key strategy is split between runtime secure storage and compile-time placeholders

Files:

- [app/build.gradle.kts](app/build.gradle.kts)
- [app/src/main/AndroidManifest.xml](app/src/main/AndroidManifest.xml)
- [app/src/main/java/com/dairum/data/repository/WeatherRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/WeatherRepositoryImpl.kt)
- [app/src/main/java/com/dairum/data/repository/ApiKeyRepositoryImpl.kt](app/src/main/java/com/dairum/data/repository/ApiKeyRepositoryImpl.kt)

What is wrong:

- Weather and Maps keys exist as hardcoded placeholder values in build config / manifest, while the app also supports runtime secure storage for keys.

Impact:

- This invites configuration drift and encourages unsafe key injection into the APK/manifest.

Recommendation:

- Pick one approach.
- If keys are user-provided, remove build-time placeholders from production guidance.
- If keys are app-owned, use manifest placeholders and CI secrets rather than literals in source.

#### 17. Embedding quality will be limited by the placeholder tokenizer

Files:

- [app/src/main/java/com/dairum/data/ai/embedding/EmbeddingService.kt](app/src/main/java/com/dairum/data/ai/embedding/EmbeddingService.kt)

What is wrong:

- The embedding pipeline uses a simplified tokenizer with a small hardcoded common-word map and hash-derived token IDs.
- The file itself notes that a real tokenizer should be used in production.

Impact:

- Semantic search and AI similarity features may behave unpredictably, especially on names, multilingual text, punctuation-heavy notes, and rare words.

Recommendation:

- Use the model’s real tokenizer assets and preprocessing pipeline.

## Testing Gaps

No automated tests were found under:

- [app/src/test](app/src/test)
- [app/src/androidTest](app/src/androidTest)

High-priority missing tests:

1. Room migration tests for every historical version.
2. Save/autosave/reload tests for formatted entries.
3. Backup and restore integration tests including media.
4. Reminder scheduling tests around hour/day/year boundaries.
5. Stats filtering tests to ensure every section respects the same selected range.
6. Export tests for MediaStore URIs and document provider destinations.

## Suitable Improvements

1. Add a shared transaction boundary for entry save operations so entry, tags, people, trackers, and media updates succeed or fail together.
2. Replace denormalized tag/person counters with derived counts, or rigorously maintain them via relationship diffs.
3. Centralize file storage rules for backups, exports, and media attachments.
4. Separate feature-complete screens from work-in-progress screens so partial behavior is not exposed in release builds.
5. Remove placeholder TODO analytics from the stats UI until the backing queries exist.
6. Add explicit recovery UX after restore instead of only asking the user to restart.
7. Introduce structured privacy review for logs, exported data, and backup visibility.

## Suggestions For New Features

These are grounded in the current codebase and product direction rather than generic app ideas.

1. Entry revision history
   - The editor already tracks updates and autosaves, so a revision timeline would fit naturally and protect against accidental edits.

2. Backup health and restore preview
   - Show whether a backup contains media, schema version, entry count, and whether it is restorable on the current app version before restore begins.

3. Attachment integrity scanner
   - Because the app relies on local file paths, a maintenance screen could detect broken or missing files and offer repair/cleanup.

4. Accurate filtered insights
   - Turn the existing time-range selector into a real analytics feature with per-range moods, tags, people, and writing-pattern trends.

5. AI-assisted tag/person suggestions
   - Once embeddings are improved, suggest tags, people, and related memories while writing.

6. Reminder snooze and quiet hours
   - The notification system is already present; users would benefit from configurable snooze, missed-entry reminders, and do-not-disturb windows.

7. Backup destination management
   - Let users browse, verify, and restore backups from both app-managed and Documents/MediaStore locations from one place.

## Priority Order

Recommended implementation order:

1. Fix migration chain and add migration tests.
2. Unify rich-text persistence across autosave/manual save/load.
3. Fix media storage path consistency for backup/export/restore.
4. Fix recurring smart reminder query logic.
5. Correct misleading stats filters.
6. Repair backup discovery and true merge semantics.
7. Clean up tag/person counters and remaining TODO-driven UI behavior.

## Final Assessment

The app is feature-rich and directionally strong, but it is not yet robust in the areas that matter most for a journal product: data durability, trustworthy restore/backup behavior, and consistency of persisted user content.

If only a few items can be addressed first, focus on database migration correctness, content persistence correctness, and media-safe backup/restore. Those three areas have the highest risk of irreversible user harm.