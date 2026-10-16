package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RecapPromptBuilderTest {

    private val promptBuilder = RecapPromptBuilder()

    @Test
    fun recapPromptNamesPracticeAndExplanationLanguage() {
        val prompt = promptBuilder.buildRecapPrompt(
            chatConfig(Language.SPANISH, PracticeType.CONVERSATION, Language.POLISH)
        )

        assertContains(prompt, "learning Spanish")
        assertContains(prompt, "two or three short sentences in Polish")
    }

    @Test
    fun sameLanguageRecapExplainsInSimplePracticeLanguage() {
        val prompt = promptBuilder.buildRecapPrompt(
            chatConfig(Language.GERMAN, PracticeType.TRANSLATION, Language.GERMAN)
        )

        assertContains(prompt, "two or three short sentences in simple German")
    }

    @Test
    fun recapTranscriptLabelsWhoSaidWhat() {
        val transcript = promptBuilder.buildRecapTranscript(
            listOf(
                message(ChatMessage.Role.ASSISTANT, "¿Qué quieres tomar?"),
                message(ChatMessage.Role.USER, "Un café, por favor.")
            )
        )

        assertEquals("Tutor: ¿Qué quieres tomar?\n\nUser: Un café, por favor.", transcript)
    }

    @Test
    fun recapTranscriptLeavesOutMessagesTheModelNeverSaw() {
        val transcript = promptBuilder.buildRecapTranscript(
            listOf(
                message(ChatMessage.Role.SYSTEM, "You are LingoMate."),
                message(ChatMessage.Role.ASSISTANT, "¿Qué quieres tomar?"),
                message(ChatMessage.Role.USER, "Un té.", ChatMessage.Status.FAILED),
                message(ChatMessage.Role.USER, "Un café.", ChatMessage.Status.IN_PROGRESS)
            )
        )

        assertEquals("Tutor: ¿Qué quieres tomar?", transcript)
    }

    private fun message(
        role: ChatMessage.Role,
        text: String,
        status: ChatMessage.Status = ChatMessage.Status.DELIVERED
    ): ChatMessage {
        return ChatMessage(id = text, status = status, role = role, text = text)
    }

    private fun chatConfig(
        practiceLanguage: Language,
        practiceType: PracticeType,
        assistantLanguage: Language = Language.ENGLISH
    ): ChatConfig {
        return ChatConfig(
            chatModel = ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-mini"),
            practiceLanguage = practiceLanguage,
            assistantLanguage = assistantLanguage,
            practiceType = practiceType
        )
    }
}
