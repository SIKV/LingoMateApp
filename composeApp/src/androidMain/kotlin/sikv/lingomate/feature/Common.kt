package sikv.lingomate.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import sikv.lingomate.R
import sikv.lingomate.data.apikeystorage.ApiKeyProvider
import sikv.lingomate.domain.chat.ChatModel
import sikv.lingomate.domain.chat.ChatModelProvider
import sikv.lingomate.domain.chat.PracticeLanguage
import sikv.lingomate.domain.chat.PracticeType
import sikv.lingomate.domain.chat.AssistantLanguage

@Composable
fun PracticeLanguage.toLocalizedString(): String {
    return when (this) {
        PracticeLanguage.ENGLISH -> stringResource(R.string.chat_language_english)
        PracticeLanguage.SPANISH -> stringResource(R.string.chat_language_spanish)
    }
}

@Composable
fun AssistantLanguage.toLocalizedString(): String {
    return when (this) {
        AssistantLanguage.ENGLISH -> stringResource(R.string.assistant_language_english)
    }
}

@Composable
fun PracticeType.toLocalizedString(): String {
    return when (this) {
        PracticeType.CONVERSATION -> stringResource(R.string.practice_type_conversation)
        PracticeType.TRANSLATION -> stringResource(R.string.practice_type_translation)
    }
}

@Composable
fun ChatModel.toLocalizedString(): String {
    return when (provider) {
        ChatModelProvider.ON_DEVICE -> stringResource(R.string.chat_model_provider_on_device)
        ChatModelProvider.OPEN_AI -> model
    }
}

@Composable
fun ApiKeyProvider.toLocalizedString(): String {
    return when (this) {
        ApiKeyProvider.OpenAI -> stringResource(R.string.api_key_provider_open_ai)
    }
}
