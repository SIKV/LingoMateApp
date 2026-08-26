package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.ChatModel
import kotlin.native.ObjCName

@ObjCName("ChatModelOption", exact = true)
data class ChatModelOption(
    val chatModel: ChatModel,
    val apiKeyNeeded: Boolean
)
