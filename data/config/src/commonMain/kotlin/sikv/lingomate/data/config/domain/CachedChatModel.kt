package sikv.lingomate.data.config.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CachedChatModel(
    @SerialName("provider")
    val provider: String,
    @SerialName("model")
    val model: String
)
