package sikv.lingomate.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import sikv.lingomate.api.model.OpenAIInputDTO
import sikv.lingomate.api.model.OpenAIResponsesRequestDTO
import sikv.lingomate.api.model.OpenAIResponsesResponseDTO

class OpenAIApi(
    private val client: HttpClient,
    private val json: Json
) {
    fun streamResponse(
        model: String,
        input: List<OpenAIInputDTO>,
        instructions: String
    ): Flow<Result<OpenAIResponsesResponseDTO>> = flow {
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
                            Result.failure(e)
                        }
                    } ?: run {
                        Result.failure(Exception("Empty event data."))
                    }
                }
                .collect {
                    emit(it)
                }
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}
