# Dairum

Android journaling app (Kotlin, Compose, Room, Hilt). Package `com.dairum`; the debug build is
`com.dairum.debug` and has its own database.

## Build and test

- JDK 17 is required. The Gradle daemon JDK is pinned per machine in `~/.gradle/gradle.properties`
  (`org.gradle.java.home=<path to a JDK 17>`), not in the repo. On this Mac:
  `/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home`. The `./gradlew` client JVM can be
  any JDK (Android Studio's bundled Java 25 works), but Gradle 8.5 and Kotlin 1.9 cannot run the
  build itself on Java 25.
- Compile instrumented tests: `./gradlew :app:compileDebugAndroidTestKotlin`
- Run them on a device or emulator: `./gradlew :app:connectedDebugAndroidTest`
  (emulator `dairum_test`, Pixel 7, Android 15; set `ANDROID_SERIAL` if several devices are attached).
- Database helpers live in `scripts/db/` (extract, inspect, scrub). Never commit a real or scrubbed
  database; fixtures under `app/src/androidTest/assets/fixtures/` are gitignored.

## Room schema rules

- **Schema JSONs are history, not source of truth.** Room rewrites `app/schemas/.../<version>.json`
  on every build, so a file only describes the entities at the last build of that version. Once a
  version has shipped (or been used for real data) its JSON is frozen: never edit or regenerate it by
  hand, and review any diff to it as a red flag.
- `5.json` (identity hash `1837df50fe8ecbfea4301570eff7b145`) is the only committed schema JSON, the
  frozen v5 baseline. `1.json` to `4.json` were regenerated from later entities, so they were
  deleted; do not recreate them or build tests on them.
- From the next schema change on (5->6), `6.json` is committed next to `5.json` (and so on for each
  new version). Once a version has shipped or held real data, its JSON is frozen: never commit a
  regenerated JSON for a released version. If a build rewrites one, `git checkout` it.
- Changing entities means bumping the `@Database` version and adding a `Migration`, in the same
  commit as the new `N.json`. Never change entities under an existing version.
- Do not add `fallbackToDestructiveMigration`.
- Avoid `@Insert(onConflict = REPLACE)` for rows that have children: with `ON DELETE CASCADE` it
  deletes the parent row first and cascades.

## Write-path conventions

- Repository writes that touch several rows (a parent plus its links) run in one
  `database.withTransaction { }`, so a failure leaves the old data. `setTagsForEntry`,
  `setPeopleForEntry` and `setTrackerValues` also bump the parent entry's `updatedAt` in the same
  transaction.
- Take time from the injected `java.time.Clock` (`di/ClockModule.kt`), not `System.currentTimeMillis()`,
  in code that stamps `updatedAt`/`deletedAt`/`createdAt`, so tests can freeze it.
- Anything that changes an entry (favorite, archive, pin, soft delete, restore) bumps
  `journal_entries.updatedAt`. `updateEntry` never changes `deletedAt`; only `softDelete` and
  `restoreEntry` do. A person's `createdAt` never changes on update.
- Deleting an entry for good (`deleteEntry`, `emptyTrash`) also deletes its media files, after the
  database commit.
- Tests for these use in-memory Room (`InMemoryDatabaseRule`) and `TestClock` under `app/src/androidTest`.

## Releases

- Read `docs/release-checklist.md` before building or installing a release, and
  `docs/release-keystore.md` for the signing key.
- Never uninstall the app before updating it (that erases the diary); update with `adb install -r`.
- The release keystore and its passphrase live outside the repo and must never be committed.
- Release builds run R8: after adding a library that uses reflection or JNI, run the release build and
  exercise that code; keep rules live in `app/proguard-rules.pro` with a reason next to each.
