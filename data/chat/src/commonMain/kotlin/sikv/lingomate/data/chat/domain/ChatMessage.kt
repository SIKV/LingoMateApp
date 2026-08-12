package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

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
