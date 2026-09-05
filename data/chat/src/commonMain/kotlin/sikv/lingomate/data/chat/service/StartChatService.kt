package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.mapping.toChatModels
import sikv.lingomate.data.chat.mapping.toLanguages
import sikv.lingomate.data.chat.storage.StartChatSelectionStorage
import sikv.lingomate.data.config.ConfigRepository
import sikv.lingomate.logger.Log
import sikv.lingomate.ondevice.llm.OnDeviceLLM

class StartChatService internal constructor(
    private val configRepository: ConfigRepository,
    private val selectionStorage: StartChatSelectionStorage,
    private val onDeviceLLM: OnDeviceLLM
) {

    suspend fun getChatModels(): List<ChatModel> {
        // TODO: Offer the on-device model once OnDeviceLLM is implemented. It cannot come from
        //  the config: whether it can run depends on the device.

        val chatModels = configRepository.getConfig().toChatModels()

        if (chatModels.isEmpty()) {
            Log.w { "The config lists no chat model this build can use." }
        }

        return chatModels
    }

    suspend fun getSelectedChatModel(): ChatModel? {
        return selectionStorage.chatModel
    }

    fun selectChatModel(chatModel: ChatModel) {
        selectionStorage.chatModel = chatModel
    }

    suspend fun getPracticeLanguages(): List<Language> {
        return configRepository.getConfig()
            .toLanguages()
    }

    suspend fun getSelectedPracticeLanguage(): Language? {
        return selectionStorage.practiceLanguage
    }

    fun selectPracticeLanguage(practiceLanguage: Language) {
        selectionStorage.practiceLanguage = practiceLanguage
    }

    suspend fun getAssistantLanguages(): List<Language> {
        return configRepository.getConfig()
            .toLanguages()
    }

    suspend fun getSelectedAssistantLanguage(): Language? {
        return selectionStorage.assistantLanguage
    }

    fun selectAssistantLanguage(assistantLanguage: Language) {
        selectionStorage.assistantLanguage = assistantLanguage
    }

    suspend fun getPracticeTypes(): List<PracticeType> {
        return PracticeType.entries
    }

    suspend fun getSelectedPracticeType(): PracticeType? {
        return selectionStorage.practiceType
    }

    fun selectPracticeType(practiceType: PracticeType) {
        selectionStorage.practiceType = practiceType
    }
}
