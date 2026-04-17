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

    @OptIn(ExperimentalUuidApi::class)
    fun startChat(scope: CoroutineScope) {
        // Do not add the initial system message to the chat history,
        // as it's not part of the conversation and is only used to get the first response from the server.
        val startChatMessage = ChatMessage(
            id = Uuid.random().toHexString(),
            status = ChatMessage.Status.IN_PROGRESS,
            role = ChatMessage.Role.SYSTEM,
            // TODO: For testing.
            text = "You are a helpful assistant. Start the conversation by greeting the user and asking how you can help",
        )

        scope.launch {
            val responseId = Uuid.random().toHexString()

            val assistantMessage = ChatMessage(
                id = responseId,
                status = ChatMessage.Status.IN_PROGRESS,
                role = ChatMessage.Role.ASSISTANT,
                text = ""
            )

            // Show initial assistant message as typing indicator or similar.
            updateChatHistory(assistantMessage)

            openAIApi
                .streamResponse(
                    model = chatModel.id,
                    input = listOf(startChatMessage.toInputDTO()),
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
                                    // Do nothing, as the initial assistant message is already in the chat history as a typing indicator.
                                }
                                is ChatResponseChunk.InProgress -> {
                                    // Do nothing, as the initial assistant message is already in the chat history as a typing indicator.
                                }
                                is ChatResponseChunk.Completed -> {
                                    // Update assistant message and mark it as delivered.
                                    // The initial assistant message is already in the chat history,
                                    // so this will update it with the actual content and mark it as delivered.
                                    updateChatHistory(chunk.content)
                                }
                                is ChatResponseChunk.Failed -> {
                                    // Update assistant message as failed/not delivered.
                                    updateChatHistory(chunk.content)
                                }
                                ChatResponseChunk.Error -> {
                                    // TODO: Mark message as failed.
                                }
                            }
                        },
                        onFailure = {
                            // TODO: Mark message as failed.
                        }
                    )
                }
        }
    }

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

        // Optimistic UI update: Add user message to chat history immediately with IN_PROGRESS status.
        updateChatHistory(userChatMessage)

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
                                    // TODO: Mark message as failed.
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
