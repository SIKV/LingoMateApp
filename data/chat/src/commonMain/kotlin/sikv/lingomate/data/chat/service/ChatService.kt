package sikv.lingomate.data.chat.service

import sikv.lingomate.api.OpenAIApi
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel

class ChatService(
    private val chatLanguage: ChatLanguage,
    private val chatLength: ChatLength,
    private val chatModel: ChatModel,
    private val openAIApi: OpenAIApi
) {
    // TODO: Implement.
}
