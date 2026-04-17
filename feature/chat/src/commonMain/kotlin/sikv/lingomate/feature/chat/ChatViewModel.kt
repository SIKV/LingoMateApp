package sikv.lingomate.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sikv.lingomate.data.chat.service.ChatService
import kotlin.native.ObjCName

@ObjCName("ChatViewModel", exact = true)
class ChatViewModel(
    private val chatService: ChatService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatState.empty())

    @NativeCoroutinesState
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            chatService.chatHistory
                .collect { messageList ->
                    _uiState.update {
                        it.copy(messages = messageList)
                    }
                }
        }

        chatService.startChat(viewModelScope)
    }

    fun sendMessage(message: String) {
        chatService.sendMessage(message, viewModelScope)
    }
}
