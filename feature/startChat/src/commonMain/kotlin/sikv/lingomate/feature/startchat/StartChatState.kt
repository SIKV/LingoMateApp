package sikv.lingomate.feature.startchat

import sikv.lingomate.domain.chat.ChatModel
import sikv.lingomate.domain.chat.PracticeLanguage
import sikv.lingomate.domain.chat.PracticeType
import sikv.lingomate.domain.chat.AssistantLanguage
import kotlin.native.ObjCName

@ObjCName("StartChatState", exact = true)
data class StartChatState(
    val chatModels: List<ChatModel> = emptyList(),
    val selectedChatModel: ChatModel? = null,

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
