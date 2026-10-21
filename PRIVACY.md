# LingoMate Privacy Policy

_Last updated: October 21, 2026_

LingoMate is a language-practice chat app for iOS and Android. This policy explains what data
the app handles, where it goes, and what you can do about it.

The short version: there are no accounts, no ads, and no tracking across other apps or websites,
and your data is never sold. Your chats go directly from your device to OpenAI using your own API
key. We receive anonymous usage statistics, crash reports, and any feedback you choose to send.

## Your chats

When you chat, your messages and the conversation so far are sent directly from your device to
OpenAI using the API key you entered. They never pass through our servers, and we can't see them.
The app asks OpenAI not to store the responses (`store: false`). OpenAI processes this data under
its own [privacy policy](https://openai.com/policies/privacy-policy) and API terms.

The app keeps a chat only in memory while it's open. Chats aren't saved on your device, and they're
gone when you leave the chat.

## Your API key

Your OpenAI API key is stored only on your device: in the Keychain on iOS and in encrypted storage
on Android. It's sent only to OpenAI, with each chat request, and backups or device transfers
don't carry it to another device. You can remove it at any time in **Manage API keys**.

On iOS, the Keychain may keep the key after the app is deleted, so remove it in the app first if
you want it gone from your device.

## Data stored on your device

Besides your API key, the app stores your chosen model, languages and practice type, and a copy
of the list of available models and languages. Uninstalling the app deletes this data.

## Usage analytics

We use [PostHog](https://posthog.com/privacy) (servers in the United States) to understand how the
app is used. Analytics use a random identifier created on your device. It isn't linked to your
name, email address, or any account. We record:

- when a chat starts, with the chosen model, languages and practice type
- when a message is sent (never its content)
- when a chat recap is opened, with the number of messages in the chat
- when an API key is added or removed, with the provider name (never the key)
- when the app is installed, updated, opened or sent to the background

Each event also includes basic device details: device model, operating system and version, app
version, language, time zone, and screen size.

## Crash reports

We use [Sentry](https://sentry.io/privacy/) to receive reports when the app crashes or hits an
error. A report contains technical details such as the error and stack trace, device model,
operating system and version, and app version. Crash reports don't include your chats or your
API key.

## Feedback

If you send feedback from the app, your message and the email address you enter (optional) are
sent to Sentry, together with the same device details as a crash report. We use your email
address only to reply to your feedback.

## Other network requests

The app downloads the list of available models and languages from GitHub Pages.
This request doesn't include any of your data. GitHub handles it under its
[privacy statement](https://docs.github.com/site-policy/privacy-policies/github-general-privacy-statement).

Like any web service, OpenAI, PostHog, Sentry and GitHub receive your device's IP address when the
app connects to them.

## Your choices

- Remove your API key at any time in **Manage API keys**.
- We don't run our own servers, but PostHog and Sentry keep the analytics, crash reports and
  feedback described above for us. To ask for this data to be deleted, contact us at the address
  below. Analytics and crash reports are anonymous, so we may not be able to find the ones that
  came from you. Feedback that included your email address can be found and deleted.
- The app doesn't currently offer a setting to turn off analytics.

## Children

LingoMate isn't intended for children under 13.

## Changes

If this policy changes, the new version will be published here with an updated date. The full
history of changes is visible in this repository.

## Contact

Questions or requests: [sikv.dev@gmail.com](mailto:sikv.dev@gmail.com)
