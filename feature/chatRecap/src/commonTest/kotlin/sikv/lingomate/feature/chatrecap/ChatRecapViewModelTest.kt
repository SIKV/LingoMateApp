package sikv.lingomate.feature.chatrecap

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import sikv.lingomate.analytics.Analytics
import sikv.lingomate.analytics.AnalyticsEvent
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatRecap
import sikv.lingomate.data.chat.service.ChatRecapService
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRecapViewModelTest {

    private val messages = listOf(
        ChatMessage(id = "1", status = ChatMessage.Status.DELIVERED, role = ChatMessage.Role.USER, text = "Hola")
    )

    private val recap = ChatRecap(summary = "You greeted the tutor naturally.")

    private val trackedEvents = mutableListOf<AnalyticsEvent>()

    private fun viewModel(chatRecapService: ChatRecapService) =
        ChatRecapViewModel(messages, chatRecapService, Analytics(trackedEvents::add))

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun recapsTheMessagesItWasGiven() {
        val chatRecapService = FakeChatRecapService(Result.success(recap))

        viewModel(chatRecapService)

        assertEquals(listOf(messages), chatRecapService.requestedMessages)
    }

    @Test
    fun showsTheRecapOnceItArrives() {
        val viewModel = viewModel(FakeChatRecapService(Result.success(recap)))

        assertEquals(ChatRecapState(recap = recap), viewModel.uiState.value)
    }

    @Test
    fun reportsAFailedRecap() {
        val viewModel = viewModel(FakeChatRecapService(Result.failure(Exception())))

        assertEquals(ChatRecapState(hasFailed = true), viewModel.uiState.value)
    }

    @Test
    fun retryReplacesTheFailureWithTheRecap() {
        val chatRecapService = FakeChatRecapService(Result.failure(Exception()))
        val viewModel = viewModel(chatRecapService)

        chatRecapService.result = Result.success(recap)
        viewModel.retry()

        assertEquals(ChatRecapState(recap = recap), viewModel.uiState.value)
    }

    @Test
    fun tracksTheRecapWithTheUserMessageCount() {
        val messages = messages + ChatMessage(
            id = "2",
            status = ChatMessage.Status.DELIVERED,
            role = ChatMessage.Role.ASSISTANT,
            text = "¡Hola!"
        )

        ChatRecapViewModel(messages, FakeChatRecapService(Result.success(recap)), Analytics(trackedEvents::add))

        assertEquals(listOf<AnalyticsEvent>(AnalyticsEvent.ChatRecapOpened(userMessageCount = 1)), trackedEvents)
    }
}

private class FakeChatRecapService(var result: Result<ChatRecap>) : ChatRecapService {

    val requestedMessages = mutableListOf<List<ChatMessage>>()

    override suspend fun getRecap(messages: List<ChatMessage>): Result<ChatRecap> {
        requestedMessages += messages
        return result
    }
}
