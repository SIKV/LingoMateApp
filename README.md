<img width="148" height="148" src="https://github.com/user-attachments/assets/4ff309bf-8f92-48bb-b586-f8ad79440963" />

# LingoMate

### 🚧 Work in progress 🚧

A cross-platform language-learning chat app built with Kotlin Multiplatform.
Business logic, networking, persistence, and state management are shared;
the UI is fully native on each platform (no Compose Multiplatform on iOS).

## Structure

Shared code is split into small modules rather than one monolithic
`shared` module.

- `shared` — umbrella KMP module, wires everything together via Koin DI
- `api/*` — networking layer (Ktor + kotlinx.serialization)
- `data/*` — repositories, persistence, services
- `feature/*` — shared feature logic (view models, state)
- `onDeviceLLM` — on-device LLM support (stubs for now)
- `logger` — logging (Kermit)  
- `composeApp` — Android app (Jetpack Compose)
- `iosApp` — iOS app (SwiftUI, Xcode project)


## Screenshots

#### Android
<img width="250" src="https://github.com/user-attachments/assets/b7a4cb4c-be18-4fca-a643-0072ecb69696" />
<img width="250" src="https://github.com/user-attachments/assets/59a7349a-5312-4e19-a21c-3a7c554aa87f" />

#### iOS
<img width="250" src="https://github.com/user-attachments/assets/6a2ad3a5-6359-463b-a1fc-b82e059327de" />
<img width="250" src="https://github.com/user-attachments/assets/11cd875a-18c2-42a5-9417-004a63e15c8a" />

## Remote config

The list of selectable chat models and languages is not hardcoded in the app. It lives in
[`resources/config/v1/remote_config.json`](resources/config/v1/remote_config.json), is
published to GitHub Pages by the `publish-remote-config` workflow on every push to `main`
that touches it, and is fetched at runtime. There is a bundled fallback for when the fetch
fails, so the app still starts offline.

## Adding your API key

The key is entered in the app, not at build time: open **Manage API keys** and paste it
in. It is stored with `EncryptedSharedPreferences` on Android and the `Keychain` on iOS.
