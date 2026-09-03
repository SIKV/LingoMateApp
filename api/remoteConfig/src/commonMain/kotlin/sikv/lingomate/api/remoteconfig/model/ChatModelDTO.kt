package sikv.lingomate.api.remoteconfig.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatModelDTO(
    @SerialName("provider")
    val provider: String,
    @SerialName("model")
    val model: String
)
