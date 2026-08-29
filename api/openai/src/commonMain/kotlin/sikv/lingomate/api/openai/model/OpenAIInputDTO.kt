package sikv.lingomate.api.openai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIInputDTO(
    @SerialName("role")
    val role: String, // assistant | user
    @SerialName("content")
    val content: String
)
