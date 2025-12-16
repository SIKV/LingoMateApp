package sikv.lingomate.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sikv.lingomate.data.chat.service.ChatService
import kotlin.native.ObjCName

@ObjCName("ChatViewModel", exact = true)
class ChatViewModel(
    private val chatService: ChatService
) : ViewModel() {

    init {
        viewModelScope.launch {
            chatService.startNewChat()
                .collect {
                    // TODO: Implement.
                }
        }
    }
}
