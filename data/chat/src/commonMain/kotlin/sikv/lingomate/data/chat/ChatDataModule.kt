package sikv.lingomate.data.chat

import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.service.ChatService
import sikv.lingomate.data.chat.service.PromptBuilder
import sikv.lingomate.data.chat.service.StartChatService

val chatDataModule = module {
    single { StartChatService() }

    factory { (chatLanguage: ChatLanguage, chatModel: ChatModel) ->
        ChatService(
            chatLanguage = chatLanguage,
            chatModel = chatModel,
            openAIApi = get(),
            promptBuilder = get()
        )
    }

    single { PromptBuilder() }
}
