# Lamaa Studio (لمعة ستوديو)

Arabic design editor — local-first PWA + Android WebView wrapper.

## Docs (Arabic)
- [README_AR.md](README_AR.md) — overview & build instructions
- [ANDROID_BUILD_CLOUD_AR.md](ANDROID_BUILD_CLOUD_AR.md) — GitHub Actions signing & smoke test
- [PLAY_STORE_CHECKLIST_AR.md](PLAY_STORE_CHECKLIST_AR.md)
- [STORE_LISTING_AR.md](STORE_LISTING_AR.md)
- [TEST_REPORT_AR.md](TEST_REPORT_AR.md)
- [privacy.html](privacy.html)

## Structure
- Root: web/PWA assets (`index.html`, `styles.css`, `app.js`, `sw.js`, `manifest.webmanifest`)
- `android/`: Android Studio project (WebView, PNG export to Pictures/Lamaa, share)
- `scripts/`: static checks & smoke helpers
- `.github/workflows/android-release.yml`: build, sign, emulator smoke test

## Notes
- Change `applicationId` / `namespace` before Play Store release.
- Never commit keystore or passwords.
- Some large frontend files may be partial in this upload; restore full content from the original source archive if needed.

License: MIT — see `LICENSE.txt`.
