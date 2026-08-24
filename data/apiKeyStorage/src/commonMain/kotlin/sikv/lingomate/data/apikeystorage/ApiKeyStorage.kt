package sikv.lingomate.data.apikeystorage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ApiKeyStorage(
    private val secureStorage: SecureStorage,
) {

    private val storedProviders = MutableStateFlow(readStoredProviders())

    fun store(provider: ApiKeyProvider, apiKey: String) {
        secureStorage.put(provider.storageKey, apiKey)
        refreshStoredProviders()
    }

    fun getApiKey(provider: ApiKeyProvider): String? {
        return secureStorage.get(provider.storageKey)
    }

    fun flowStoredProviders(): Flow<Set<ApiKeyProvider>> {
        return storedProviders.asStateFlow()
    }

    fun remove(provider: ApiKeyProvider) {
        secureStorage.remove(provider.storageKey)
        refreshStoredProviders()
    }

    fun clear() {
        secureStorage.clear()
        refreshStoredProviders()
    }

    private fun refreshStoredProviders() {
        storedProviders.value = readStoredProviders()
    }

    private fun readStoredProviders(): Set<ApiKeyProvider> {
        // TODO: Optimize.
        return ApiKeyProvider.entries
            .filter { secureStorage.get(it.storageKey) != null }
            .toSet()
    }
}
