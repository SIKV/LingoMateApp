package sikv.lingomate.data.apikeystorage

/**
 * Securely stores and retrieves user-provided API keys.
 *
 * Keys are persisted through a platform-specific [SecureStorage] backend
 * (Android Keystore / iOS Keychain), so plaintext values never touch
 * unencrypted storage.
 *
 * A [key] identifies which API key is being stored, allowing multiple
 * providers to be kept side by side (e.g. [Keys.OPEN_AI]).
 */
class ApiKeyStorage(
    private val secureStorage: SecureStorage,
) {

    /** Stores (or overwrites) the [apiKey] for the given [key]. */
    fun store(key: String, apiKey: String) {
        secureStorage.put(key, apiKey)
    }

    /** Returns the stored API key for [key], or `null` if none is stored. */
    fun getApiKey(key: String): String? {
        return secureStorage.get(key)
    }

    /** Removes the stored API key for [key], if present. */
    fun remove(key: String) {
        secureStorage.remove(key)
    }

    /** Removes all stored API keys. */
    fun clear() {
        secureStorage.clear()
    }

    /** Well-known keys for the providers the app supports. */
    object Keys {
        const val OPEN_AI = "open_ai"
    }
}
