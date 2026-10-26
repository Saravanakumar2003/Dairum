# Release checklist (com.dairum)

**NEVER uninstall the app before updating it. Uninstalling erases the diary (database, photos,
settings). Always update in place with `adb install -r`.**

**NEVER lose the release keystore or its passphrase. Without it the installed app can never be
updated again, and the only way to install a differently signed build is to uninstall first (see
above). See `docs/release-keystore.md`.**

Also: make an in-app backup (Settings -> Backup & Restore -> Create backup) and copy the ZIP off the
phone before every update, and use the `dairum_test` emulator only (never `exersify_test`).

## 0. One-time setup

1. Create the keystore outside the repo and store it in two places: `scripts/release/create_keystore.sh`,
   then follow the storage checklist in `docs/release-keystore.md`.
2. Add the four signing properties to `~/.gradle/gradle.properties` (never to the repo):
   `DAIRUM_RELEASE_STORE_FILE`, `DAIRUM_RELEASE_STORE_PASSWORD`, `DAIRUM_RELEASE_KEY_ALIAS`,
   `DAIRUM_RELEASE_KEY_PASSWORD`. If any is missing, `assembleRelease` fails in seconds with a message
   naming the missing ones; it never produces an unsigned APK. A keystore inside the repo is refused.
3. JDK: the Gradle daemon JDK 17 is set in `~/.gradle/gradle.properties` (`org.gradle.java.home`);
   the `./gradlew` client needs any `JAVA_HOME` (see `CLAUDE.md`). The daemon heap is 4 GB
   (`gradle.properties`); R8 ran out of memory at 2 GB.
4. Write down the keystore certificate SHA-256 in your password manager. You compare it on every
   release (step 2.4).

## 1. Versioning

- `applicationId` is `com.dairum` for release and `com.dairum.debug` for debug (a separate app with
  its own data). Never change `applicationId`: a different id is a different app.
- `versionCode` (an integer in `app/build.gradle.kts`, currently 1) must increase with every release
  you install over another. Android refuses to install a lower one. Never reuse a number.
- `versionName` is shown to humans: `MAJOR.MINOR.PATCH`. Debug builds append `-debug`.
- Scheme: add 1 to `versionCode` per release (1, 2, 3, ...); bump PATCH for fixes, MINOR for
  features, MAJOR for breaking changes (for example a database change that cannot be undone).
  Change both in `app/build.gradle.kts` `defaultConfig`, in one commit named `Release X.Y.Z (vcN)`,
  and tag it `vX.Y.Z`.

## 2. Cut a release

1. Clean tree on `main`. Run the tests on `dairum_test`:
   `./gradlew :app:connectedDebugAndroidTest` (use `ANDROID_SERIAL=emulator-5556`).
2. Bump `versionCode`/`versionName`, commit, tag.
3. Build: `./gradlew :app:assembleRelease` -> `app/build/outputs/apk/release/app-release.apk`.
   The build runs lint (`lintVitalRelease`), R8 and resource shrinking.
4. Verify the signature and compare the fingerprint with your password manager:
   `<sdk>/build-tools/<ver>/apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk`
   (needs a JDK on `JAVA_HOME`). The SHA-256 must match. If it does not, stop.
5. Archive the APK and the R8 mapping **now** (the mapping is overwritten by the next build):
   `scripts/release/archive_release.sh` -> `~/dairum-releases/<versionName>-vc<versionCode>/`
   (APK, `mapping.txt`, `r8-configuration.txt`, `SHA256SUMS`, `signature.txt`, `git-commit.txt`).
   The script refuses to overwrite a version already archived. Copy that folder to your offline
   storage too.
6. Test on the emulator: `adb -s emulator-5556 install -r <apk>` (no uninstall), then section 3.
7. On the phone: back up in-app and copy the ZIP off, then `adb install -r <apk>` (or copy the APK
   and open it). It updates in place and keeps the data.
8. On the phone run the checks in section 3 that need real hardware.
9. Merge to `main`, push the tag.

## 3. Test list

Run on the emulator against the signed release, then compare with the debug build. Results of the
first run (2026-10-26, throwaway key, `dairum_test`, Android 15) are in the last column.

| # | Check | Expected | First run |
|---|---|---|---|
| 1 | First launch | Home with "No entries yet", no crash | pass |
| 2 | Create an entry with a photo, tag and person | All three shown on the entry and in the PDF/text export | pass |
| 3 | Favorite | Heart on the entry; survives restore | pass |
| 4 | Trash, then restore | Entry leaves Home, appears in Trash, returns with photo/tag/favorite | pass |
| 5 | Delete permanently | Confirmation dialog ("cannot be undone"); Cancel keeps it; Delete removes it | pass |
| 6 | Create backup | ZIP in `Documents/Data Exports/Dairum`; `metadata.json` has real keys (`appVersion`, ...) | pass after the keep rules (was `{"a":..}`) |
| 7 | Restore backup | "Restart required"; after restart the data matches the backup | pass |
| 8 | Export to PDF and to text | Files created; PDF shows title, photo, tag, person, category | pass |
| 9 | App lock | Set PIN; wrong PIN rejected; correct PIN unlocks; cold start asks for the PIN | pass |
| 10 | Reminders screen | Opens; with the permission granted, "Test" posts a notification | pass |
| 11 | On This Day | Story screen opens (needs an entry from a previous year to show content) | pass (empty state only) |
| 12 | Statistics | All four tabs render | pass |
| 13 | AI model import | `.gguf` imports and selects; the native library is reached | pass with a synthetic file; real model and chat turn NOT tested (no model available) |
| 14 | Update in place | `adb install -r` of a newer build keeps entries, photo and settings | pass (twice) |
| 15 | Native and ONNX load | A bad `.gguf`/`.onnx` gives an error message, never a crash | pass after the ONNX keep rule |

Needs a real phone (not covered by the emulator): biometric unlock, real GPS location and weather,
voice recording and playback, video attach and playback (Media3), sharing/export to other apps,
widgets on the home screen, notification timing (reminders at their scheduled time), battery/doze
behaviour, and a real LLM/embedding model on arm64 hardware.

## 4. R8 and the mapping file

- Release builds minify and shrink resources. Rules are in `app/proguard-rules.pro`; the last block
  ("Release-build fixes") holds the rules that running the release proved necessary, each with its
  reason.
- Keep the mapping: `scripts/release/archive_release.sh` copies it with the APK. The raw file is at
  `app/build/outputs/mapping/release/mapping.txt` until the next build overwrites it.
- Retrace a crash trace (use the mapping of the exact APK that crashed):
  `adb logcat -d -b crash | scripts/release/retrace.sh - ~/dairum-releases/1.0.0-vc1`, or
  `scripts/release/retrace.sh trace.txt <archive-dir-or-mapping.txt>`. It runs the SDK's
  `cmdline-tools/latest/bin/retrace`.
- After adding a library that uses reflection, JNI, or `Class.forName`, run the release build and
  exercise that code. Debug builds do not run R8, so they hide these failures.

## 5. Known issues found while preparing the first release

- **Encrypted preferences crash the editor after a restore or reinstall.** If
  `shared_prefs/encrypted_api_keys.xml` exists but its Android Keystore key does not (restored
  backup, reinstall, new phone), `ApiKeyRepositoryImpl` throws `AEADBadTagException` when it is
  created, and opening New Entry crashes. Reproduced by simulation on the debug build. The in-app
  backup ZIP contains that file, and so do the auto-backup rules. A fix is queued as a separate task;
  until it ships, delete the file (or clear the app's API keys) before restoring onto another
  install, and do not restore a ZIP onto a freshly reinstalled app without testing it first.
- **Auto-backup rules are wrong.** `backup_rules.xml` and `data_extraction_rules.xml` include `media/`,
  which does not exist (media lives in `photo/`, `video/`, `voice/`, `attachment/`) and exclude
  `cache/`, which lint reports as an error (made informational for now in `app/build.gradle.kts`).
  See the full list below.
- Dead dependencies with keep rules: iText, Vico and `maps-compose` are declared but never imported;
  their keep rules and `-keep class androidx.compose.** { *; }` etc. stop R8 from shrinking. Release
  APK is about 52 MB and the mapping 240 MB.
- Not exercised at runtime: the Retrofit weather call (needs a location fix), Media3 playback.

### What Android auto-backup and device transfer copy (release)

`allowBackup="true"`. API 31+ uses `data_extraction_rules.xml`; API 26-30 uses `backup_rules.xml`.

| Data | Cloud backup / device transfer |
|---|---|
| `databases/dairum.db` | Copied (only the main file; `-wal`/`-shm` are not named, so writes not yet checkpointed may be missing) |
| `shared_prefs/*` (all, `path="."`) | Copied, including the encrypted API-key file and widget prefs |
| `files/media/` | Nothing: the directory does not exist |
| `files/photo`, `video`, `voice`, `attachment` | Not copied: every photo/video/voice note is lost |
| `files/datastore/*` (settings, **app lock PIN hash**, guardrails, templates) | Not copied: settings reset and the app lock is off after a restore |
| `files/models/*` (AI models), `files/exports`, `files/backups` | Not copied |

After a restore the entries come back, media rows point at files that no longer exist, the app lock
is disabled, and the restored encrypted preferences cannot be decrypted without their Keystore key
(see the crash above).

## 6. If the key is lost

See `docs/release-keystore.md`: back up in-app, uninstall, install a build signed with a new key,
restore the backup, re-enter API keys.
