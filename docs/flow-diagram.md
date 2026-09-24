# Flow Diagrams — BID.AI Android App

Mermaid diagrams for client-side flows. API endpoint details are out of scope (see your API/Postman docs).

---

## 1. App launch & session gate

```mermaid
flowchart TD
    A[App cold start] --> B[MainActivity.onCreate]
    B --> C[TokenStore initialized]
    B --> D[NetworkModule.init with token provider]
    C --> E{Token exists?}
    E -->|Yes| F[NavHost start = home]
    E -->|No| G[NavHost start = login]
    F --> H[HomeScreen]
    G --> I[LoginScreen]
```

---

## 2. Authentication (login / signup)

```mermaid
flowchart TD
    A[LoginScreen or SignupScreen] --> B[User submits form]
    B --> C[Client validation<br/>signup: required fields + password match]
    C -->|Invalid| D[Show field / form error]
    C -->|Valid| E[AuthViewModel.login or signup]
    E --> F[AuthRepository → POST api/auth/login or signup]
    F -->|Success| G[TokenStore.saveSession token + user]
    G --> H[uiState.success = true]
    H --> I[navigate home<br/>popUpTo login inclusive]
    F -->|HTTP error| J[Parse ApiError → uiState.error]
    F -->|Network / parse| K[Generic error message]
    J --> L[Snackbar shown → clearError]
    K --> L
    D --> B
```

**Cross-links:** Login → Signup (`navigate("signup")`), Signup → Login (`popBackStack`).

---

## 3. Sell listing flow (end-to-end)

```mermaid
flowchart TD
    A[Open Sell tab] --> B[SellViewModel.init]
    B --> C[GET api/categories]
    C -->|Success| D[Categories from API]
    C -->|Failure| E[Fallback static categories]
    D --> F[Select category]
    E --> F
    F --> G[Fill form: title, price, details, sell-as]
    G --> H[Pick photos Photo Picker]
    H --> I{2 ≤ photos ≤ 5?}
    I -->|No| J[Validation error snackbar]
    I -->|Yes| K[Tap Upload]
    K --> L{Client validation<br/>title, price, category, photos}
    L -->|Fail| J
    L -->|Pass| M[POST api/uploads multipart images]
    M -->|Fail| N[Error snackbar]
    M -->|Success urls| O[POST api/products with imageUrls]
    O -->|Fail| N
    O -->|Success| P[showSuccess = true]
    P --> Q[Congratulations dialog]
    Q --> R[Clear form / success state]
```

**Notes:**
- Category ids: API slug → UI model (`apiId` for create).
- `sell_as`: business → `vendor`, individual → `individual`.
- Concurrent submits blocked while `isSubmitting`.

---

## 4. Bottom navigation (tabs)

```mermaid
flowchart TD
    A[Bottom bar visible<br/>home · sell · one_click · chat · your_items] --> B[User taps tab]
    B --> C[navigate route<br/>popUpTo home saveState<br/>launchSingleTop · restoreState]
    C --> D{Route}
    D -->|home| E[HomeScreen]
    D -->|sell| F[SellScreen]
    D -->|one_click| G[Placeholder]
    D -->|chat| H[Placeholder]
    D -->|your_items| I[Placeholder]
    E --> J[Home content: top bar, search, banners,<br/>categories, products, plan, invite, …]
```

Bottom bar is hidden on `login` and `signup`.

---

## 5. Layer / call sequence (sell submit)

```mermaid
sequenceDiagram
    participant UI as SellScreen
    participant VM as SellViewModel
    participant Repo as SellRepository
    participant API as Backend API
    participant Store as TokenStore

    UI->>VM: submit(photos, fields)
    VM->>VM: validate title/price/photos/category
    VM->>Repo: uploadImages(uris)
    Repo->>Store: read session (via interceptor)
    Repo->>API: POST /api/uploads
    API-->>Repo: urls
    Repo-->>VM: Result.success(urls)
    VM->>Repo: CreateProductRequest(...)
    Repo->>API: POST /api/products
    API-->>Repo: product
    Repo-->>VM: Result.success
    VM-->>UI: showSuccess = true
    UI->>UI: Congratulations dialog
```

---

## Related docs

- [Tech stack](tech-stack.md) — versions, architecture, endpoints inventory
