package sikv.lingomate.feature.chat

import androidx.lifecycle.ViewModel
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.service.ChatService
import kotlin.native.ObjCName

@ObjCName("ChatViewModel", exact = true)
class ChatViewModel(
    private val chatLanguage: ChatLanguage,
    private val chatLength: ChatLength,
    private val chatModel: ChatModel,
    private val chatService: ChatService
) : ViewModel() {

}