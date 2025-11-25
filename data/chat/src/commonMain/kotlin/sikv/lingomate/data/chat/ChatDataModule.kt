package sikv.lingomate.data.chat

import org.koin.dsl.module
import sikv.lingomate.data.chat.service.StartChatService

val chatDataModule = module {
    single { StartChatService() }
}
