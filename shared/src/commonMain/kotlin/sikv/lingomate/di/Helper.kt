package sikv.lingomate.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.feature.chat.ChatViewModel
import sikv.lingomate.feature.chatrecap.ChatRecapViewModel
import sikv.lingomate.feature.feedback.FeedbackViewModel
import sikv.lingomate.feature.manageapikeys.ManageApiKeysViewModel
import sikv.lingomate.feature.startchat.StartChatViewModel

fun initKoinIOS() {
    startKoin {
        modules(appModule())
    }
}

object ViewModels : KoinComponent {
    fun getStartChatViewModel(): StartChatViewModel = get()

    fun getChatViewModel(
        chatConfig: ChatConfig
    ): ChatViewModel = get { parametersOf(chatConfig) }

    fun getChatRecapViewModel(
        chatConfig: ChatConfig,
        messages: List<ChatMessage>
    ): ChatRecapViewModel = get { parametersOf(chatConfig, messages) }

    fun getManageApiKeysViewModel(): ManageApiKeysViewModel = get()

    fun getFeedbackViewModel(): FeedbackViewModel = get()
}
