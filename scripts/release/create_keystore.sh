#!/usr/bin/env bash
# Create the Dairum release keystore OUTSIDE the repo.
#
#   scripts/release/create_keystore.sh [path] [alias]
#   defaults: ~/.dairum-release/dairum-release.p12   alias: dairum
#
# keytool prompts for the passphrase itself, so it never appears on a command line, in shell
# history or in this repo. For a PKCS12 keystore the key password is the store password.
# Read docs/release-keystore.md before running this: losing this file means you can never update
# the app again without uninstalling it (which erases its data).
set -euo pipefail

out="${1:-$HOME/.dairum-release/dairum-release.p12}"
alias_name="${2:-dairum}"

keytool="${KEYTOOL:-}"
if [ -z "$keytool" ]; then
  for c in "${JAVA_HOME:-/nonexistent}/bin/keytool" /opt/homebrew/opt/openjdk@17/bin/keytool "$(command -v keytool || true)"; do
    [ -n "$c" ] && [ -x "$c" ] && { keytool="$c"; break; }
  done
fi
[ -x "$keytool" ] || { echo "keytool not found; set KEYTOOL=/path/to/keytool (any JDK)" >&2; exit 1; }

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd -P)"
mkdir -p "$(dirname "$out")"
abs_out="$(cd "$(dirname "$out")" && pwd -P)/$(basename "$out")"
case "$abs_out" in
  "$repo_root"/*) echo "Refusing: $abs_out is inside the repo ($repo_root). Pick a path outside it." >&2; exit 1 ;;
esac
[ ! -e "$abs_out" ] || { echo "Refusing to overwrite existing $abs_out" >&2; exit 1; }

chmod 700 "$(dirname "$abs_out")"
"$keytool" -genkeypair -v \
  -keystore "$abs_out" -storetype PKCS12 \
  -alias "$alias_name" \
  -keyalg RSA -keysize 4096 -validity 10000 \
  -dname "CN=Dairum release"
chmod 600 "$abs_out"

echo
echo "Created $abs_out"
echo "Certificate fingerprints (public; write the SHA-256 into your password manager entry):"
"$keytool" -list -v -keystore "$abs_out" -alias "$alias_name" | grep -E "SHA1:|SHA256:|Valid from"
echo
echo "Next: store the passphrase and a copy of the file as described in docs/release-keystore.md."
