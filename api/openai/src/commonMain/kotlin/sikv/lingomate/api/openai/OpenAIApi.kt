package sikv.lingomate.api.openai

import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import sikv.lingomate.api.openai.model.OpenAIInputDTO
import sikv.lingomate.api.openai.model.OpenAIResponsesRequestDTO
import sikv.lingomate.api.openai.model.OpenAIResponsesResponseDTO
import sikv.lingomate.logger.Log

class OpenAIApi(
    private val client: HttpClient,
    private val json: Json
) {
    fun streamResponse(
        model: String,
        input: List<OpenAIInputDTO>,
        instructions: String
    ): Flow<Result<OpenAIResponsesResponseDTO>> = flow {
        Log.d { "Requesting a streamed response. Model: $model, input messages: ${input.size}." }

        client.sse(
            request = {
                // TODO: Refactor.
                url("https://api.openai.com/v1/responses")
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                bearerAuth("API_KEY")
                setBody(
                    OpenAIResponsesRequestDTO(
                        model = model,
                        input = input,
                        instructions = instructions,
                        store = false, // Never store the generated response on the Server.
                        stream = true,
                    )
                )
            }
        ) {
            incoming
                .map { event: ServerSentEvent ->
                    if (event.event == "response.completed" || event.event == "response.failed" || event.event == "error") {
                        this.cancel()
                    }
                    event.data?.let { rawJson ->
                        try {
                            Result.success(json.decodeFromString<OpenAIResponsesResponseDTO>(rawJson))
                        } catch (e: Exception) {
                            Log.e(e) { "Failed to parse the \"${event.event}\" event." }
                            Result.failure(e)
                        }
                    } ?: run {
                        Log.w { "Received the \"${event.event}\" event with no data." }
                        Result.failure(Exception("Empty event data."))
                    }
                }
                .collect {
                    emit(it)
                }
        }
    }.catch { e ->
        // The session is cancelled on purpose once a terminal event arrives, so that is not a failure.
        if (e !is CancellationException) {
            Log.e(e) { "Response stream failed." }
        }

        emit(Result.failure(e))
    }
}
