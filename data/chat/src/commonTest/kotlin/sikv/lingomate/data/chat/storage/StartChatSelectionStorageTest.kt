package sikv.lingomate.data.chat.storage

import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StartChatSelectionStorageTest {

    private lateinit var keyValueStorage: FakeKeyValueStorage
    private lateinit var selectionStorage: StartChatSelectionStorage

    @BeforeTest
    fun setUp() {
        keyValueStorage = FakeKeyValueStorage()
        selectionStorage = StartChatSelectionStorage(keyValueStorage)
    }

    @Test
    fun selections_areNullWhenNothingStored() {
        assertNull(selectionStorage.chatModel)
        assertNull(selectionStorage.practiceLanguage)
        assertNull(selectionStorage.assistantLanguage)
        assertNull(selectionStorage.practiceType)
    }

    @Test
    fun selections_surviveANewInstance() {
        val chatModel = ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-5-mini")

        selectionStorage.chatModel = chatModel
        selectionStorage.practiceLanguage = Language.GERMAN
        selectionStorage.assistantLanguage = Language.ENGLISH
        selectionStorage.practiceType = PracticeType.TRANSLATION

        val restored = StartChatSelectionStorage(keyValueStorage)

        assertEquals(chatModel, restored.chatModel)
        assertEquals(Language.GERMAN, restored.practiceLanguage)
        assertEquals(Language.ENGLISH, restored.assistantLanguage)
        assertEquals(PracticeType.TRANSLATION, restored.practiceType)
    }

    @Test
    fun selections_overwriteThePreviousValue() {
        selectionStorage.practiceLanguage = Language.GERMAN
        selectionStorage.practiceLanguage = Language.SPANISH

        assertEquals(Language.SPANISH, selectionStorage.practiceLanguage)
    }

    @Test
    fun selections_areDroppedWhenSetToNull() {
        selectionStorage.chatModel = ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-5-mini")
        selectionStorage.practiceType = PracticeType.CONVERSATION

        selectionStorage.chatModel = null
        selectionStorage.practiceType = null

        assertNull(selectionStorage.chatModel)
        assertNull(selectionStorage.practiceType)
        assertEquals(0, keyValueStorage.entries.size)
    }

    @Test
    fun chatModel_isStoredByProviderNameAndModel() {
        selectionStorage.chatModel = ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-5-mini")

        assertEquals(
            mapOf(
                "start_chat.chat_model_provider" to "OPEN_AI",
                "start_chat.chat_model" to "gpt-5-mini"
            ),
            keyValueStorage.entries
        )
    }

    @Test
    fun chatModel_isNullWhenOnlyPartOfItWasStored() {
        keyValueStorage.put("start_chat.chat_model_provider", "OPEN_AI")

        assertNull(selectionStorage.chatModel)

        keyValueStorage.clear()
        keyValueStorage.put("start_chat.chat_model", "gpt-5-mini")

        assertNull(selectionStorage.chatModel)
    }

    @Test
    fun selections_areNullWhenTheStoredNameIsNoLongerKnown() {
        keyValueStorage.put("start_chat.chat_model_provider", "GONE_PROVIDER")
        keyValueStorage.put("start_chat.chat_model", "gpt-5-mini")
        keyValueStorage.put("start_chat.practice_language", "KLINGON")
        keyValueStorage.put("start_chat.practice_type", "DICTATION")

        assertNull(selectionStorage.chatModel)
        assertNull(selectionStorage.practiceLanguage)
        assertNull(selectionStorage.practiceType)
    }
}
