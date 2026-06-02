@file:OptIn(ExperimentalUuidApi::class)

package sikv.lingomate.data.chat.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.ondevice.llm.OnDeviceLLM
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OnDeviceChatService(
    private val chatLanguage: ChatLanguage,
    private val onDeviceLLM: OnDeviceLLM,
    private val promptBuilder: PromptBuilder
) : ChatService {
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    override val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory.asStateFlow()

    override fun startChat(scope: CoroutineScope) {
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

            onDeviceLLM.streamResponse(
                input = "", // TODO: Provide input.
                instructions = promptBuilder.buildSystemPrompt(chatLanguage)
            ).collect { message ->
                if (message != null) {
                    _chatHistory.updateChatHistory(
                        assistantMessage.copy(
                            text = message,
                            status = ChatMessage.Status.DELIVERED
                        )
                    )
                } else {
                    _chatHistory.updateChatHistory(
                        assistantMessage.copy(
                            status = ChatMessage.Status.FAILED
                        )
                    )
                }
            }
        }
    }

    override fun sendMessage(message: String, scope: CoroutineScope) {
        TODO("Not yet implemented")
    }

    override fun retryMessage(messageId: String, scope: CoroutineScope) {
        TODO("Not yet implemented")
    }
}
