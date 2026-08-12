package sikv.lingomate.domain.chat

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatModel", exact = true)
data class ChatModel(
    val provider: ChatModelProvider,
    val model: String
)
