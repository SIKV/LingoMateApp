package sikv.lingomate.data.chat.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sikv.lingomate.api.OpenAIApi
import sikv.lingomate.data.chat.domain.ChatContent
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel
import sikv.lingomate.data.chat.domain.ChatResponseChunk
import sikv.lingomate.data.chat.mapping.toDomain
import sikv.lingomate.data.chat.mapping.toInputDTO

class ChatService(
    private val chatLanguage: ChatLanguage,
    private val chatLength: ChatLength,
    private val chatModel: ChatModel,
    private val openAIApi: OpenAIApi,
    private val promptBuilder: PromptBuilder
) {
    // TODO: For testing.
    private val chatHistory = mutableListOf<ChatContent>()

    fun startNewChat(): Flow<Result<ChatResponseChunk>> {
        val firstMessage = ChatContent(
            role = ChatContent.Role.USER,
            text = "Continue the count. I start: One"
        )

        chatHistory.add(firstMessage)

        return openAIApi
            .streamResponse(
                model = chatModel.id,
                input = chatHistory.map { it.toInputDTO() },
                instructions = promptBuilder.buildSystemPrompt(chatLanguage, chatLength)
            )
            .map { result ->
                result.fold(
                    onSuccess = { value -> Result.success(value.toDomain(chatHistory)) },
                    onFailure = { error -> Result.failure(error) }
                )
            }
    }

    fun reply(
        message: String
    ): Flow<Result<ChatResponseChunk>> {
        TODO()
    }
}
