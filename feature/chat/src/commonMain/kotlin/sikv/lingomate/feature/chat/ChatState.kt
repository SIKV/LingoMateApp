package sikv.lingomate.feature.chat

import sikv.lingomate.data.chat.domain.ChatMessage
import kotlin.native.ObjCName

@ObjCName("ChatState", exact = true)
data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val currentMessage: String? = null,
) {
    companion object {
        fun empty() = ChatState()
    }
}
