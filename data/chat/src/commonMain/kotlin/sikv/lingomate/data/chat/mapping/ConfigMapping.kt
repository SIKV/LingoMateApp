package sikv.lingomate.data.chat.mapping

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.config.domain.Config

internal fun Config.toChatModels(): List<ChatModel> {
    return chatModels.mapNotNull { chatModel ->
        val provider = chatModel.provider.toChatModelProvider() ?: return@mapNotNull null

        if (chatModel.model.isBlank()) {
            return@mapNotNull null
        }

        ChatModel(
            provider = provider,
            model = chatModel.model
        )
    }
}

private fun String.toChatModelProvider(): ChatModelProvider? {
    return when (this) {
        "OPEN_AI" -> ChatModelProvider.OPEN_AI
        else -> null
    }
}

internal fun Config.toLanguages(): List<Language> {
    return languageCodes.mapNotNull { code ->
        LANGUAGES_BY_CODE[code.lowercase()]
    }
}

private val LANGUAGES_BY_CODE: Map<String, Language> = Language.entries.associateBy { it.code }

private val Language.code: String
    get() = when (this) {
        Language.ARABIC -> "ar"
        Language.CZECH -> "cs"
        Language.DANISH -> "da"
        Language.DUTCH -> "nl"
        Language.ENGLISH -> "en"
        Language.FINNISH -> "fi"
        Language.FRENCH -> "fr"
        Language.GERMAN -> "de"
        Language.GREEK -> "el"
        Language.HUNGARIAN -> "hu"
        Language.ITALIAN -> "it"
        Language.JAPANESE -> "ja"
        Language.KOREAN -> "ko"
        Language.NORWEGIAN -> "no"
        Language.POLISH -> "pl"
        Language.PORTUGUESE -> "pt"
        Language.ROMANIAN -> "ro"
        Language.SPANISH -> "es"
        Language.SWEDISH -> "sv"
        Language.TURKISH -> "tr"
        Language.UKRAINIAN -> "uk"
    }
