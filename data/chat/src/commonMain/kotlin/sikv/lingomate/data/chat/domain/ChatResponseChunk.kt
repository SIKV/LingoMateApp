package sikv.lingomate.data.chat.domain

sealed class ChatResponseChunk {

    object Created : ChatResponseChunk()

    data class Completed(
        // Includes chat history.
        val content: List<ChatContent>
    ) : ChatResponseChunk()

    object InProgress : ChatResponseChunk()

    object Error : ChatResponseChunk()

    object InternalError : ChatResponseChunk()
}
