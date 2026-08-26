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