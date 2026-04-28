package sikv.lingomate.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatLanguage

@Composable
fun ChatLanguage.toLocalizedString(): String {
    return when (this) {
        ChatLanguage.ENGLISH -> stringResource(R.string.chat_language_english)
        ChatLanguage.SPANISH -> stringResource(R.string.chat_language_spanish)
    }
}
