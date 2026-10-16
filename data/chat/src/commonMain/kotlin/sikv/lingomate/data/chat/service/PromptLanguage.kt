package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.Language

// Both sides can name the same language. There is then no second language to fall back on: the session runs
// entirely in the practice language, and translation practice turns into rephrasing.
internal val ChatConfig.isMonolingual: Boolean
    get() = practiceLanguage == assistantLanguage

internal val ChatConfig.explanationLanguage: String
    get() = if (isMonolingual) {
        "simple ${practiceLanguage.promptName}"
    } else {
        assistantLanguage.promptName
    }

internal val Language.promptName: String
    get() = when (this) {
        Language.ARABIC -> "Arabic"
        Language.CZECH -> "Czech"
        Language.DANISH -> "Danish"
        Language.DUTCH -> "Dutch"
        Language.ENGLISH -> "English"
        Language.FINNISH -> "Finnish"
        Language.FRENCH -> "French"
        Language.GERMAN -> "German"
        Language.GREEK -> "Greek"
        Language.HUNGARIAN -> "Hungarian"
        Language.ITALIAN -> "Italian"
        Language.JAPANESE -> "Japanese"
        Language.KOREAN -> "Korean"
        Language.NORWEGIAN -> "Norwegian"
        Language.POLISH -> "Polish"
        Language.PORTUGUESE -> "Portuguese"
        Language.ROMANIAN -> "Romanian"
        Language.SPANISH -> "Spanish"
        Language.SWEDISH -> "Swedish"
        Language.TURKISH -> "Turkish"
        Language.UKRAINIAN -> "Ukrainian"
    }
