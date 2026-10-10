package sikv.lingomate.feature.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.service.ChatService
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val chatService = FakeChatService()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun trimsSpacesAroundTheMessage() {
        ChatViewModel(chatService).sendMessage("  Hallo, wie geht's?\n ")

        assertEquals(listOf("Hallo, wie geht's?"), chatService.sentMessages)
    }

    @Test
    fun keepsSpacesInsideTheMessage() {
        ChatViewModel(chatService).sendMessage("Guten  Morgen")

        assertEquals(listOf("Guten  Morgen"), chatService.sentMessages)
    }

    @Test
    fun ignoresABlankMessage() {
        ChatViewModel(chatService).sendMessage(" \n\t ")

        assertTrue(chatService.sentMessages.isEmpty())
    }

    @Test
    fun cannotEndChatBeforeAUserMessageIsDelivered() {
        chatService.chatHistory.value = listOf(
            message(ChatMessage.Role.ASSISTANT, ChatMessage.Status.DELIVERED),
            message(ChatMessage.Role.USER, ChatMessage.Status.FAILED)
        )

        assertFalse(ChatViewModel(chatService).uiState.value.canEndChat)
    }

    @Test
    fun canEndChatOnceAUserMessageIsDelivered() {
        chatService.chatHistory.value = listOf(
            message(ChatMessage.Role.ASSISTANT, ChatMessage.Status.DELIVERED),
            message(ChatMessage.Role.USER, ChatMessage.Status.DELIVERED)
        )

        assertTrue(ChatViewModel(chatService).uiState.value.canEndChat)
    }

    private fun message(role: ChatMessage.Role, status: ChatMessage.Status): ChatMessage {
        return ChatMessage(id = "$role-$status", status = status, role = role, text = "Hallo")
    }
}

private class FakeChatService : ChatService {

    val sentMessages = mutableListOf<String>()

    override val chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())

    override fun startChat(scope: CoroutineScope) {}

    override fun sendMessage(message: String, scope: CoroutineScope) {
        sentMessages += message
    }

    override fun retryMessage(messageId: String, scope: CoroutineScope) {}
}
