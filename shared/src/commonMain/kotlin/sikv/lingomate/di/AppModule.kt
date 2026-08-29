package sikv.lingomate.di

import sikv.lingomate.api.openai.openaiApiModule
import sikv.lingomate.data.apikeystorage.apiKeyStorageModule
import sikv.lingomate.data.chat.chatDataModule
import sikv.lingomate.ondevice.llm.onDeviceLLMModule

fun appModule() = listOf(
    openaiApiModule,
    chatDataModule,
    apiKeyStorageModule(),
    viewModelsModule,
    onDeviceLLMModule(),
)
