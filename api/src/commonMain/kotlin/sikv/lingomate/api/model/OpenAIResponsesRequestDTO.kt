package sikv.lingomate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponsesRequestDTO(
    @SerialName("model")
    val model: String,
    @SerialName("input")
    val input: List<OpenAIInputDTO>,
    // A system (or developer) message inserted into the model's context.
    @SerialName("instructions")
    val instructions: String,
    // Whether to store the generated model response for later retrieval via API.
    @SerialName("store")
    val store: Boolean,
    @SerialName("stream")
    val stream: Boolean
)
