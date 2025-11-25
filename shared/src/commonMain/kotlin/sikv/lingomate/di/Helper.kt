package sikv.lingomate.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import sikv.lingomate.feature.startchat.StartChatViewModel

fun initKoinIOS() {
    startKoin {
        modules(appModule())
    }
}

object ViewModels : KoinComponent {
    fun getStartChatViewModel(): StartChatViewModel = get()
}
