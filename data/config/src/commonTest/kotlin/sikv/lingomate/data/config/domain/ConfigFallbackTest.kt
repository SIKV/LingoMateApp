package sikv.lingomate.data.config.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigFallbackTest {

    private val fallback = Config(
        chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "fallback-model")),
        languageCodes = listOf("en")
    )

    @Test
    fun `Keeps the sections the config carries`() {
        val config = Config(
            chatModels = listOf(ConfigChatModel(provider = "OPEN_AI", model = "gpt-5.1")),
            languageCodes = listOf("uk", "de")
        )

        assertEquals(config, config.withFallback(fallback))
    }

    @Test
    fun `Fills in only the sections the config leaves out`() {
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
    fun `Falls back entirely for an empty config`() {
        assertEquals(fallback, Config().withFallback(fallback))
    }
}
