package sikv.lingomate.api.openai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class OpenAITextDTO(
    @SerialName("format")
    val format: Format
) {
    @Serializable
    data class Format(
        @SerialName("type")
        val type: String,
        @SerialName("name")
        val name: String,
        @SerialName("schema")
        val schema: JsonObject,
        @SerialName("strict")
        val strict: Boolean
    )
}
