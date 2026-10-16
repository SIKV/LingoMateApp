package sikv.lingomate.data.chat.service

import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage

class RecapPromptBuilder {

    /**
     * Builds the instructions for recapping a finished chat session. The session itself goes to the model as the
     * input, written out by [buildRecapTranscript]. The field names match the recap's JSON schema.
     */
    fun buildRecapPrompt(chatConfig: ChatConfig): String {
        val practiceLanguage = chatConfig.practiceLanguage.promptName
        val explanationLanguage = chatConfig.explanationLanguage
        return """
            You are LingoMate, a patient and encouraging language tutor.
            The input is the transcript of a practice session in which the user was learning $practiceLanguage. The
            session is over, so do not continue it. Give the user a short recap of how it went.
            - summary: two or three short sentences in $explanationLanguage.
              Say what the user did well and name the one thing most worth working on next, without listing every mistake.
              Ignore typos, missing accents and punctuation unless they change the meaning.
            - Judge only the user's lines; the tutor's lines are there for context.
            - Speak to the user as "you" and write plain text with no markdown.
        """.trimIndent()
    }

    /**
     * Writes a chat session out as plain text for [buildRecapPrompt]. Only delivered messages are kept: they are the
     * conversation the model actually had.
     */
    fun buildRecapTranscript(messages: List<ChatMessage>): String {
        return messages
            .filter { it.status == ChatMessage.Status.DELIVERED }
            .mapNotNull { message ->
                when (message.role) {
                    ChatMessage.Role.USER -> "User: ${message.text}"
                    ChatMessage.Role.ASSISTANT -> "Tutor: ${message.text}"
                    ChatMessage.Role.SYSTEM -> null
                }
            }
            .joinToString(separator = "\n\n")
    }
}
