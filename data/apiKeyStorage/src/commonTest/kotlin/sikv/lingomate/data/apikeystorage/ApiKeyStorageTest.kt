package sikv.lingomate.data.apikeystorage

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
    fun store_persistsValueInSecureStorage() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI.storageKey, "sk-123")

        assertEquals("sk-123", secureStorage.entries[ApiKeyProvider.OpenAI.storageKey])
    }

    @Test
    fun getApiKey_returnsStoredValue() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI.storageKey, "sk-123")

        assertEquals("sk-123", apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI.storageKey))
    }

    @Test
    fun getApiKey_returnsNullWhenMissing() {
        assertNull(apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI.storageKey))
    }

    @Test
    fun store_overwritesExistingValue() {
        apiKeyStorage.store(ApiKeyProvider.OpenAI.storageKey, "sk-old")
        apiKeyStorage.store(ApiKeyProvider.OpenAI.storageKey, "sk-new")

        assertEquals("sk-new", apiKeyStorage.getApiKey(ApiKeyProvider.OpenAI.storageKey))
        assertEquals(1, secureStorage.entries.size)
    }

    @Test
    fun differentKeys_areStoredIndependently() {
        apiKeyStorage.store("provider_a", "key-a")
        apiKeyStorage.store("provider_b", "key-b")

        assertEquals("key-a", apiKeyStorage.getApiKey("provider_a"))
        assertEquals("key-b", apiKeyStorage.getApiKey("provider_b"))
    }

    @Test
    fun remove_deletesOnlyTargetedKey() {
        apiKeyStorage.store("provider_a", "key-a")
        apiKeyStorage.store("provider_b", "key-b")

        apiKeyStorage.remove("provider_a")

        assertNull(apiKeyStorage.getApiKey("provider_a"))
        assertEquals("key-b", apiKeyStorage.getApiKey("provider_b"))
    }

    @Test
    fun remove_missingKeyDoesNothing() {
        apiKeyStorage.store("provider_a", "key-a")

        apiKeyStorage.remove("provider_b")

        assertEquals("key-a", apiKeyStorage.getApiKey("provider_a"))
    }

    @Test
    fun clear_removesAllStoredKeys() {
        apiKeyStorage.store("provider_a", "key-a")
        apiKeyStorage.store("provider_b", "key-b")

        apiKeyStorage.clear()

        assertNull(apiKeyStorage.getApiKey("provider_a"))
        assertNull(apiKeyStorage.getApiKey("provider_b"))
        assertEquals(0, secureStorage.entries.size)
    }
}
