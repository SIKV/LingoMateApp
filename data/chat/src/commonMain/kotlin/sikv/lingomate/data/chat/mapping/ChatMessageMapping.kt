package sikv.lingomate.data.chat.mapping

import sikv.lingomate.api.model.OpenAIInputDTO
import sikv.lingomate.data.chat.domain.ChatMessage

internal fun ChatMessage.toInputDTO(): OpenAIInputDTO {
    return OpenAIInputDTO(
        role = this.role.toDTO(),
        content = this.text
    )
}

internal fun ChatMessage.Role.toDTO(): String {
    return when (this) {
        ChatMessage.Role.SYSTEM -> "system"
        ChatMessage.Role.ASSISTANT -> "assistant"
        ChatMessage.Role.USER -> "user"
    }
}
