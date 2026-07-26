package sikv.lingomate.data.apikeystorage

/**
 * Securely stores and retrieves user-provided API keys.
 *
 * Keys are persisted through a platform-specific [SecureStorage] backend
 * (Android Keystore / iOS Keychain), so plaintext values never touch
 * unencrypted storage.
 *
 * Keys are addressed by [ApiKeyProvider], letting multiple providers' keys be
 * kept side by side. The underlying [ApiKeyProvider.storageKey] mapping stays
 * an implementation detail of this class.
 */
class ApiKeyStorage(
    private val secureStorage: SecureStorage,
) {

    /** Stores (or overwrites) the [apiKey] for the given [provider]. */
    fun store(provider: ApiKeyProvider, apiKey: String) {
        secureStorage.put(provider.storageKey, apiKey)
    }

    /** Returns the stored API key for [provider], or `null` if none is stored. */
    fun getApiKey(provider: ApiKeyProvider): String? {
        return secureStorage.get(provider.storageKey)
    }

    /** Removes the stored API key for [provider], if present. */
    fun remove(provider: ApiKeyProvider) {
        secureStorage.remove(provider.storageKey)
    }

    /** Removes all stored API keys. */
    fun clear() {
        secureStorage.clear()
    }
}
