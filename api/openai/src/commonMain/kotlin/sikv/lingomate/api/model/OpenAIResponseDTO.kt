package sikv.lingomate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponseDTO(
    @SerialName("id")
    val id: String,
    @SerialName("output")
    val output: List<OpenAIOutputDTO>
)
