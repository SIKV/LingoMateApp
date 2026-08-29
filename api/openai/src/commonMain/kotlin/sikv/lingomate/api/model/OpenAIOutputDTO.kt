package sikv.lingomate.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIOutputDTO(
    @SerialName("role")
    val role: String = "",
    @SerialName("content")
    val content: List<Content> = emptyList()
) {
    @Serializable
    data class Content(
        @SerialName("type")
        val type: String = "text",
        @SerialName("text")
        val text: String
    )
}
