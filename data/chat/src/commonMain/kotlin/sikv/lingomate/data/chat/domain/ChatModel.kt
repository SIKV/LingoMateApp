package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatModel", exact = true)
data class ChatModel(
    val provider: ChatModelProvider,
    val model: String
)
