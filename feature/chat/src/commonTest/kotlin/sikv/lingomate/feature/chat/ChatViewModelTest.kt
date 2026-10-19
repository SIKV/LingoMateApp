package sikv.lingomate.feature.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import sikv.lingomate.analytics.Analytics
import sikv.lingomate.analytics.AnalyticsEvent
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.service.ChatService
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val chatConfig = ChatConfig(
        chatModel = ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-4o-mini"),
        practiceLanguage = Language.GERMAN,
        assistantLanguage = Language.ENGLISH,
        practiceType = PracticeType.CONVERSATION
    )

    private val chatService = FakeChatService()

    private val trackedEvents = mutableListOf<AnalyticsEvent>()

    private fun viewModel() = ChatViewModel(chatConfig, chatService, Analytics(trackedEvents::add))

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
        viewModel().sendMessage("  Hallo, wie geht's?\n ")

        assertEquals(listOf("Hallo, wie geht's?"), chatService.sentMessages)
    }

    @Test
    fun keepsSpacesInsideTheMessage() {
        viewModel().sendMessage("Guten  Morgen")

        assertEquals(listOf("Guten  Morgen"), chatService.sentMessages)
    }

    @Test
    fun ignoresABlankMessage() {
        viewModel().sendMessage(" \n\t ")

        assertTrue(chatService.sentMessages.isEmpty())
    }

    @Test
    fun cannotEndChatBeforeAUserMessageIsDelivered() {
        chatService.chatHistory.value = listOf(
            message(ChatMessage.Role.ASSISTANT, ChatMessage.Status.DELIVERED),
            message(ChatMessage.Role.USER, ChatMessage.Status.FAILED)
        )

        assertFalse(viewModel().uiState.value.canEndChat)
    }

    @Test
    fun canEndChatOnceAUserMessageIsDelivered() {
        chatService.chatHistory.value = listOf(
            message(ChatMessage.Role.ASSISTANT, ChatMessage.Status.DELIVERED),
            message(ChatMessage.Role.USER, ChatMessage.Status.DELIVERED)
        )

        assertTrue(viewModel().uiState.value.canEndChat)
    }

    @Test
    fun tracksTheChatStartWithItsConfig() {
        viewModel()

        assertEquals(
            listOf<AnalyticsEvent>(
                AnalyticsEvent.ChatStarted(
                    chatModelProvider = "OPEN_AI",
                    chatModel = "gpt-4o-mini",
                    practiceLanguage = "GERMAN",
                    assistantLanguage = "ENGLISH",
                    practiceType = "CONVERSATION"
                )
            ),
            trackedEvents
        )
    }

    @Test
    fun tracksASentMessage() {
        viewModel().sendMessage("Hallo")

        assertEquals(AnalyticsEvent.MessageSent, trackedEvents.last())
    }

    @Test
    fun doesNotTrackABlankMessage() {
        viewModel().sendMessage(" \n\t ")

        assertFalse(trackedEvents.contains(AnalyticsEvent.MessageSent))
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
