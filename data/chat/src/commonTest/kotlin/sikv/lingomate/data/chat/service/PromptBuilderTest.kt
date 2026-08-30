package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.AssistantLanguage
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.PracticeLanguage
import sikv.lingomate.data.chat.domain.PracticeType
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PromptBuilderTest {

    private val promptBuilder = PromptBuilder()

    @Test
    fun `prompt names both the practice and the assistant language`() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(PracticeLanguage.SPANISH, PracticeType.CONVERSATION)
        )

        assertContains(prompt, "learning Spanish")
        assertContains(prompt, "English is the language the user already knows")
    }

    @Test
    fun `conversation prompt asks for a conversation and not for translations`() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(PracticeLanguage.SPANISH, PracticeType.CONVERSATION)
        )

        assertContains(prompt, "Session type: conversation practice.")
        assertFalse(prompt.contains("Session type: translation practice."))
    }

    @Test
    fun `translation prompt asks for one sentence at a time`() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(PracticeLanguage.SPANISH, PracticeType.TRANSLATION)
        )

        assertContains(prompt, "Session type: translation practice.")
        assertContains(prompt, "one sentence in English at a time")
        assertContains(prompt, "translate it into Spanish")
    }

    @Test
    fun `prompt tells the model to open the session when there are no user messages`() {
        PracticeType.entries.forEach { practiceType ->
            val prompt = promptBuilder.buildSystemPrompt(
                chatConfig(PracticeLanguage.ENGLISH, practiceType)
            )

            assertContains(prompt, "If there are no user messages yet, start the session yourself")
        }
    }

    @Test
    fun `conversation prompt pins one situation for the whole session`() {
        val prompt = PromptBuilder(Random(42)).buildSystemPrompt(
            chatConfig(PracticeLanguage.SPANISH, PracticeType.CONVERSATION)
        )

        assertContains(prompt, "The situation for this session is: you are ")
        assertContains(prompt, "Your manner:")
        assertContains(prompt, "Do not replace this situation with another one")
    }

    @Test
    fun `conversation situations are combined from independent axes`() {
        val situations = (1..40).map { seed ->
            PromptBuilder(Random(seed)).buildSystemPrompt(
                chatConfig(PracticeLanguage.SPANISH, PracticeType.CONVERSATION)
            ).situationLine()
        }

        // Combining three axes has to give many more distinct situations than a single fixed list of scenes would.
        assertTrue(
            situations.distinct().size >= 35,
            "Expected nearly all situations to differ, got ${situations.distinct().size} distinct out of ${situations.size}."
        )
    }

    @Test
    fun `translation prompt pins a theme and a focus and a style`() {
        val prompt = PromptBuilder(Random(42)).buildSystemPrompt(
            chatConfig(PracticeLanguage.SPANISH, PracticeType.TRANSLATION)
        )

        assertContains(prompt, "Draw this session's sentences from ")
        assertContains(prompt, "and make them practise ")
        assertContains(prompt, "Write them as ")
        assertContains(prompt, "Keep to this theme, focus and style for the whole session.")
        assertFalse(prompt.contains("The situation for this session is:"))
    }

    @Test
    fun `translation sessions do not all get the same brief`() {
        val briefs = (1..40).map { seed ->
            PromptBuilder(Random(seed)).buildSystemPrompt(
                chatConfig(PracticeLanguage.SPANISH, PracticeType.TRANSLATION)
            ).briefLine()
        }

        assertTrue(
            briefs.distinct().size >= 35,
            "Expected nearly all briefs to differ, got ${briefs.distinct().size} distinct out of ${briefs.size}."
        )
    }

    @Test
    fun `conversation prompt does not pick a translation brief`() {
        val prompt = PromptBuilder(Random(42)).buildSystemPrompt(
            chatConfig(PracticeLanguage.SPANISH, PracticeType.CONVERSATION)
        )

        assertFalse(prompt.contains("Draw this session's sentences from"))
    }

    @Test
    fun `every configuration produces a non blank prompt`() {
        PracticeLanguage.entries.forEach { practiceLanguage ->
            PracticeType.entries.forEach { practiceType ->
                val prompt = promptBuilder.buildSystemPrompt(chatConfig(practiceLanguage, practiceType))

                assertTrue(prompt.isNotBlank(), "Prompt for $practiceLanguage/$practiceType is blank.")
            }
        }
    }

    private fun String.situationLine(): String {
        return lines().first { it.contains("The situation for this session is:") }
    }

    private fun String.briefLine(): String {
        return lines().first { it.contains("Draw this session's sentences from") }
    }

    private fun chatConfig(
        practiceLanguage: PracticeLanguage,
        practiceType: PracticeType,
        assistantLanguage: AssistantLanguage = AssistantLanguage.ENGLISH
    ): ChatConfig {
        return ChatConfig(
            chatModel = ChatModel(ChatModelProvider.OPEN_AI, "gpt-5-mini"),
            practiceLanguage = practiceLanguage,
            assistantLanguage = assistantLanguage,
            practiceType = practiceType
        )
    }
}
