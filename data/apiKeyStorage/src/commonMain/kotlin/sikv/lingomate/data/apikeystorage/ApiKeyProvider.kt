package sikv.lingomate.data.apikeystorage

import kotlin.native.ObjCName

/**
 * A provider whose API key can be stored by the app.
 *
 * This is the single source of truth for the set of supported providers. Each
 * entry carries the [storageKey] under which its key is persisted in
 * [ApiKeyStorage], keeping multiple providers' keys side by side.
 */
@ObjCName("ApiKeyProvider", exact = true)
enum class ApiKeyProvider(val storageKey: String) {
    OpenAI("open_ai"),
}
