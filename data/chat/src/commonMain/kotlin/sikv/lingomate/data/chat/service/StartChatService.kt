package sikv.lingomate.data.chat.service

import sikv.lingomate.domain.chat.ChatModel
import sikv.lingomate.domain.chat.ChatModelProvider
import sikv.lingomate.domain.chat.PracticeLanguage
import sikv.lingomate.domain.chat.PracticeType
import sikv.lingomate.domain.chat.AssistantLanguage
import sikv.lingomate.ondevice.llm.OnDeviceLLM

class StartChatService(
    private val onDeviceLLM: OnDeviceLLM
) {

    // TODO: Implement persistent storage.

    private var selectedChatModel: ChatModel? = null
    private var selectedPracticeLanguage: PracticeLanguage? = null
    private var selectedAssistantLanguage: AssistantLanguage? = null
    private var selectedPracticeType: PracticeType? = null

    suspend fun getChatModels(): List<ChatModel> {
        // TODO: Refactor. Current implementation is for testing only.
        val openAIModels = listOf(
            ChatModel(
                ChatModelProvider.OPEN_AI,
                "gpt-5-nano"
            ),
            ChatModel(
                ChatModelProvider.OPEN_AI,
                "gpt-5-mini"
            ),
            ChatModel(
                ChatModelProvider.OPEN_AI,
                "gpt-5.1"
            )
        )
        if (onDeviceLLM.checkAvailability()) {
            val onDeviceModel = ChatModel(
                ChatModelProvider.ON_DEVICE,
                "on-device"
            )
            return openAIModels + onDeviceModel
        } else {
            return openAIModels
        }
    }

    suspend fun getSelectedChatModel(): ChatModel? {
        return selectedChatModel
    }

    fun selectChatModel(chatModel: ChatModel) {
        this.selectedChatModel = chatModel
    }

    suspend fun getPracticeLanguages(): List<PracticeLanguage> {
        return PracticeLanguage.entries
    }

    suspend fun getSelectedPracticeLanguage(): PracticeLanguage? {
        return selectedPracticeLanguage
    }

    fun selectPracticeLanguage(practiceLanguage: PracticeLanguage) {
        this.selectedPracticeLanguage = practiceLanguage
    }

    suspend fun getAssistantLanguages(): List<AssistantLanguage> {
        return AssistantLanguage.entries
    }

    suspend fun getSelectedAssistantLanguage(): AssistantLanguage? {
        return selectedAssistantLanguage
    }

    fun selectAssistantLanguage(assistantLanguage: AssistantLanguage) {
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
