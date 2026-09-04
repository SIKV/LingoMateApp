package sikv.lingomate.api.remoteconfig.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteConfigDTO(
    @SerialName("chat_models")
    val chatModels: List<ChatModelDTO> = emptyList(),
    @SerialName("languages")
    val languages: List<String> = emptyList()
)
