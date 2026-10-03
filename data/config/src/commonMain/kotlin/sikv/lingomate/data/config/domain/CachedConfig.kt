package sikv.lingomate.data.config.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CachedConfig(
    @SerialName("chat_models")
    val chatModels: List<CachedChatModel> = emptyList(),
    @SerialName("language_codes")
    val languageCodes: List<String> = emptyList()
)
