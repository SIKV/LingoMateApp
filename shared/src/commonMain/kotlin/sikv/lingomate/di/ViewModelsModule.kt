package sikv.lingomate.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.feature.chat.ChatViewModel
import sikv.lingomate.feature.startchat.StartChatViewModel

val viewModelsModule = module {
    viewModel {
        StartChatViewModel(
            startChatService = get()
        )
    }

    // TODO: ViewModel is not cleared when the screen is closed. Needs to be fixed.
    viewModel { (chatLanguage: ChatLanguage, chatLength: ChatLength, chatModel: ChatModel) ->
        ChatViewModel(
            chatLanguage = chatLanguage,
            chatLength = chatLength,
            chatModel = chatModel,
            chatService = get { parametersOf(chatLanguage, chatLength, chatModel) }
        )
    }
}
