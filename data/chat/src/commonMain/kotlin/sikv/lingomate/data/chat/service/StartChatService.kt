package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.chat.mapping.toChatModels
import sikv.lingomate.data.chat.mapping.toLanguages
import sikv.lingomate.data.config.ConfigRepository
import sikv.lingomate.logger.Log
import sikv.lingomate.ondevice.llm.OnDeviceLLM

class StartChatService(
    private val configRepository: ConfigRepository,
    private val onDeviceLLM: OnDeviceLLM
) {

    // TODO: Implement persistent storage.

    private var selectedChatModel: ChatModel? = null
    private var selectedPracticeLanguage: Language? = null
    private var selectedAssistantLanguage: Language? = null
    private var selectedPracticeType: PracticeType? = null

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
        return selectedChatModel
    }

    fun selectChatModel(chatModel: ChatModel) {
        this.selectedChatModel = chatModel
    }

    suspend fun getPracticeLanguages(): List<Language> {
        return configRepository.getConfig()
            .toLanguages()
    }

    suspend fun getSelectedPracticeLanguage(): Language? {
        return selectedPracticeLanguage
    }

    fun selectPracticeLanguage(practiceLanguage: Language) {
        this.selectedPracticeLanguage = practiceLanguage
    }

    suspend fun getAssistantLanguages(): List<Language> {
        return configRepository.getConfig()
            .toLanguages()
    }

    suspend fun getSelectedAssistantLanguage(): Language? {
        return selectedAssistantLanguage
    }

    fun selectAssistantLanguage(assistantLanguage: Language) {
        this.selectedAssistantLanguage = assistantLanguage
    }

    suspend fun getPracticeTypes(): List<PracticeType> {
        return PracticeType.entries
    }

    suspend fun getSelectedPracticeType(): PracticeType? {
        return selectedPracticeType
    }

    fun selectPracticeType(practiceType: PracticeType) {
        this.selectedPracticeType = practiceType
    }
}
