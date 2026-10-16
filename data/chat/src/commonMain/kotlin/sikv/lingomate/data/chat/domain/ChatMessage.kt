package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatMessage", exact = true)
data class ChatMessage(
    val id: String,
    val status: Status,
    val role: Role,
    var text: String,
    val error: String? = null,
) {
    enum class Status {
        IN_PROGRESS,
        DELIVERED,
        FAILED
    }

    enum class Role {
        SYSTEM,
        ASSISTANT,
        USER
    }
}
