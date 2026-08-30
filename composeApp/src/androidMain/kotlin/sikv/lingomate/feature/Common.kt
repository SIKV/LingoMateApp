package sikv.lingomate.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import sikv.lingomate.R
import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.feature.startchat.ChatModelOption

@Composable
fun Language.toLocalizedString(): String {
    return stringResource(languageNameRes)
}

private val Language.languageNameRes: Int
    get() = when (this) {
        Language.ARABIC -> R.string.language_arabic
        Language.CZECH -> R.string.language_czech
        Language.DANISH -> R.string.language_danish
        Language.DUTCH -> R.string.language_dutch
        Language.ENGLISH -> R.string.language_english
        Language.FINNISH -> R.string.language_finnish
        Language.FRENCH -> R.string.language_french
        Language.GERMAN -> R.string.language_german
        Language.GREEK -> R.string.language_greek
        Language.HUNGARIAN -> R.string.language_hungarian
        Language.ITALIAN -> R.string.language_italian
        Language.JAPANESE -> R.string.language_japanese
        Language.KOREAN -> R.string.language_korean
        Language.NORWEGIAN -> R.string.language_norwegian
        Language.POLISH -> R.string.language_polish
        Language.PORTUGUESE -> R.string.language_portuguese
        Language.ROMANIAN -> R.string.language_romanian
        Language.SPANISH -> R.string.language_spanish
        Language.SWEDISH -> R.string.language_swedish
        Language.TURKISH -> R.string.language_turkish
        Language.UKRAINIAN -> R.string.language_ukrainian
    }

@Composable
fun PracticeType.toLocalizedString(): String {
    return when (this) {
        PracticeType.CONVERSATION -> stringResource(R.string.practice_type_conversation)
        PracticeType.TRANSLATION -> stringResource(R.string.practice_type_translation)
    }
}

@Composable
fun ChatModelOption.toLocalizedString(): String {
    return when (chatModel.provider) {
        ChatModelProvider.ON_DEVICE -> stringResource(R.string.chat_model_provider_on_device)
        ChatModelProvider.OPEN_AI -> chatModel.model
    }
}

@Composable
fun ApiKeyProvider.toLocalizedString(): String {
    return when (this) {
        ApiKeyProvider.OpenAI -> stringResource(R.string.api_key_provider_open_ai)
    }
}
