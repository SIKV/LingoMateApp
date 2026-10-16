package sikv.lingomate.data.chat.service

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import sikv.lingomate.api.openai.OpenAIApi
import sikv.lingomate.api.openai.model.OpenAIInputDTO
import sikv.lingomate.api.openai.model.OpenAITextDTO
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatRecap
import sikv.lingomate.data.chat.domain.ChatResponseChunk
import sikv.lingomate.data.chat.mapping.toDTO
import sikv.lingomate.data.chat.mapping.toDomain
import sikv.lingomate.data.chat.model.ChatRecapDTO
import sikv.lingomate.data.chat.model.chatRecapSchema
import sikv.lingomate.logger.Log

interface ChatRecapService {
    suspend fun getRecap(messages: List<ChatMessage>): Result<ChatRecap>
}

class OpenAIChatRecapService(
    private val chatConfig: ChatConfig,
    private val openAIApi: OpenAIApi,
    private val promptBuilder: RecapPromptBuilder,
    private val json: Json
) : ChatRecapService {

    override suspend fun getRecap(messages: List<ChatMessage>): Result<ChatRecap> {
        Log.d { "Requesting a recap of ${messages.size} messages from model ${chatConfig.chatModel.model}." }

        val lastChunk = openAIApi
            .streamResponse(
                model = chatConfig.chatModel.model,
                input = listOf(
                    OpenAIInputDTO(
                        role = ChatMessage.Role.USER.toDTO(),
                        content = promptBuilder.buildRecapTranscript(messages)
                    )
                ),
                instructions = promptBuilder.buildRecapPrompt(chatConfig),
                text = OpenAITextDTO(
                    format = OpenAITextDTO.Format(
                        type = "json_schema",
                        name = "chat_recap",
                        schema = chatRecapSchema,
                        strict = true
                    )
                )
            )
            .map { result -> result.map { response -> response.toDomain(RECAP_RESPONSE_ID) } }
            // The recap is only usable once complete, so stop at the first chunk that ends the response.
            .firstOrNull { result -> result.fold(onSuccess = { it.isFinal }, onFailure = { true }) }
            ?: Result.failure(IllegalStateException("The response stream ended before the response did."))

        return lastChunk
            .mapCatching { chunk ->
                val text = (chunk as? ChatResponseChunk.Completed)?.content
                    ?.takeIf { it.status == ChatMessage.Status.DELIVERED }
                    ?.text
                    ?: error("The response ended without a recap: $chunk.")

                json.decodeFromString<ChatRecapDTO>(text).toDomain()
            }
            .onFailure { error ->
                Log.e(error) { "Failed to get a recap." }
            }
    }

    private companion object {
        // The chunk mapping needs a message ID, but the recap is never shown as a message.
        const val RECAP_RESPONSE_ID = "recap"
    }
}

private val ChatResponseChunk.isFinal: Boolean
    get() = when (this) {
        ChatResponseChunk.Created,
        is ChatResponseChunk.InProgress -> false
        is ChatResponseChunk.Completed,
        is ChatResponseChunk.Failed,
        ChatResponseChunk.Error -> true
    }
