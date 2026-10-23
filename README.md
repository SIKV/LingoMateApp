<img width="148" height="148" src="https://github.com/user-attachments/assets/4ff309bf-8f92-48bb-b586-f8ad79440963" />

# LingoMate

![publish-remote-config](https://github.com/SIKV/LingoMateApp/actions/workflows/publish-remote-config.yml/badge.svg)

A cross-platform language-learning chat app built with Kotlin Multiplatform.
Business logic, networking, persistence, and state management are shared;
the UI is fully native on each platform (no Compose Multiplatform on iOS).

## Features

- Practice a language by chatting with a model, either as a **conversation**
  about real-life situations or as **translation** of sentences into the language you're learning.
- Pick the model, the language to practice, and the language the assistant explains in.
- End a chat to get a recap of the session.
- Send feedback from the app.

## Structure

Shared code is split into small modules rather than one monolithic
`shared` module.

- `shared` — umbrella KMP module, wires everything together via Koin DI.
- `api/*` — networking layer (Ktor + kotlinx.serialization).
- `data/*` — repositories, persistence, services.
- `feature/*` — shared feature logic (view models, state).
- `onDeviceLLM` — on-device LLM support (stubs for now).
- `analytics` — analytics interface each app implements with the PostHog SDK.
- `logger` — logging (Kermit).
- `composeApp` — Android app (Jetpack Compose).
- `iosApp` — iOS app (SwiftUI).

## Screenshots

<img width="250" src="https://github.com/user-attachments/assets/4177eafd-84f0-4b60-a0b5-8821d44e1cff" />

<img width="250" src="https://github.com/user-attachments/assets/e3786eb6-7826-4d04-8a67-8ea187f24ac3" />

<img width="250" src="https://github.com/user-attachments/assets/01ca7674-c793-46bb-8475-8150b8351b5e" />

## Remote config

The list of selectable chat models and languages is not hardcoded in the app. It lives in
[`resources/config/v1/remote_config.json`](resources/config/v1/remote_config.json), is
published to GitHub Pages by the `publish-remote-config` workflow on every push to `main`
that touches it, and is fetched at runtime. There is a bundled fallback for when the fetch
fails, so the app still starts offline.

## Adding your API key

The key is entered in the app, not at build time: open **Manage API keys** and paste it
in. It is stored with `EncryptedSharedPreferences` on Android and the `Keychain` on iOS.

## Crash reporting and analytics

Crash reports go to `Sentry` and usage analytics to `PostHog`.

## Privacy

See [PRIVACY.md](PRIVACY.md) for what the app collects and where it goes.
