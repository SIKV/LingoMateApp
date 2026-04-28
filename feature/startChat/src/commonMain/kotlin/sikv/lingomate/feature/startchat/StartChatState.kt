package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.ChatLanguage
import kotlin.native.ObjCName

@ObjCName("StartChatState", exact = true)
data class StartChatState(
    val chatLanguages: List<ChatLanguage> = emptyList(),
    val selectedLanguage: ChatLanguage? = null
) {
    companion object {
        fun empty() = StartChatState()
    }
}
