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
- `5.json` (identity hash `1837df50fe8ecbfea4301570eff7b145`) is the frozen v5 baseline. `1.json` to
  `4.json` were regenerated from later entities and are unreliable; do not build tests on them.
- Changing entities means bumping the `@Database` version and adding a `Migration`, in the same
  commit as the new `N.json`. Never change entities under an existing version.
- Do not add `fallbackToDestructiveMigration`.
- Avoid `@Insert(onConflict = REPLACE)` for rows that have children: with `ON DELETE CASCADE` it
  deletes the parent row first and cascades.

## Sync project (in progress, in steps)

Dairum (phone) syncs with a self-hosted server app, Kith, over an HTTPS Tailscale URL.

- The phone owns diary content; Kith owns people. Phone-created people are proposals to Kith and
  quick-create must keep working offline.
- Last-write-wins with a server-assigned cursor. Tombstones, never hard delete of synced rows.
- Embeddings and all AI/chat data never sync. `locationData` and `weatherData` never sync.
  Diary body text is optional in the protocol (metadata-only first).
- Single write path via SQLite triggers (planned, not yet implemented).
