package sikv.lingomate.data.chat

import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.service.ChatService
import sikv.lingomate.data.chat.service.StartChatService

val chatDataModule = module {
    single { StartChatService() }

    // TODO: Consider using [scope] instead of [factory]
    factory { (chatLanguage: ChatLanguage, chatLength: ChatLength, chatModel: ChatModel) ->
        ChatService(
            chatLanguage = chatLanguage,
            chatLength = chatLength,
            chatModel = chatModel,
            openAIApi = get()
        )
    }
}
