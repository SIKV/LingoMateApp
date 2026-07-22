package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.PracticeLanguage
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.domain.AssistantLanguage
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
