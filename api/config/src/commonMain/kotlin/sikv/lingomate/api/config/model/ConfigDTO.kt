package sikv.lingomate.api.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigDTO(
    @SerialName("chat_models")
    val chatModels: List<ChatModelDTO> = emptyList(),
    @SerialName("languages")
    val languages: List<String> = emptyList() // BCP 47 codes, e.g. "en".
)
