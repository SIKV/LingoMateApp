# LingoMate

### 🚧 Work in progress 🚧

A cross-platform language-learning chat app built with Kotlin Multiplatform.
Business logic, networking, persistence, and state management are shared;
the UI is fully native on each platform (no Compose Multiplatform on iOS).

## Structure

- `shared` — umbrella KMP module, wires everything together via Koin DI
- `api` — networking layer (Ktor + kotlinx.serialization)
- `data/*` — repositories, persistence, services
- `feature/*` — shared feature logic (view models, state)
- `onDeviceLLM` — on-device LLM support (stubs for now)
- `composeApp` — Android app (Jetpack Compose)
- `iosApp` — iOS app (SwiftUI, Xcode project)


## Screenshots

<img width="291" src="https://github.com/user-attachments/assets/5ffcb242-8ddd-4997-9454-d9ca12f561a6" />

<img width="300" src="https://github.com/user-attachments/assets/d95a3541-36be-4f5d-b2e6-1024f6e0214b" />
