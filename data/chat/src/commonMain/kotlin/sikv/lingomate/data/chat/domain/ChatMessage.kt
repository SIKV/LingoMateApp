package sikv.lingomate.data.chat.domain

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
        ASSISTANT,
        USER
    }
}
