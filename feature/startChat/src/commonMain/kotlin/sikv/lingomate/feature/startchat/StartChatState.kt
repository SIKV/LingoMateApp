package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import kotlin.native.ObjCName

@ObjCName("StartChatState", exact = true)
data class StartChatState(
    val chatModelOptions: List<ChatModelOption> = emptyList(),
    val selectedChatModelOption: ChatModelOption? = null,

    val practiceLanguages: List<Language> = emptyList(),
    val selectedPracticeLanguage: Language? = null,

    val assistantLanguages: List<Language> = emptyList(),
    val selectedAssistantLanguage: Language? = null,

    val practiceTypes: List<PracticeType> = emptyList(),
    val selectedPracticeType: PracticeType? = null
) {
    companion object {
        fun empty() = StartChatState()
    }
}
