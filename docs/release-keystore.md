# Release keystore

**If you lose this keystore (or its passphrase) you can never update the installed app. The only
way to install a differently signed build is to uninstall first, which erases all app data.**

## What the key does

Android only installs an update over an existing app if the update is signed with the same
certificate. The release build of `com.dairum` is signed with the key in this keystore. The debug
build (`com.dairum.debug`) is a different app and does not use it.

If the key is lost:

- You cannot ship an update to the phone that has the release app. `adb install -r` of a build
  signed with a new key fails with `INSTALL_FAILED_UPDATE_INCOMPATIBLE`.
- The only way forward is to uninstall the app, which deletes its private data (the Room database,
  photos, voice notes, settings). Before that, create an in-app backup (Settings -> Backup &
  Restore -> Create backup) and copy the ZIP off the phone. After installing the new build, restore
  it. API keys stored in encrypted preferences cannot be restored on a fresh install (they are
  bound to the old install's Android Keystore key), so you would re-enter them.
- Nothing about this is recoverable from Google or Anthropic: the app is not on a store.

Treat the keystore like the diary itself.

## Create it (once)

Do this yourself in a terminal. The passphrase is typed at keytool's prompt; I never see it and it
must never be committed, pasted into chat, or written in a script.

```bash
scripts/release/create_keystore.sh
```

That runs, with the output path outside the repo (the script refuses a path inside it):

```bash
# ~/.dairum-release is created with mode 700, then:
keytool -genkeypair -v \
  -keystore ~/.dairum-release/dairum-release.p12 -storetype PKCS12 \
  -alias dairum \
  -keyalg RSA -keysize 4096 -validity 10000 \
  -dname "CN=Dairum release"
```

- RSA 4096 works with every Android version this app supports (minSdk 26). `-validity 10000` is
  about 27 years.
- For a PKCS12 keystore the key password equals the store password; use one strong passphrase
  (a generated one from your password manager).
- An EC key is also fine on minSdk 26 (`-keyalg EC -groupname secp384r1`), but RSA 4096 is the
  conservative choice.

Record the SHA-256 fingerprint the script prints. It is public. You will compare it with
`apksigner verify --print-certs` on every release to prove the right key signed the build.

## Storage checklist

Both must exist before you ship a release you care about. Tell me when each is done.

- [ ] **Password manager entry** containing: the keystore passphrase, the alias (`dairum`), the
      file path, the SHA-256 fingerprint, and the keystore file itself as an attachment (if your
      password manager supports attachments).
- [ ] **Offline copy** of `dairum-release.p12` on media that is not your Mac and not synced to the
      same account as your password manager (for example an encrypted USB drive kept elsewhere).
      Write down where it is.
- [ ] **Restore test:** copy the file somewhere temporary and run
      `keytool -list -keystore <copy> -alias dairum` with the stored passphrase. Delete the copy.
- [ ] The file is **not** in the repository, in iCloud/Dropbox, or in a Time Machine-only location.

## Gradle configuration

Release builds read four properties, looked up in this order: `~/.gradle/gradle.properties` first, then
environment variables with the same names (handy for a one-off shell or CI). Never from the repo.

```
DAIRUM_RELEASE_STORE_FILE=/Users/<you>/.dairum-release/dairum-release.p12
DAIRUM_RELEASE_STORE_PASSWORD=<passphrase>
DAIRUM_RELEASE_KEY_ALIAS=dairum
DAIRUM_RELEASE_KEY_PASSWORD=<passphrase>
```

Keep `~/.gradle/gradle.properties` readable only by you (`chmod 600`). It already holds your JDK path.
If any value is missing, the file does not exist, or it lives inside the repo, `assembleRelease` fails
within seconds with a message naming the problem and never produces an unsigned APK. See
`docs/release-checklist.md` for cutting a release.
