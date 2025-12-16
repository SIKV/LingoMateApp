package sikv.lingomate.data.chat.domain

data class ChatContent(
    val role: Role,
    var text: String
) {
    enum class Role {
        ASSISTANT,
        USER
    }
}
