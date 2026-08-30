package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatModelProvider
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PromptBuilderTest {

    private val promptBuilder = PromptBuilder()

    @Test
    fun promptNamesPracticeAndAssistantLanguage() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(Language.SPANISH, PracticeType.CONVERSATION)
        )

        assertContains(prompt, "learning Spanish")
        assertContains(prompt, "English is the language the user already knows")
    }

    @Test
    fun conversationPromptAsksForConversationNotTranslations() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(Language.SPANISH, PracticeType.CONVERSATION)
        )

        assertContains(prompt, "Session type: conversation practice.")
        assertFalse(prompt.contains("Session type: translation practice."))
    }

    @Test
    fun translationPromptAsksForOneSentenceAtATime() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(Language.SPANISH, PracticeType.TRANSLATION)
        )

        assertContains(prompt, "Session type: translation practice.")
        assertContains(prompt, "one sentence in English at a time")
        assertContains(prompt, "translate it into Spanish")
    }

    @Test
    fun promptTellsModelToOpenSessionWhenThereAreNoUserMessages() {
        PracticeType.entries.forEach { practiceType ->
            val prompt = promptBuilder.buildSystemPrompt(
                chatConfig(Language.ENGLISH, practiceType)
            )

            assertContains(prompt, "If there are no user messages yet, start the session yourself")
        }
    }

    @Test
    fun conversationPromptPinsOneSituationForWholeSession() {
        val prompt = PromptBuilder(Random(42)).buildSystemPrompt(
            chatConfig(Language.SPANISH, PracticeType.CONVERSATION)
        )

        assertContains(prompt, "The situation for this session is: you are ")
        assertContains(prompt, "Your manner:")
        assertContains(prompt, "Do not replace this situation with another one")
    }

    @Test
    fun conversationSituationsAreCombinedFromIndependentAxes() {
        val situations = (1..40).map { seed ->
            PromptBuilder(Random(seed)).buildSystemPrompt(
                chatConfig(Language.SPANISH, PracticeType.CONVERSATION)
            ).situationLine()
        }

        // Combining three axes has to give many more distinct situations than a single fixed list of scenes would.
        assertTrue(
            situations.distinct().size >= 35,
            "Expected nearly all situations to differ, got ${situations.distinct().size} distinct out of ${situations.size}."
        )
    }

    @Test
    fun translationPromptPinsThemeFocusAndStyle() {
        val prompt = PromptBuilder(Random(42)).buildSystemPrompt(
            chatConfig(Language.SPANISH, PracticeType.TRANSLATION)
        )

        assertContains(prompt, "Draw this session's sentences from ")
        assertContains(prompt, "and make them practise ")
        assertContains(prompt, "Write them as ")
        assertContains(prompt, "Keep to this theme, focus and style for the whole session.")
        assertFalse(prompt.contains("The situation for this session is:"))
    }

    @Test
    fun translationSessionsDoNotAllGetTheSameBrief() {
        val briefs = (1..40).map { seed ->
            PromptBuilder(Random(seed)).buildSystemPrompt(
                chatConfig(Language.SPANISH, PracticeType.TRANSLATION)
            ).briefLine()
        }

        assertTrue(
            briefs.distinct().size >= 35,
            "Expected nearly all briefs to differ, got ${briefs.distinct().size} distinct out of ${briefs.size}."
        )
    }

    @Test
    fun conversationPromptDoesNotPickTranslationBrief() {
        val prompt = PromptBuilder(Random(42)).buildSystemPrompt(
            chatConfig(Language.SPANISH, PracticeType.CONVERSATION)
        )

        assertFalse(prompt.contains("Draw this session's sentences from"))
    }

    @Test
    fun sameLanguageOnBothSidesRunsTheSessionInThePracticeLanguage() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(Language.GERMAN, PracticeType.CONVERSATION, Language.GERMAN)
        )

        assertContains(prompt, "stay in German for the whole session")
        assertContains(prompt, "in simple German")
        assertFalse(prompt.contains("is the language the user already knows"))
    }

    @Test
    fun sameLanguageTurnsTranslationPracticeIntoRephrasing() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(Language.GERMAN, PracticeType.TRANSLATION, Language.GERMAN)
        )

        assertContains(prompt, "Session type: rephrasing practice.")
        assertContains(prompt, "say the same thing in their own words")
        assertFalse(prompt.contains("translate it into German"))
    }

    @Test
    fun differentLanguagesKeepTranslationPractice() {
        val prompt = promptBuilder.buildSystemPrompt(
            chatConfig(Language.GERMAN, PracticeType.TRANSLATION, Language.POLISH)
        )

        assertContains(prompt, "Session type: translation practice.")
        assertContains(prompt, "translate it into German")
        assertFalse(prompt.contains("rephrasing practice"))
    }

    @Test
    fun everyConfigurationProducesNonBlankPrompt() {
        Language.entries.forEach { practiceLanguage ->
            Language.entries.forEach { assistantLanguage ->
                PracticeType.entries.forEach { practiceType ->
                    val prompt = promptBuilder.buildSystemPrompt(
                        chatConfig(practiceLanguage, practiceType, assistantLanguage)
                    )

                    assertTrue(
                        prompt.isNotBlank(),
                        "Prompt for $practiceLanguage/$assistantLanguage/$practiceType is blank."
                    )
                }
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
