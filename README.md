# bidai

> A modern Android application built with Kotlin + Jetpack Compose.

[![Android CI/CD](https://github.com/MaheshTechnicals/android-project-generator/actions/workflows/android-ci.yml/badge.svg)](https://github.com/MaheshTechnicals/android-project-generator/actions)

## Tech Stack

| Component             | Version            |
|-----------------------|--------------------|
| Kotlin                | 2.4.20  |
| Android Gradle Plugin | 9.4.1     |
| Gradle                | 9.7.1  |
| Target SDK            | 37      |
| Min SDK               | 24         |
| Jetpack Compose       | BOM 2026.09.00 |

## Getting Started

```bash
# Clone
git clone <your-repo-url>
cd bidai

# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# APK location
# app/build/outputs/apk/debug/app-debug.apk
```

## CI/CD

- **GitHub Actions** — lint, unit tests, debug/release builds on push. Release APK + AAB uploaded on tag push (`v*`).
- **GitLab CI** — same pipeline with integrated GitLab Releases.

## Release

Push a tag to trigger a release build:

```bash
git tag v1.0.0
git push origin v1.0.0
```

### Required Secrets (for signed release builds)

You can use either the legacy or modern naming — the build checks both (modern takes precedence).

| Secret                    | Description                          |
|---------------------------|--------------------------------------|
| `ANDROID_SIGNING_KEY`     | Base64-encoded keystore file         |
| `ANDROID_KEYSTORE_PASSWORD` | Keystore password                 |
| `ANDROID_ALIAS`           | Key alias                            |
| `ANDROID_KEY_PASSWORD`    | Key password                         |
| `KEYSTORE_BASE64`         | *(legacy)* Base64-encoded keystore   |
| `KEYSTORE_PASSWORD`       | *(legacy)* Keystore password         |
| `KEY_ALIAS`               | *(legacy)* Key alias                 |
| `KEY_PASSWORD`            | *(legacy)* Key password              |

> **Note:** The  variables take priority over the legacy  /  variables. If neither is set, the release build will be unsigned (uses debug keystore as fallback).

## Package

`com.samrudha.bidai`

---
*Scaffolded with [Android Project Generator](https://github.com/MaheshTechnicals/android-project-generator) by Mahesh Technicals*
