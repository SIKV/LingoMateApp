package sikv.lingomate.data.config.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigFallbackTest {

    private val fallback = Config(
        chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "fallback-model", tier = "fast")),
        languageCodes = listOf("en")
    )

    @Test
    fun keepsTheSectionsTheConfigCarries() {
        val config = Config(
            chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "gpt-5.1", tier = "fast")),
            languageCodes = listOf("uk", "de")
        )

        assertEquals(config, config.withFallback(fallback))
    }

    @Test
    fun fillsInOnlyTheSectionsTheConfigLeavesOut() {
        val config = Config(languageCodes = listOf("uk"))

        assertEquals(
            Config(
                chatModels = fallback.chatModels,
                languageCodes = listOf("uk")
            ),
            config.withFallback(fallback)
        )
    }

    @Test
    fun fallsBackEntirelyForAnEmptyConfig() {
        assertEquals(fallback, Config().withFallback(fallback))
    }
}
