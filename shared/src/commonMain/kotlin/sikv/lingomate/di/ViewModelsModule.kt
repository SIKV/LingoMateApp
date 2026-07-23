package sikv.lingomate.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.feature.chat.ChatViewModel
import sikv.lingomate.feature.manageapikeys.ManageApiKeysViewModel
import sikv.lingomate.feature.startchat.StartChatViewModel

val viewModelsModule = module {
    viewModel {
        StartChatViewModel(
            startChatService = get()
        )
    }

    viewModel {
        ManageApiKeysViewModel(
            apiKeyStorage = get()
        )
    }

    viewModel { (chatConfig: ChatConfig) ->
        ChatViewModel(
            chatService = get { parametersOf(chatConfig) }
        )
    }
}
