package sikv.lingomate.feature.manageapikeys

import androidx.lifecycle.ViewModel
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sikv.lingomate.analytics.Analytics
import sikv.lingomate.analytics.AnalyticsEvent
import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.data.apikeystorage.ApiKeyStorage
import kotlin.native.ObjCName

/**
 * Manages the API keys the user has provided.
 *
 * Exposes which [ApiKeyProvider]s currently have a stored key and lets the user
 * add or remove a key for a given provider. Key values are written to and read
 * from [ApiKeyStorage] only - they are never held in [uiState].
 */
@ObjCName("ManageApiKeysViewModel", exact = true)
class ManageApiKeysViewModel(
    private val apiKeyStorage: ApiKeyStorage,
    private val analytics: Analytics,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageApiKeysState.empty())

    @NativeCoroutinesState
    val uiState: StateFlow<ManageApiKeysState> = _uiState.asStateFlow()

    init {
        refreshStoredProviders()
    }

    /** Stores (or overwrites) the [apiKey] for [provider]. */
    fun addApiKey(provider: ApiKeyProvider, apiKey: String) {
        apiKeyStorage.store(provider, apiKey)
        refreshStoredProviders()
        analytics.track(AnalyticsEvent.ApiKeyAdded(provider = provider.name))
    }

    /** Removes the stored key for [provider], if present. */
    fun removeApiKey(provider: ApiKeyProvider) {
        apiKeyStorage.remove(provider)
        refreshStoredProviders()
        analytics.track(AnalyticsEvent.ApiKeyRemoved(provider = provider.name))
    }

    private fun refreshStoredProviders() {
        val storedProviders = ApiKeyProvider.entries.filter { provider ->
            apiKeyStorage.getApiKey(provider) != null
        }
        _uiState.update { it.copy(storedProviders = storedProviders) }
    }
}
