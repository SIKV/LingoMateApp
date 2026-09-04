package sikv.lingomate.data.chat.mapping

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.config.datasource.FallbackConfigDataSource
import sikv.lingomate.data.config.domain.Config
import sikv.lingomate.data.config.domain.ConfigChatModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigMappingTest {

    @Test
    fun keepsTheModelsAndTheirOrder() {
        val config = Config(
            chatModels = listOf(
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5-nano"),
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5.1")
            )
        )

        assertEquals(
            listOf(
                ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-nano"),
                ChatModel(ChatModelProvider.OPEN_AI, "gpt-5.1")
            ),
            config.toChatModels()
        )
    }

    @Test
    fun dropsAModelOfAnUnknownProvider() {
        val config = Config(
            chatModels = listOf(
                ConfigChatModel(provider = "ANTHROPIC", model = "claude-opus-5"),
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5-mini")
            )
        )

        assertEquals(
            listOf(ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-mini")),
            config.toChatModels()
        )
    }

    @Test
    fun dropsAModelWithABlankName() {
        val config = Config(
            chatModels = listOf(
                ConfigChatModel(provider = "OPEN_AI", model = " ")
            )
        )

        assertEquals(emptyList(), config.toChatModels())
    }

    @Test
    fun mapsAConfigWithNoModelsToAnEmptyList() {
        assertEquals(emptyList(), Config().toChatModels())
    }

    @Test
    fun keepsTheLanguagesAndTheirOrder() {
        val config = Config(languageCodes = listOf("uk", "en", "ja"))

        assertEquals(
            listOf(Language.UKRAINIAN, Language.ENGLISH, Language.JAPANESE),
            config.toLanguages()
        )
    }

    @Test
    fun readsALanguageCodeInAnyCase() {
        val config = Config(languageCodes = listOf("EN", "Es"))

        assertEquals(listOf(Language.ENGLISH, Language.SPANISH), config.toLanguages())
    }

    @Test
    fun dropsALanguageTheAppDoesNotShip() {
        val config = Config(languageCodes = listOf("vi", "en"))

        assertEquals(listOf(Language.ENGLISH), config.toLanguages())
    }

    @Test
    fun mapsAConfigWithNoLanguagesToAnEmptyList() {
        assertEquals(emptyList(), Config().toLanguages())
    }

    @Test
    fun readsTheChatModelTheAppFallsBackTo() {
        val config = FallbackConfigDataSource().getConfig()

        assertEquals(1, config.toChatModels().size)
    }

    @Test
    fun fallsBackToEveryLanguageTheAppShipsWith() {
        val config = FallbackConfigDataSource().getConfig()

        assertEquals(Language.entries, config.toLanguages())
    }
}
