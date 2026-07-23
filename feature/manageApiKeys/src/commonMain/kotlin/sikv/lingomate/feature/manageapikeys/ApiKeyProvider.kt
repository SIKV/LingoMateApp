package sikv.lingomate.feature.manageapikeys

import sikv.lingomate.data.apikeystorage.ApiKeyStorage
import kotlin.native.ObjCName

/**
 * A provider whose API key can be stored by the app.
 *
 * Each entry maps to the [storageKey] under which its key is persisted in
 * [ApiKeyStorage]. Only the provider identity is ever exposed to the UI - the
 * key value itself never leaves the secure storage layer.
 */
@ObjCName("ApiKeyProvider", exact = true)
enum class ApiKeyProvider(val storageKey: String) {
    OpenAI(ApiKeyStorage.Keys.OPEN_AI),
}
