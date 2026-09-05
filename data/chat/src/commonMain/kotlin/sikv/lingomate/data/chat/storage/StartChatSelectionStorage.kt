package sikv.lingomate.data.chat.storage

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.keyvaluestorage.KeyValueStorage
import sikv.lingomate.data.keyvaluestorage.getEnum
import sikv.lingomate.data.keyvaluestorage.putEnum
import sikv.lingomate.data.keyvaluestorage.putOrRemove

/**
 * Persists what the user picked on the start chat screen, so the next launch opens on the
 * same selection.
 */
internal class StartChatSelectionStorage(
    private val keyValueStorage: KeyValueStorage,
) {

    suspend fun getChatModel(): ChatModel? {
        val provider = keyValueStorage.getEnum<ChatModelProvider>(KEY_CHAT_MODEL_PROVIDER)
            ?: return null
        val model = keyValueStorage.get(KEY_CHAT_MODEL) ?: return null

        return ChatModel(
            provider = provider,
            model = model
        )
    }

    suspend fun setChatModel(chatModel: ChatModel?) {
        keyValueStorage.putEnum(KEY_CHAT_MODEL_PROVIDER, chatModel?.provider)
        keyValueStorage.putOrRemove(KEY_CHAT_MODEL, chatModel?.model)
    }

    suspend fun getPracticeLanguage(): Language? {
        return keyValueStorage.getEnum<Language>(KEY_PRACTICE_LANGUAGE)
    }

    suspend fun setPracticeLanguage(practiceLanguage: Language?) {
        keyValueStorage.putEnum(KEY_PRACTICE_LANGUAGE, practiceLanguage)
    }

    suspend fun getAssistantLanguage(): Language? {
        return keyValueStorage.getEnum<Language>(KEY_ASSISTANT_LANGUAGE)
    }

    suspend fun setAssistantLanguage(assistantLanguage: Language?) {
        keyValueStorage.putEnum(KEY_ASSISTANT_LANGUAGE, assistantLanguage)
    }

    suspend fun getPracticeType(): PracticeType? {
        return keyValueStorage.getEnum<PracticeType>(KEY_PRACTICE_TYPE)
    }

    suspend fun setPracticeType(practiceType: PracticeType?) {
        keyValueStorage.putEnum(KEY_PRACTICE_TYPE, practiceType)
    }

    private companion object {
        const val KEY_CHAT_MODEL_PROVIDER = "start_chat.chat_model_provider"
        const val KEY_CHAT_MODEL = "start_chat.chat_model"
        const val KEY_PRACTICE_LANGUAGE = "start_chat.practice_language"
        const val KEY_ASSISTANT_LANGUAGE = "start_chat.assistant_language"
        const val KEY_PRACTICE_TYPE = "start_chat.practice_type"
    }
}
