package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.AssistantLanguage
import sikv.lingomate.data.chat.domain.PracticeLanguage
import sikv.lingomate.data.chat.domain.PracticeType
import kotlin.native.ObjCName

@ObjCName("StartChatState", exact = true)
data class StartChatState(
    val chatModelOptions: List<ChatModelOption> = emptyList(),
    val selectedChatModelOption: ChatModelOption? = null,

    val practiceLanguages: List<PracticeLanguage> = emptyList(),
    val selectedPracticeLanguage: PracticeLanguage? = null,

    val assistantLanguages: List<AssistantLanguage> = emptyList(),
    val selectedAssistantLanguage: AssistantLanguage? = null,

    val practiceTypes: List<PracticeType> = emptyList(),
    val selectedPracticeType: PracticeType? = null
) {
    companion object {
        fun empty() = StartChatState()
    }
}
