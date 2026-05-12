package sikv.lingomate.di

import sikv.lingomate.api.apiModule
import sikv.lingomate.data.chat.chatDataModule
import sikv.lingomate.ondevice.llm.onDeviceLLMModule

fun appModule() = listOf(
    apiModule,
    chatDataModule,
    viewModelsModule,
    onDeviceLLMModule(),
)
