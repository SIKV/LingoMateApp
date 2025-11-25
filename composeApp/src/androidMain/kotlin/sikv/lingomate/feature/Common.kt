package sikv.lingomate.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength

@Composable
fun ChatLanguage.toLocalizedString(): String {
    return when (this) {
        ChatLanguage.ENGLISH -> stringResource(R.string.chat_language_english)
        ChatLanguage.SPANISH -> stringResource(R.string.chat_language_spanish)
    }
}

@Composable
fun ChatLength.toLocalizedString(): String {
    return when (this) {
        ChatLength.SHORT_LENGTH -> stringResource(R.string.chat_length_short)
        ChatLength.MEDIUM_LENGTH -> stringResource(R.string.chat_length_medium)
        ChatLength.LONG_LENGTH -> stringResource(R.string.chat_length_long)
    }
}
