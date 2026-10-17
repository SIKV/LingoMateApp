package sikv.lingomate.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.feature.chat.ChatViewModel
import sikv.lingomate.feature.chatrecap.ChatRecapViewModel
import sikv.lingomate.feature.feedback.FeedbackViewModel
import sikv.lingomate.feature.manageapikeys.ManageApiKeysViewModel
import sikv.lingomate.feature.startchat.StartChatViewModel

val viewModelsModule = module {
    viewModel {
        StartChatViewModel(
            startChatService = get(),
            apiKeyStorage = get()
        )
    }

    viewModel {
        ManageApiKeysViewModel(
            apiKeyStorage = get()
        )
    }

    viewModel {
        FeedbackViewModel(
            feedbackSender = get()
        )
    }

    viewModel { (chatConfig: ChatConfig) ->
        ChatViewModel(
            chatService = get { parametersOf(chatConfig) }
        )
    }

    viewModel { (chatConfig: ChatConfig, messages: List<ChatMessage>) ->
        ChatRecapViewModel(
            messages = messages,
            chatRecapService = get { parametersOf(chatConfig) }
        )
    }
}
