package sikv.lingomate.data.chat.storage

import kotlinx.coroutines.test.runTest
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
    fun selections_areNullWhenNothingStored() = runTest {
        assertNull(selectionStorage.getChatModel())
        assertNull(selectionStorage.getPracticeLanguage())
        assertNull(selectionStorage.getAssistantLanguage())
        assertNull(selectionStorage.getPracticeType())
    }

    @Test
    fun selections_surviveANewInstance() = runTest {
        val chatModel = ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-5-mini")

        selectionStorage.setChatModel(chatModel)
        selectionStorage.setPracticeLanguage(Language.GERMAN)
        selectionStorage.setAssistantLanguage(Language.ENGLISH)
        selectionStorage.setPracticeType(PracticeType.TRANSLATION)

        val restored = StartChatSelectionStorage(keyValueStorage)

        assertEquals(chatModel, restored.getChatModel())
        assertEquals(Language.GERMAN, restored.getPracticeLanguage())
        assertEquals(Language.ENGLISH, restored.getAssistantLanguage())
        assertEquals(PracticeType.TRANSLATION, restored.getPracticeType())
    }

    @Test
    fun selections_overwriteThePreviousValue() = runTest {
        selectionStorage.setPracticeLanguage(Language.GERMAN)
        selectionStorage.setPracticeLanguage(Language.SPANISH)

        assertEquals(Language.SPANISH, selectionStorage.getPracticeLanguage())
    }

    @Test
    fun selections_areDroppedWhenSetToNull() = runTest {
        selectionStorage.setChatModel(ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-5-mini"))
        selectionStorage.setPracticeType(PracticeType.CONVERSATION)

        selectionStorage.setChatModel(null)
        selectionStorage.setPracticeType(null)

        assertNull(selectionStorage.getChatModel())
        assertNull(selectionStorage.getPracticeType())
        assertEquals(0, keyValueStorage.entries.size)
    }

    @Test
    fun chatModel_isStoredByProviderNameAndModel() = runTest {
        selectionStorage.setChatModel(ChatModel(provider = ChatModelProvider.OPEN_AI, model = "gpt-5-mini"))

        assertEquals(
            mapOf(
                "start_chat.chat_model_provider" to "OPEN_AI",
                "start_chat.chat_model" to "gpt-5-mini"
            ),
            keyValueStorage.entries
        )
    }

    @Test
    fun chatModel_isNullWhenOnlyPartOfItWasStored() = runTest {
        keyValueStorage.put("start_chat.chat_model_provider", "OPEN_AI")

        assertNull(selectionStorage.getChatModel())

        keyValueStorage.clear()
        keyValueStorage.put("start_chat.chat_model", "gpt-5-mini")

        assertNull(selectionStorage.getChatModel())
    }

    @Test
    fun selections_areNullWhenTheStoredNameIsNoLongerKnown() = runTest {
        keyValueStorage.put("start_chat.chat_model_provider", "GONE_PROVIDER")
        keyValueStorage.put("start_chat.chat_model", "gpt-5-mini")
        keyValueStorage.put("start_chat.practice_language", "KLINGON")
        keyValueStorage.put("start_chat.practice_type", "DICTATION")

        assertNull(selectionStorage.getChatModel())
        assertNull(selectionStorage.getPracticeLanguage())
        assertNull(selectionStorage.getPracticeType())
    }
}
