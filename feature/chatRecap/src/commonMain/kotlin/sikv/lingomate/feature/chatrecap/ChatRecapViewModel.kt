package sikv.lingomate.feature.chatrecap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.service.ChatRecapService
import kotlin.native.ObjCName

@ObjCName("ChatRecapViewModel", exact = true)
class ChatRecapViewModel(
    private val messages: List<ChatMessage>,
    private val chatRecapService: ChatRecapService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatRecapState.empty())

    @NativeCoroutinesState
    val uiState: StateFlow<ChatRecapState> = _uiState.asStateFlow()

    init {
        loadRecap()
    }

    fun retry() {
        loadRecap()
    }

    private fun loadRecap() {
        _uiState.update { it.copy(hasFailed = false) }

        viewModelScope.launch {
            chatRecapService.getRecap(messages).fold(
                onSuccess = { recap ->
                    _uiState.update { it.copy(recap = recap) }
                },
                onFailure = {
                    _uiState.update { it.copy(hasFailed = true) }
                }
            )
        }
    }
}
