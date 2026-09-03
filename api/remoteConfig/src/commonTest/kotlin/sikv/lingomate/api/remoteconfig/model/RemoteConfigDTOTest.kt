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
    fun `Reads the chat models of a published config`() {
        val config = json.decodeFromString<RemoteConfigDTO>(
            """
            {
              "app_version": "2.0",
              "chat_models": [
                {
                  "provider": "OPEN_AI",
                  "model": "gpt-5-nano"
                },
                {
                  "provider": "OPEN_AI",
                  "model": "gpt-5.1"
                }
              ],
              "languages": ["en", "uk"]
            }
            """.trimIndent()
        )

        assertEquals(
            listOf(
                ChatModelDTO(provider = "OPEN_AI", model = "gpt-5-nano"),
                ChatModelDTO(provider = "OPEN_AI", model = "gpt-5.1")
            ),
            config.chatModels
        )
        assertEquals(listOf("en", "uk"), config.languages)
    }

    @Test
    fun `Reads a config that carries no chat models yet`() {
        val config = json.decodeFromString<RemoteConfigDTO>("""{ "app_version": "2.0" }""")

        assertEquals(emptyList(), config.chatModels)
        assertEquals(emptyList(), config.languages)
    }
}
