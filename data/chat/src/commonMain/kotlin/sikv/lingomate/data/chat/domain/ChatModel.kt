package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatModel", exact = true)
data class ChatModel(
    val provider: ChatModelProvider,
    val model: String
)
