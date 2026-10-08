package sikv.lingomate.data.chat.mapping

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelEntry
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.ChatModelTier
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
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5-nano", tier = "fast"),
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5.1", tier = "smart")
            )
        )

        assertEquals(
            listOf(
                ChatModelEntry(ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-nano"), ChatModelTier.FAST),
                ChatModelEntry(ChatModel(ChatModelProvider.OPEN_AI, "gpt-5.1"), ChatModelTier.SMART)
            ),
            config.toChatModels()
        )
    }

    @Test
    fun dropsAModelOfAnUnknownProvider() {
        val config = Config(
            chatModels = listOf(
                ConfigChatModel(provider = "ANTHROPIC", model = "claude-opus-5", tier = "smart"),
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5-mini", tier = "fast")
            )
        )

        assertEquals(
            listOf(ChatModelEntry(ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-mini"), ChatModelTier.FAST)),
            config.toChatModels()
        )
    }

    @Test
    fun dropsAModelWithABlankName() {
        val config = Config(
            chatModels = listOf(
                ConfigChatModel(provider = "OPEN_AI", model = " ", tier = "fast")
            )
        )

        assertEquals(emptyList(), config.toChatModels())
    }

    @Test
    fun keepsAModelOfAnUnknownTierWithoutATier() {
        val config = Config(
            chatModels = listOf(
                ConfigChatModel(provider = "OPEN_AI", model = "gpt-5-mini", tier = "balanced")
            )
        )

        assertEquals(
            listOf(ChatModelEntry(ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-mini"), tier = null)),
            config.toChatModels()
        )
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

        assertEquals(ChatModelTier.FAST, config.toChatModels().single().tier)
    }

    @Test
    fun fallsBackToEveryLanguageTheAppShipsWith() {
        val config = FallbackConfigDataSource().getConfig()

        assertEquals(Language.entries, config.toLanguages())
    }
}
