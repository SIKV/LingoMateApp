package sikv.lingomate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponsesResponseDTO(
    @SerialName("type")
    val type: String,
    @SerialName("response")
    val response: OpenAIResponseDTO? = null,
    @SerialName("sequence_number")
    val sequenceNumber: Int
)
