package sikv.lingomate.data.chat.mapping

import sikv.lingomate.api.model.OpenAIResponsesResponseDTO
import sikv.lingomate.data.chat.domain.ChatContent
import sikv.lingomate.data.chat.domain.ChatResponseChunk
import sikv.lingomate.data.chat.domain.ChatResponseChunkType

fun OpenAIResponsesResponseDTO.toDomain(chatHistory: List<ChatContent>): ChatResponseChunk {
    val type = this.toChatResponseChunkType() ?: return ChatResponseChunk.InternalError

    return when (type) {
        ChatResponseChunkType.Created -> ChatResponseChunk.Created
        ChatResponseChunkType.Incomplete,
        ChatResponseChunkType.Error,
        ChatResponseChunkType.Failed -> ChatResponseChunk.Error
        ChatResponseChunkType.InProgress,
        ChatResponseChunkType.OutputItemAdded,
        ChatResponseChunkType.OutputItemDone,
        ChatResponseChunkType.ContentPartAdded,
        ChatResponseChunkType.ContentPartDone,
        ChatResponseChunkType.OutputTextDelta,
        ChatResponseChunkType.OutputTextDone -> ChatResponseChunk.InProgress
        ChatResponseChunkType.Completed -> ChatResponseChunk.Completed(
            content = chatHistory + ChatContent(
                role = ChatContent.Role.ASSISTANT,
                text = this.response?.output?.firstOrNull()?.content?.firstOrNull()?.text ?: return ChatResponseChunk.InternalError
            )
        )
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
        else -> return null
    }
}
