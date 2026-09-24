# Tech Stack — BID.AI Android App

Client-only technical documentation for `com.samrudha.bidai` (module `:app`).

## Platform & build

| Component | Version / value |
|-----------|-----------------|
| Language | Kotlin **2.4.20** |
| Android Gradle Plugin | **9.4.1** |
| Gradle wrapper | **9.7.1** |
| JVM toolchain | **21** |
| Package / applicationId | `com.samrudha.bidai` |
| minSdk | **24** |
| compileSdk / targetSdk | **37** |
| versionName / versionCode | `1.0.0` / `1` |
| Modules | Single module `:app` |

**Build features:** Compose, BuildConfig.  
**Build types:** `debug` (`applicationIdSuffix = ".debug"`), `release` (minify + resource shrink).

**Permissions:** `INTERNET` only.

---

## UI

| Library | Version |
|---------|---------|
| Jetpack Compose BOM | **2026.09.00** |
| Material 3 | via Compose BOM |
| Activity Compose | 1.13.0 |
| Navigation Compose | 2.9.5 |
| Lifecycle (runtime / viewmodel-compose) | 2.11.0 |
| Core KTX | 1.19.0 |
| Coil (image loading) | 2.7.0 (`coil-compose`) |

**Theme:** light-only `lightColorScheme`, primary `#0668E1`, transparent status bar with light icons.  
**Navigation:** single `NavHost` in `MainActivity` with string routes (`login`, `signup`, `home`, `sell`, `one_click`, `chat`, `your_items`).

---

## Networking

| Library | Version |
|---------|---------|
| Retrofit | **2.11.0** |
| Retrofit kotlinx.serialization converter | 2.11.0 |
| OkHttp | **4.12.0** |
| kotlinx.serialization JSON | **1.9.0** |

**Base URL:** `https://tiling-mobilize-structure.ngrok-free.dev/`  
**Client setup (`NetworkModule`):** Bearer token interceptor (from `TokenStore`), 30s connect / 60s read / 60s write, JSON `ignoreUnknownKeys` + `coerceInputValues`.

**Endpoints consumed:**

| Method | Path | Used by |
|--------|------|---------|
| POST | `api/auth/signup` | Signup |
| POST | `api/auth/login` | Login |
| GET | `api/categories` | Sell category list |
| POST | `api/uploads` | Sell photo upload (multipart) |
| POST | `api/products` | Sell create listing |

---

## Architecture

```
MainActivity (NavHost + Scaffold + bottom bar)
    │
    ├── ViewModels (StateFlow<UiState> + viewModelFactory)
    │     AuthViewModel · HomeViewModel · SellViewModel
    │
    ├── Repositories (Result<T>)
    │     AuthRepository · SellRepository
    │
    ├── NetworkModule (singleton Retrofit / OkHttp / APIs)
    │
    └── TokenStore (SharedPreferences "auth")
```

| Concern | Approach |
|---------|----------|
| Pattern | MVVM — UI ← StateFlow ← ViewModel ← Repository ← Retrofit |
| DI | **Manual** (no Hilt/Koin); `NetworkModule` object + ViewModel factories |
| State | One `MutableStateFlow` per feature; screens use `collectAsStateWithLifecycle()` |
| Async | Kotlin coroutines; repo methods return `Result<T>` |
| Errors | Repo maps HTTP/IO/serialization → user message → snackbar via `uiState.error` |
| Local storage | **No Room** — session only: `TokenStore` (SharedPreferences: token, name, email) |
| Home feed data | Static `SampleHomeData` (swap-ready for products API; not wired yet) |
| Images | Coil `AsyncImage` (URL or drawable fallback) |

---

## Screens & navigation

| Route | Screen | Bottom bar |
|-------|--------|------------|
| `login` | LoginScreen | no |
| `signup` | SignupScreen | no |
| `home` | HomeScreen | yes |
| `sell` | SellScreen | yes |
| `one_click` | TabPlaceholderScreen | yes |
| `chat` | TabPlaceholderScreen | yes |
| `your_items` | TabPlaceholderScreen | yes |

**Start destination:** `home` if token exists, else `login`.

**Bottom nav tabs:** Home · Sell · One click sell · Chat · Your items.

---

## Feature status

| Feature | Status |
|---------|--------|
| Login / signup / session restore | Live API |
| Home feed (banners, categories, products, plan/invite, testimonials) | UI complete — static sample data |
| Sell (categories, 2–5 photos, upload, create product, congrats) | Live API with category fallback |
| Favorites toggle | In-memory only |
| Search / camera search / product detail / notifications | UI shell or no-op callbacks |
| One-click sell / Chat / Your items | Placeholder screens |
| Logout | Data-layer helper only (not in UI) |

---

## Tests & CI

**Tests:** JUnit 4, AndroidX JUnit, Espresso, Compose UI test (scaffold examples only).

**CI:** GitHub Actions and GitLab CI — lint, unit tests, debug/release builds; tag push (`v*`) for release artifacts.

---

## Related docs

- [Flow diagrams](flow-diagram.md) — app start, auth, sell, tab navigation
- [README](../README.md) — build commands and release secrets
