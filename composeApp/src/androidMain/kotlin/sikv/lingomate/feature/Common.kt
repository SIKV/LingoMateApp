package sikv.lingomate.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.PracticeLanguage
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.domain.AssistantLanguage

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
