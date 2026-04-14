package sikv.lingomate.data.chat.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.api.OpenAIApi
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatResponseChunk
import sikv.lingomate.data.chat.mapping.toDomain
import sikv.lingomate.data.chat.mapping.toInputDTO
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatService(
    private val chatLanguage: ChatLanguage,
    private val chatLength: ChatLength,
    private val chatModel: ChatModel,
    private val openAIApi: OpenAIApi,
    private val promptBuilder: PromptBuilder
) {

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory.asStateFlow()

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    fun sendMessage(
        message: String,
        scope: CoroutineScope
    ) {
        val userChatMessage = ChatMessage(
            id = Uuid.random().toHexString(),
            status = ChatMessage.Status.IN_PROGRESS,
            role = ChatMessage.Role.USER,
            text = message,
        )

        // Optimistic.
        updateChatHistory(userChatMessage)

        // Get server response.
        scope.launch {
            val responseId = Uuid.random().toHexString()

            openAIApi
                .streamResponse(
                    model = chatModel.id,
                    input = _chatHistory.value.map { it.toInputDTO() },
                    instructions = promptBuilder.buildSystemPrompt(chatLanguage, chatLength)
                )
                .map { result ->
                    result.fold(
                        onSuccess = { value -> Result.success(value.toDomain(responseId)) },
                        onFailure = { error -> Result.failure(error) }
                    )
                }.collect { chunkResult ->
                    chunkResult.fold(
                        onSuccess = { chunk ->
                            when (chunk) {
                                ChatResponseChunk.Created -> {
                                    // Mark user message as delivered.
                                    updateChatHistory(
                                        userChatMessage.copy(status = ChatMessage.Status.DELIVERED)
                                    )
                                }
                                is ChatResponseChunk.InProgress -> {
                                    // Mark user message as delivered.
                                    updateChatHistory(
                                        userChatMessage.copy(status = ChatMessage.Status.DELIVERED)
                                    )
                                    // Add assistant message.
                                    updateChatHistory(chunk.content)
                                }
                                is ChatResponseChunk.Completed -> {
                                    // Mark user message as delivered.
                                    updateChatHistory(
                                        userChatMessage.copy(status = ChatMessage.Status.DELIVERED)
                                    )
                                    // Update assistant message as delivered.
                                    updateChatHistory(chunk.content)
                                }
                                is ChatResponseChunk.Failed -> {
                                    // Update assistant message as failed/not delivered.
                                    updateChatHistory(chunk.content)
                                }
                                ChatResponseChunk.Error -> {
                                    // TODO: Mark message as failed?
                                }
                            }
                        },
                        onFailure = {
                            // Mark user message as failed/not delivered.
                            updateChatHistory(
                                userChatMessage.copy(status = ChatMessage.Status.FAILED)
                            )
                        }
                    )
                }
        }
    }

    private fun updateChatHistory(message: ChatMessage) {
        _chatHistory.update { chatHistory ->
            // TODO: Refactor.
            val existingIndex = chatHistory.indexOfFirst { it.id == message.id }
            if (existingIndex >= 0) {
                chatHistory.toMutableList().apply {
                    this[existingIndex] = message
                }
            } else {
                chatHistory + message
            }
        }
    }
}
