package sikv.lingomate.data.chat.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatMessage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ChatService {
    val chatHistory: StateFlow<List<ChatMessage>>

    fun startChat(scope: CoroutineScope)
    fun sendMessage(message: String, scope: CoroutineScope)
    fun retryMessage(messageId: String, scope: CoroutineScope)
}

@OptIn(ExperimentalUuidApi::class)
internal fun ChatService.generateStartChatMessage(
    promptBuilder: PromptBuilder,
    chatLanguage: ChatLanguage
): ChatMessage {
    return ChatMessage(
        id = Uuid.random().toHexString(),
        status = ChatMessage.Status.IN_PROGRESS,
        role = ChatMessage.Role.SYSTEM,
        text = promptBuilder.buildSystemPrompt(chatLanguage)
    )
}

internal fun MutableStateFlow<List<ChatMessage>>.updateChatHistory(message: ChatMessage) {
    update { chatHistory ->
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

internal fun MutableStateFlow<List<ChatMessage>>.markLastMessageAsFailed() {
    // Mark the last message in chat as failed.
    value.lastOrNull()?.let { lastMessage ->
        updateChatHistory(
            lastMessage.copy(status = ChatMessage.Status.FAILED)
        )
    }
}
