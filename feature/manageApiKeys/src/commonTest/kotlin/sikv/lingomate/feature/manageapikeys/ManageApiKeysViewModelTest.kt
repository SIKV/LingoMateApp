package sikv.lingomate.feature.manageapikeys

import sikv.lingomate.analytics.Analytics
import sikv.lingomate.analytics.AnalyticsEvent
import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.data.apikeystorage.ApiKeyStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ManageApiKeysViewModelTest {

    private val trackedEvents = mutableListOf<AnalyticsEvent>()

    private fun viewModel(
        secureStorage: FakeSecureStorage = FakeSecureStorage(),
    ) = ManageApiKeysViewModel(ApiKeyStorage(secureStorage), Analytics(trackedEvents::add))

    @Test
    fun initialStateHasNoStoredProviders() {
        val viewModel = viewModel()

        assertTrue(viewModel.uiState.value.storedProviders.isEmpty())
    }

    @Test
    fun addApiKeyExposesTheProvider() {
        val viewModel = viewModel()

        viewModel.addApiKey(ApiKeyProvider.OpenAI, "sk-secret")

        assertEquals(listOf(ApiKeyProvider.OpenAI), viewModel.uiState.value.storedProviders)
    }

    @Test
    fun addApiKeyDoesNotLeakTheKeyValueIntoState() {
        val viewModel = viewModel()

        viewModel.addApiKey(ApiKeyProvider.OpenAI, "sk-secret")

        // State only carries provider identity, never the key itself.
        assertEquals(1, viewModel.uiState.value.storedProviders.size)
    }

    @Test
    fun addApiKeyTracksTheProvider() {
        val viewModel = viewModel()

        viewModel.addApiKey(ApiKeyProvider.OpenAI, "sk-secret")

        assertEquals(listOf<AnalyticsEvent>(AnalyticsEvent.ApiKeyAdded(provider = "OpenAI")), trackedEvents)
    }

    @Test
    fun removeApiKeyDropsTheProvider() {
        val viewModel = viewModel()
        viewModel.addApiKey(ApiKeyProvider.OpenAI, "sk-secret")

        viewModel.removeApiKey(ApiKeyProvider.OpenAI)

        assertFalse(viewModel.uiState.value.storedProviders.contains(ApiKeyProvider.OpenAI))
        assertTrue(viewModel.uiState.value.storedProviders.isEmpty())
    }

    @Test
    fun removeApiKeyTracksTheProvider() {
        val viewModel = viewModel()
        viewModel.addApiKey(ApiKeyProvider.OpenAI, "sk-secret")

        viewModel.removeApiKey(ApiKeyProvider.OpenAI)

        assertEquals(AnalyticsEvent.ApiKeyRemoved(provider = "OpenAI"), trackedEvents.last())
    }

    @Test
    fun existingKeysAreReflectedOnInit() {
        val secureStorage = FakeSecureStorage().apply {
            put(ApiKeyProvider.OpenAI.storageKey, "sk-secret")
        }

        val viewModel = viewModel(secureStorage)

        assertEquals(listOf(ApiKeyProvider.OpenAI), viewModel.uiState.value.storedProviders)
    }
}
