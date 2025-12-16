package sikv.lingomate.data.chat.mapping

import sikv.lingomate.api.model.OpenAIInputDTO
import sikv.lingomate.data.chat.domain.ChatContent

internal fun ChatContent.toInputDTO(): OpenAIInputDTO {
    return OpenAIInputDTO(
        role = this.role.toDTO(),
        content = this.text
    )
}

internal fun ChatContent.Role.toDTO(): String {
    return when (this) {
        ChatContent.Role.ASSISTANT -> "assistant"
        ChatContent.Role.USER -> "user"
    }
}
