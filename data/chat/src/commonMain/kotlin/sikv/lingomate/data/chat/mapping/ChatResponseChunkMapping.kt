package sikv.lingomate.data.chat.mapping

import sikv.lingomate.api.model.OpenAIResponsesResponseDTO
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatResponseChunk
import sikv.lingomate.data.chat.domain.ChatResponseChunkType

fun OpenAIResponsesResponseDTO.toDomain(id: String): ChatResponseChunk {
    val type = this.toChatResponseChunkType() ?: return ChatResponseChunk.Error

    return when (type) {
        ChatResponseChunkType.Created -> ChatResponseChunk.Created
        ChatResponseChunkType.Error -> ChatResponseChunk.Error
        ChatResponseChunkType.Incomplete,
        ChatResponseChunkType.Failed -> ChatResponseChunk.Failed(
            content = ChatMessage(
                id = id,
                status = ChatMessage.Status.FAILED,
                role = ChatMessage.Role.ASSISTANT,
                text = "",
                error = "" // TODO: Provide error details from the response if available.
            )
        )
        ChatResponseChunkType.InProgress,
        ChatResponseChunkType.OutputItemAdded,
        ChatResponseChunkType.OutputItemDone,
        ChatResponseChunkType.ContentPartAdded,
        ChatResponseChunkType.ContentPartDone,
        ChatResponseChunkType.OutputTextDelta,
        ChatResponseChunkType.OutputTextDone -> ChatResponseChunk.InProgress(
            content = ChatMessage(
                id = id,
                status = ChatMessage.Status.IN_PROGRESS,
                role = ChatMessage.Role.ASSISTANT,
                text = ""
            )
        )
        ChatResponseChunkType.Completed -> {
            val text = this.response?.output?.firstOrNull()?.content?.firstOrNull()?.text
            ChatResponseChunk.Completed(
                content = ChatMessage(
                    id = id,
                    status = if (text == null) ChatMessage.Status.FAILED else ChatMessage.Status.DELIVERED,
                    role = ChatMessage.Role.ASSISTANT,
                    text = text ?: ""
                )
            )
        }
    }
}

internal fun OpenAIResponsesResponseDTO.toChatResponseChunkType(): ChatResponseChunkType? {
    return when (this.type) {
        "error" -> ChatResponseChunkType.Error
        "response.created" -> ChatResponseChunkType.Created
        "response.in_progress" -> ChatResponseChunkType.InProgress
        "response.completed" -> ChatResponseChunkType.Completed
        "response.failed" -> ChatResponseChunkType.Failed
        "response.incomplete" -> ChatResponseChunkType.Incomplete
        "response.output_item.added" -> ChatResponseChunkType.OutputItemAdded
        "response.output_item.done" -> ChatResponseChunkType.OutputItemDone
        "response.content_part.added" -> ChatResponseChunkType.ContentPartAdded
        "response.content_part.done" -> ChatResponseChunkType.ContentPartDone
        "response.output_text.delta" -> ChatResponseChunkType.OutputTextDelta
        "response.output_text.done" -> ChatResponseChunkType.OutputTextDone
        else -> null
    }
}
