package sikv.lingomate.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.feature.chat.ChatViewModel
import sikv.lingomate.feature.startchat.StartChatViewModel

fun initKoinIOS() {
    startKoin {
        modules(appModule())
    }
}

object ViewModels : KoinComponent {
    fun getStartChatViewModel(): StartChatViewModel = get()

    fun getChatViewModel(
        chatLanguage: ChatLanguage,
        chatLength: ChatLength,
        chatModel: ChatModel
    ): ChatViewModel = get { parametersOf(chatLanguage, chatLength, chatModel) }
}
