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
        apiKeyStorage.store(ApiKeyStorage.Keys.OPEN_AI, "sk-123")

        assertEquals("sk-123", secureStorage.entries[ApiKeyStorage.Keys.OPEN_AI])
    }

    @Test
    fun getApiKey_returnsStoredValue() {
        apiKeyStorage.store(ApiKeyStorage.Keys.OPEN_AI, "sk-123")

        assertEquals("sk-123", apiKeyStorage.getApiKey(ApiKeyStorage.Keys.OPEN_AI))
    }

    @Test
    fun getApiKey_returnsNullWhenMissing() {
        assertNull(apiKeyStorage.getApiKey(ApiKeyStorage.Keys.OPEN_AI))
    }

    @Test
    fun store_overwritesExistingValue() {
        apiKeyStorage.store(ApiKeyStorage.Keys.OPEN_AI, "sk-old")
        apiKeyStorage.store(ApiKeyStorage.Keys.OPEN_AI, "sk-new")

        assertEquals("sk-new", apiKeyStorage.getApiKey(ApiKeyStorage.Keys.OPEN_AI))
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
