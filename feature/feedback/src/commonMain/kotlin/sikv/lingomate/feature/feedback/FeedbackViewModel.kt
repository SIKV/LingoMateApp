package sikv.lingomate.feature.feedback

import androidx.lifecycle.ViewModel
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.native.ObjCName

@ObjCName("FeedbackViewModel", exact = true)
class FeedbackViewModel(
    private val feedbackSender: FeedbackSender,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackState.empty())

    @NativeCoroutinesState
    val uiState: StateFlow<FeedbackState> = _uiState.asStateFlow()

    fun send(message: String, email: String) {
        feedbackSender.send(
            message = message.trim(),
            email = email.trim().ifEmpty { null }
        )
        _uiState.update { it.copy(isSent = true) }
    }

    companion object {
        const val MAX_MESSAGE_LENGTH = 2000

        // The longest address the email standard (RFC 5321) allows.
        const val MAX_EMAIL_LENGTH = 254
    }
}
