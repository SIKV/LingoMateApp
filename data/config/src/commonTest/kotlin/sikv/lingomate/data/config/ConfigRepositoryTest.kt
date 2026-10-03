package sikv.lingomate.data.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import sikv.lingomate.api.remoteconfig.RemoteConfigApi
import sikv.lingomate.data.config.datasource.CachedConfigDataSource
import sikv.lingomate.data.config.datasource.FallbackConfigDataSource
import sikv.lingomate.data.config.datasource.RemoteConfigDataSource
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConfigRepositoryTest {

    private val keyValueStorage = FakeKeyValueStorage()
    private val cachedConfigDataSource = CachedConfigDataSource(keyValueStorage)
    private val refreshScope = CoroutineScope(SupervisorJob())

    private val cachedConfig = Config(
        chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "gpt-5-nano")),
        languageCodes = listOf("en", "uk")
    )

    private val remoteConfig = Config(
        chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "gpt-5.1")),
        languageCodes = listOf("de", "fr")
    )

    private val remoteConfigJson = """
        {
          "chat_models": [{ "provider": "OPEN_AI", "model": "gpt-5.1" }],
          "languages": ["de", "fr"]
        }
    """.trimIndent()

    @AfterTest
    fun tearDown() {
        refreshScope.cancel()
    }

    @Test
    fun waitsForTheRemoteAndCachesItWhenNothingIsCached() = runTest {
        val repository = createRepository { respond(remoteConfigJson) }

        assertEquals(remoteConfig, repository.getConfig())
        assertEquals(remoteConfig, cachedConfigDataSource.getConfig())
    }

    @Test
    fun startsWithTheCachedConfigWithoutWaitingForTheRemote() = runTest {
        cachedConfigDataSource.setConfig(cachedConfig)

        val repository = createRepository { awaitCancellation() }

        assertEquals(cachedConfig, repository.getConfig())
    }

    @Test
    fun appliesTheRefreshedConfigFromTheNextLaunch() = runTest {
        cachedConfigDataSource.setConfig(cachedConfig)

        val repository = createRepository { respond(remoteConfigJson) }

        assertEquals(cachedConfig, repository.getConfig())

        awaitRefresh()

        assertEquals(cachedConfig, repository.getConfig())
        assertEquals(remoteConfig, createRepository { respondError(HttpStatusCode.NotFound) }.getConfig())
    }

    @Test
    fun keepsTheCachedConfigWhenTheRefreshFails() = runTest {
        cachedConfigDataSource.setConfig(cachedConfig)

        createRepository { respondError(HttpStatusCode.NotFound) }.getConfig()

        awaitRefresh()

        assertEquals(cachedConfig, cachedConfigDataSource.getConfig())
    }

    @Test
    fun fallsBackWithoutCachingWhenNothingIsCachedAndTheRemoteFails() = runTest {
        val repository = createRepository { respondError(HttpStatusCode.NotFound) }

        assertEquals(FallbackConfigDataSource().getConfig(), repository.getConfig())
        assertNull(cachedConfigDataSource.getConfig())
    }

    @Test
    fun fillsInTheSectionsTheCachedConfigLeavesOutFromTheFallback() = runTest {
        cachedConfigDataSource.setConfig(Config(languageCodes = listOf("uk")))

        val repository = createRepository { awaitCancellation() }

        assertEquals(
            Config(
                chatModels = FallbackConfigDataSource().getConfig().chatModels,
                languageCodes = listOf("uk")
            ),
            repository.getConfig()
        )
    }

    private fun createRepository(respondToRemote: MockRequestHandler): ConfigRepository {
        val remoteConfigApi = RemoteConfigApi(
            client = HttpClient(MockEngine(respondToRemote)),
            json = Json { ignoreUnknownKeys = true },
            configUrl = "https://example.com/remote_config.json"
        )

        return ConfigRepository(
            remoteConfigDataSource = RemoteConfigDataSource(remoteConfigApi),
            cachedConfigDataSource = cachedConfigDataSource,
            fallbackConfigDataSource = FallbackConfigDataSource(),
            refreshScope = refreshScope
        )
    }

    private suspend fun awaitRefresh() {
        refreshScope.coroutineContext.job.children.toList().joinAll()
    }
}
