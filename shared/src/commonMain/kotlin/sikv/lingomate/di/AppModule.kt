package sikv.lingomate.di

import sikv.lingomate.api.apiModule
import sikv.lingomate.data.chat.chatDataModule

fun appModule() = listOf(apiModule, chatDataModule, viewModelsModule)
