## Project Overview

A cross-platform language-learning chat app built with Kotlin Multiplatform.
Business logic, networking, persistence, and state management are shared in a common
module (shared). UI is fully native per platform - this project does NOT use Compose
Multiplatform for iOS.

- Android UI: Jetpack Compose.
- iOS UI: SwiftUI.

## Project Structure
It's a Kotlin Multiplatform project split into several feature/data modules rather than one monolithic shared module:

- shared: the umbrella/root KMP module; wires everything together via Koin DI (AppModule, ViewModelsModule) rather than holding business logic itself.
- api: networking layer.
- data/*: data layer (persistence/repositories/services).
- feature/*: feature's shared logic (view models, state) consumed by both native UIs.
- onDeviceLLM - module for on-device LLM support (currently stub implementations).
- composeApp: Android app, Jetpack Compose UI.
- iosApp: Xcode project, SwiftUI UI.

Key rule: no platform-specific types leak into commonMain public APIs.
Use DI (preferred) or expect/actual declarations for platform-specific implementations.

## Tech Stack
- Kotlin Multiplatform + Kotlin Coroutines / Flow for async and state.
- Ktor client for networking (shared).
- kotlinx.serialization for JSON.
- Koin for dependency injection.
- Jetpack Compose (Android only).
- SwiftUI (iOS only).

## State Sharing Strategy
Shared view models expose state as StateFlow/Flow from commonMain.

- Android: consume StateFlow directly in Compose.
- iOS: Kotlin Flow isn't natively supported. Use: KMP-NativeCoroutines to generate Swift-async-friendly APIs.

## Build & Run Commands
- Build the shared module: ./gradlew :shared:build
- Build Android app: ./gradlew :composeApp:assembleDebug
- Run shared module tests: ./gradlew :shared:allTests
