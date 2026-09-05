package sikv.lingomate.data.chat.storage

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.data.keyvaluestorage.KeyValueStorage
import sikv.lingomate.logger.Log

/**
 * Persists what the user picked on the start chat screen, so the next launch opens on the
 * same selection.
 *
 * Values are stored by enum name rather than ordinal: reordering the enums keeps stored
 * selections readable, and a name that no longer exists reads back as null instead of
 * resolving to an unrelated entry.
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
        keyValueStorage.putOrRemove(KEY_CHAT_MODEL_PROVIDER, chatModel?.provider?.name)
        keyValueStorage.putOrRemove(KEY_CHAT_MODEL, chatModel?.model)
    }

    suspend fun getPracticeLanguage(): Language? {
        return keyValueStorage.getEnum<Language>(KEY_PRACTICE_LANGUAGE)
    }

    suspend fun setPracticeLanguage(practiceLanguage: Language?) {
        keyValueStorage.putOrRemove(KEY_PRACTICE_LANGUAGE, practiceLanguage?.name)
    }

    suspend fun getAssistantLanguage(): Language? {
        return keyValueStorage.getEnum<Language>(KEY_ASSISTANT_LANGUAGE)
    }

    suspend fun setAssistantLanguage(assistantLanguage: Language?) {
        keyValueStorage.putOrRemove(KEY_ASSISTANT_LANGUAGE, assistantLanguage?.name)
    }

    suspend fun getPracticeType(): PracticeType? {
        return keyValueStorage.getEnum<PracticeType>(KEY_PRACTICE_TYPE)
    }

    suspend fun setPracticeType(practiceType: PracticeType?) {
        keyValueStorage.putOrRemove(KEY_PRACTICE_TYPE, practiceType?.name)
    }

    private companion object {
        const val KEY_CHAT_MODEL_PROVIDER = "start_chat.chat_model_provider"
        const val KEY_CHAT_MODEL = "start_chat.chat_model"
        const val KEY_PRACTICE_LANGUAGE = "start_chat.practice_language"
        const val KEY_ASSISTANT_LANGUAGE = "start_chat.assistant_language"
        const val KEY_PRACTICE_TYPE = "start_chat.practice_type"
    }
}

/** Stores [value], or drops the entry when it is null. */
private suspend fun KeyValueStorage.putOrRemove(key: String, value: String?) {
    if (value == null) {
        remove(key)
    } else {
        put(key, value)
    }
}

/** Reads the entry as a [T], returning null when it is missing or no longer a known entry. */
private suspend inline fun <reified T : Enum<T>> KeyValueStorage.getEnum(key: String): T? {
    val name = get(key) ?: return null

    return enumValues<T>().firstOrNull { it.name == name }
        ?: run {
            Log.w { "Dropping the stored value of $key: $name is no longer a known entry." }
            null
        }
}
