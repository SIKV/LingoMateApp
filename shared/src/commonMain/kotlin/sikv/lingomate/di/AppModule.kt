package sikv.lingomate.di

import sikv.lingomate.api.remoteconfig.remoteConfigApiModule
import sikv.lingomate.api.openai.openaiApiModule
import sikv.lingomate.data.apikeystorage.apiKeyStorageModule
import sikv.lingomate.data.chat.chatDataModule
import sikv.lingomate.data.config.configDataModule
import sikv.lingomate.data.keyvaluestorage.keyValueStorageModule
import sikv.lingomate.ondevice.llm.onDeviceLLMModule

fun appModule() = listOf(
    remoteConfigApiModule,
    openaiApiModule,
    chatDataModule,
    configDataModule,
    apiKeyStorageModule(),
    keyValueStorageModule(),
    apiKeyProviderModule,
    viewModelsModule,
    onDeviceLLMModule(),
)
