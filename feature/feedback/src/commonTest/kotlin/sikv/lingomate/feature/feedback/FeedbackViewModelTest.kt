package sikv.lingomate.feature.feedback

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeedbackViewModelTest {

    @Test
    fun startsUnsent() {
        val viewModel = FeedbackViewModel(FakeFeedbackSender())

        assertFalse(viewModel.uiState.value.isSent)
    }

    @Test
    fun sendsTheTrimmedMessageAndEmail() {
        val feedbackSender = FakeFeedbackSender()
        val viewModel = FeedbackViewModel(feedbackSender)

        viewModel.send(message = "  The app froze.\n", email = " me@example.com ")

        assertEquals(
            listOf(FakeFeedbackSender.Feedback(message = "The app froze.", email = "me@example.com")),
            feedbackSender.sent
        )
    }

    @Test
    fun sendsNoEmailWhenItIsBlank() {
        val feedbackSender = FakeFeedbackSender()
        val viewModel = FeedbackViewModel(feedbackSender)

        viewModel.send(message = "The app froze.", email = "  ")

        assertEquals(
            listOf(FakeFeedbackSender.Feedback(message = "The app froze.", email = null)),
            feedbackSender.sent
        )
    }

    @Test
    fun marksTheFeedbackAsSent() {
        val viewModel = FeedbackViewModel(FakeFeedbackSender())

        viewModel.send(message = "The app froze.", email = "")

        assertTrue(viewModel.uiState.value.isSent)
    }
}

private class FakeFeedbackSender : FeedbackSender {

    data class Feedback(val message: String, val email: String?)

    val sent = mutableListOf<Feedback>()

    override fun send(message: String, email: String?) {
        sent += Feedback(message, email)
    }
}
