package sikv.lingomate.feature.startchat

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelTier
import kotlin.native.ObjCName

@ObjCName("ChatModelOption", exact = true)
data class ChatModelOption(
    val chatModel: ChatModel,
    val tier: ChatModelTier?,
    val apiKeyNeeded: Boolean
)
