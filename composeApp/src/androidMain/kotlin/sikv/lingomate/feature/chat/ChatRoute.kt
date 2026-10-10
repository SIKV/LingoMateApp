package sikv.lingomate.feature.chat

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage

sealed interface ChatRoute : NavKey {

    @Serializable
    data object StartChat : ChatRoute

    @Serializable
    data class Chat(val chatConfig: ChatConfig) : ChatRoute

    @Serializable
    data class ChatRecap(val chatConfig: ChatConfig, val messages: List<ChatMessage>) : ChatRoute

    @Serializable
    data object ManageApiKeys : ChatRoute
}
