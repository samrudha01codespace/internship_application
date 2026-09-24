# BID.AI

> Buy. Inspect. Deal. — Android marketplace app (Kotlin + Jetpack Compose).

## About

BID.AI is a mobile buy/sell marketplace for listings such as cars, real estate, mobiles, bikes, and more. The app covers login/signup with session restore, a home feed (banners, categories, products, plan/invite), and an end-to-end sell flow (category, photos, upload, create listing).

| | |
|---|---|
| Package | `com.samrudha.bidai` |
| minSdk / targetSdk | 24 / 37 |
| Version | 1.0.0 |
| Module | Single module `:app` |

## Tech Stack

| Component | Version |
|-----------|---------|
| Kotlin | 2.4.20 |
| Android Gradle Plugin | 9.4.1 |
| Gradle | 9.7.1 |
| Jetpack Compose | BOM 2026.09.00 |
| Material 3 | via Compose BOM |
| Navigation Compose | 2.9.5 |
| Lifecycle / ViewModel | 2.11.0 |
| Retrofit + kotlinx.serialization | 2.11.0 |
| OkHttp | 4.12.0 |
| Coil | 2.7.0 |
| Architecture | MVVM (StateFlow), manual DI |
| Local storage | SharedPreferences (session only) |

Full details: [docs/tech-stack.md](docs/tech-stack.md)

## Features

- **Auth** — login, signup, token session restore (start on Home if signed in)
- **Home** — top bar, rotating search hints, banners, category grid with photo icons, cars/bikes sections, recommendations, plan expiry + invite banners, testimonials
- **Sell** — API categories (static fallback), multi-photo (2–5), upload + create product, success dialog
- **Tabs** — Home, Sell, One click sell, Chat, Your items (last three placeholders)
- **Favorites** — in-memory toggle on product cards

Home product feed currently uses static sample data (ready to swap to products API).

## Project structure

```
app/src/main/kotlin/com/samrudha/bidai/
├── MainActivity.kt          # NavHost, Scaffold, bottom bar
├── data/                    # NetworkModule, repositories, TokenStore, DTOs
└── ui/                      # screens, ViewModels, components, theme, sample data
```

## Getting Started

```bash
# Clone
git clone <your-repo-url>
cd bidai

# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# APK
# app/build/outputs/apk/debug/app-debug.apk
```

Base API URL is set in `NetworkModule` (`app/src/main/kotlin/.../data/NetworkModule.kt`).

## Documentation

| Document | Description |
|----------|-------------|
| [docs/tech-stack.md](docs/tech-stack.md) | Tech stack, architecture, screens, feature status |
| [docs/flow-diagram.md](docs/flow-diagram.md) | App start, auth, sell, navigation, sequence flows (Mermaid) |

## Release

Push a tag to trigger a release build:

```bash
git tag v1.0.0
git push origin v1.0.0
```
