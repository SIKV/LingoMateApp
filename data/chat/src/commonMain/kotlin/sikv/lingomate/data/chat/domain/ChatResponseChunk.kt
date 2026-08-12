package sikv.lingomate.data.chat.domain

sealed class ChatResponseChunk {

    object Created : ChatResponseChunk()

    data class Completed(
        val content: ChatMessage
    ) : ChatResponseChunk()

    data class InProgress(
        val content: ChatMessage
    ) : ChatResponseChunk()

    data class Failed(
        val content: ChatMessage
    ) : ChatResponseChunk()

    object Error : ChatResponseChunk()
}
