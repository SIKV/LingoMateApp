package sikv.lingomate.api.remoteconfig.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class RemoteConfigDTOTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Test
    fun readsTheChatModelsOfAPublishedConfig() {
        val config = json.decodeFromString<RemoteConfigDTO>(
            """
            {
              "chat_models": [
                {
                  "provider": "OPEN_AI",
                  "model": "gpt-5-nano",
                  "tier": "fast"
                },
                {
                  "provider": "OPEN_AI",
                  "model": "gpt-5.1",
                  "tier": "smart"
                }
              ],
              "languages": ["en", "uk"]
            }
            """.trimIndent()
        )

        assertEquals(
            listOf(
                ChatModelDTO(provider = "OPEN_AI", model = "gpt-5-nano", tier = "fast"),
                ChatModelDTO(provider = "OPEN_AI", model = "gpt-5.1", tier = "smart")
            ),
            config.chatModels
        )
        assertEquals(listOf("en", "uk"), config.languages)
    }

    @Test
    fun readsAConfigThatCarriesNoChatModelsYet() {
        val config = json.decodeFromString<RemoteConfigDTO>("""{ "app_version": "2.0" }""")

        assertEquals(emptyList(), config.chatModels)
        assertEquals(emptyList(), config.languages)
    }
}
