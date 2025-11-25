package sikv.lingomate.di

import org.koin.dsl.module
import sikv.lingomate.feature.startchat.StartChatViewModel

val viewModelsModule = module {
    factory {
        StartChatViewModel(
            startChatService = get()
        )
    }
}
