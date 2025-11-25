package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import kotlin.native.ObjCName

@ObjCName("StartChatState", exact = true)
data class StartChatState(
    val chatLanguages: List<ChatLanguage> = emptyList(),
    val selectedLanguage: ChatLanguage? = null,
    val chatLengths: List<ChatLength> = emptyList(),
    val selectedLength: ChatLength? = null
) {
    companion object {
        fun empty() = StartChatState()
    }
}
