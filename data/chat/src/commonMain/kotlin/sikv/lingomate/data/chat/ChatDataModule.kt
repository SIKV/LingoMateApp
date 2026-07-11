package sikv.lingomate.data.chat

import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.service.ChatService
import sikv.lingomate.data.chat.service.OnDeviceChatService
import sikv.lingomate.data.chat.service.OpenAIChatService
import sikv.lingomate.data.chat.service.PromptBuilder
import sikv.lingomate.data.chat.service.StartChatService

val chatDataModule = module {
    single {
        StartChatService(
            onDeviceLLM = get()
        )
    }

    factory<ChatService> { (chatConfig: ChatConfig) ->
        when (chatConfig.chatModel.provider) {
            ChatModelProvider.ON_DEVICE -> {
                OnDeviceChatService(
                    chatConfig = chatConfig,
                    onDeviceLLM = get(),
                    promptBuilder = get()
                )
            }
            ChatModelProvider.OPEN_AI -> {
                OpenAIChatService(
                    chatConfig = chatConfig,
                    openAIApi = get(),
                    promptBuilder = get()
                )
            }
        }
    }

    single { PromptBuilder() }
}
