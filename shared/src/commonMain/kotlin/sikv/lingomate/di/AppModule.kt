package sikv.lingomate.di

import sikv.lingomate.api.config.configApiModule
import sikv.lingomate.api.openai.openaiApiModule
import sikv.lingomate.data.apikeystorage.apiKeyStorageModule
import sikv.lingomate.data.chat.chatDataModule
import sikv.lingomate.data.config.configDataModule
import sikv.lingomate.ondevice.llm.onDeviceLLMModule

fun appModule() = listOf(
    configApiModule,
    openaiApiModule,
    chatDataModule,
    configDataModule,
    apiKeyStorageModule(),
    apiKeyProviderModule,
    viewModelsModule,
    onDeviceLLMModule(),
)
