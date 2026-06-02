package sikv.lingomate.data.chat

import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.service.ChatService
import sikv.lingomate.data.chat.service.OnDeviceChatService
import sikv.lingomate.data.chat.service.OpenAIChatService
import sikv.lingomate.data.chat.service.PromptBuilder
import sikv.lingomate.data.chat.service.StartChatService

val chatDataModule = module {
    single { StartChatService() }

    factory<ChatService> { (chatLanguage: ChatLanguage, chatModel: ChatModel) ->
        when (chatModel) {
            ChatModel.ON_DEVICE -> {
                OnDeviceChatService(
                    chatLanguage = chatLanguage,
                    onDeviceLLM = get(),
                    promptBuilder = get()
                )
            }
            ChatModel.GPT_5_NANO,
            ChatModel.GPT_5_MINI,
            ChatModel.GPT_5_1 -> {
                OpenAIChatService(
                    openAIApi = get(),
                    chatLanguage = chatLanguage,
                    chatModel = chatModel,
                    promptBuilder = get()
                )
            }
        }
    }

    single { PromptBuilder() }
}
