package sikv.lingomate.data.config.datasource

import kotlinx.coroutines.test.runTest
import sikv.lingomate.data.config.FakeKeyValueStorage
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CachedConfigDataSourceTest {

    private val keyValueStorage = FakeKeyValueStorage()

    @Test
    fun isEmptyBeforeAnythingIsCached() = runTest {
        assertNull(CachedConfigDataSource(keyValueStorage).getConfig())
    }

    @Test
    fun readsBackTheCachedConfigAfterARelaunch() = runTest {
        val config = Config(
            chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "gpt-5.1")),
            languageCodes = listOf("uk", "de")
        )

        CachedConfigDataSource(keyValueStorage).setConfig(config)

        assertEquals(config, CachedConfigDataSource(keyValueStorage).getConfig())
    }

    @Test
    fun dropsACachedConfigThatCannotBeRead() = runTest {
        keyValueStorage.entries["config.cached_config"] = "not json"

        assertNull(CachedConfigDataSource(keyValueStorage).getConfig())
        assertTrue(keyValueStorage.entries.isEmpty())
    }
}
