package sikv.lingomate.di

import sikv.lingomate.data.chat.chatDataModule

fun appModule() = listOf(chatDataModule, viewModelsModule)
