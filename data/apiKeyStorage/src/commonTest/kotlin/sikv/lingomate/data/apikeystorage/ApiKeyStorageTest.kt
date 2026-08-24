package sikv.lingomate.data.apikeystorage

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApiKeyStorageTest {

    private lateinit var secureStorage: FakeSecureStorage
    private lateinit var apiKeyStorage: ApiKeyStorage

    @BeforeTest
    fun setUp() {
        secureStorage = FakeSecureStorage()
        apiKeyStorage = ApiKeyStorage(secureStorage)
    }

    @Test
    fun store_persistsValueUnderProviderStorageKey() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        assertEquals("sk-123", secureStorage.entries[ApiKeyProvider.OpenAI.storageKey])
    }

    @Test
    fun getApiKey_returnsStoredValue() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        assertEquals("sk-123", apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI))
    }

    @Test
    fun getApiKey_returnsNullWhenMissing() {
        assertNull(apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI))
    }

    @Test
    fun store_overwritesExistingValue() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-old")
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-new")

        assertEquals("sk-new", apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI))
        assertEquals(1, secureStorage.entries.size)
    }

    @Test
    fun remove_deletesStoredKey() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        apiKeyStorage.remove(ApiKeyProvider.OpenAI)

        assertNull(apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI))
    }

    @Test
    fun remove_missingKeyDoesNothing() {
        apiKeyStorage.remove(ApiKeyProvider.OpenAI)

        assertNull(apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI))
        assertEquals(0, secureStorage.entries.size)
    }

    @Test
    fun clear_removesAllStoredKeys() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        apiKeyStorage.clear()

        assertNull(apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI))
        assertEquals(0, secureStorage.entries.size)
    }

    @Test
    fun flowStoredProviders_emitsEmptyListWhenNothingStored() = runTest {
        assertEquals(emptyList(), apiKeyStorage.flowStoredProviders().first())
    }

    @Test
    fun flowStoredProviders_emitsKeysPersistedBeforeConstruction() = runTest {
        secureStorage.put(ApiKeyProvider.OpenAI.storageKey, "sk-123")

        val storage = ApiKeyStorage(secureStorage)

        assertEquals(
            listOf(ApiKeyProvider.OpenAI),
            storage.flowStoredProviders().first()
        )
    }

    @Test
    fun flowStoredProviders_emitsAfterStore() = runTest {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        assertEquals(
            listOf(ApiKeyProvider.OpenAI),
            apiKeyStorage.flowStoredProviders().first()
        )
    }

    @Test
    fun flowStoredProviders_emitsAfterRemove() = runTest {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        apiKeyStorage.remove(ApiKeyProvider.OpenAI)

        assertEquals(emptyList(), apiKeyStorage.flowStoredProviders().first())
    }

    @Test
    fun flowStoredProviders_emitsAfterClear() = runTest {
        apiKeyStorage.store(ApiKeyProvider.OpenAI, "sk-123")

        apiKeyStorage.clear()

        assertEquals(emptyList(), apiKeyStorage.flowStoredProviders().first())
    }
}
