package sikv.lingomate.data.chat.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.api.openai.OpenAIApi
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatResponseChunk
import sikv.lingomate.data.chat.mapping.toDomain
import sikv.lingomate.data.chat.mapping.toInputDTO
import sikv.lingomate.logger.Log
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OpenAIChatService(
    private val chatConfig: ChatConfig,
    private val openAIApi: OpenAIApi,
    private val promptBuilder: PromptBuilder
) : ChatService {

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    override val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    override fun startChat(scope: CoroutineScope) {
        // Do not add the initial system message to the chat history,
        // as it's not part of the conversation and is only used to get the first response from the server.
        val startChatMessage = generateStartChatMessage(promptBuilder, chatConfig)

        Log.d { "Starting chat with model ${chatConfig.chatModel.model}." }

        scope.launch {
            val responseId = Uuid.random().toHexString()

            val assistantMessage = ChatMessage(
                id = responseId,
                status = ChatMessage.Status.IN_PROGRESS,
                role = ChatMessage.Role.ASSISTANT,
                text = ""
            )

            // Show initial assistant message as typing indicator or similar.
            _chatHistory.updateChatHistory(assistantMessage)

            openAIApi
                .streamResponse(
                    model = chatConfig.chatModel.model,
                    input = listOf(startChatMessage.toInputDTO()),
                    instructions = promptBuilder.buildSystemPrompt(chatConfig)
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
                                    _chatHistory.updateChatHistory(chunk.content)
                                }
                                is ChatResponseChunk.Failed -> {
                                    Log.e { "Failed to start chat: ${chunk.content.error}" }

                                    // Update assistant message as failed/not delivered.
                                    _chatHistory.updateChatHistory(chunk.content)
                                }
                                ChatResponseChunk.Error -> {
                                    Log.e { "Failed to start chat: response stream returned an error." }

                                    _chatHistory.markLastMessageAsFailed()
                                }
                            }
                        },
                        onFailure = { error ->
                            Log.e(error) { "Failed to start chat." }

                            _chatHistory.markLastMessageAsFailed()
                        }
                    )
                }
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override fun sendMessage(
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
        _chatHistory.updateChatHistory(userChatMessage)

        scope.launch {
            val responseId = Uuid.random().toHexString()

            openAIApi
                .streamResponse(
                    model = chatConfig.chatModel.model,
                    input = _chatHistory.value.map { it.toInputDTO() },
                    instructions = promptBuilder.buildSystemPrompt(chatConfig)
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
                                    _chatHistory.updateChatHistory(
                                        userChatMessage.copy(status = ChatMessage.Status.DELIVERED)
                                    )
                                }

                                is ChatResponseChunk.InProgress -> {
                                    // Add assistant message.
                                    _chatHistory.updateChatHistory(chunk.content)
                                }

                                is ChatResponseChunk.Completed -> {
                                    // Update assistant message as delivered.
                                    _chatHistory.updateChatHistory(chunk.content)
                                }

                                is ChatResponseChunk.Failed -> {
                                    Log.e { "Failed to send message: ${chunk.content.error}" }

                                    // Update assistant message as failed/not delivered.
                                    _chatHistory.updateChatHistory(chunk.content)
                                }

                                ChatResponseChunk.Error -> {
                                    Log.e { "Failed to send message: response stream returned an error." }

                                    _chatHistory.markLastMessageAsFailed()
                                }
                            }
                        },
                        onFailure = { error ->
                            Log.e(error) { "Failed to send message." }

                            _chatHistory.markLastMessageAsFailed()
                        }
                    )
                }
        }
    }

    override fun retryMessage(messageId: String, scope: CoroutineScope) {
        val messageToRetry = _chatHistory.value.find { it.id == messageId }

        if (messageToRetry == null) {
            Log.w { "Cannot retry message $messageId: not found in the chat history." }
            return
        }

        // Remove the failed message from the chat history.
        _chatHistory.update { chatHistory ->
            chatHistory.filterNot { it.id == messageId }
        }

        // Resend the message.
        sendMessage(messageToRetry.text, scope)
    }
}
