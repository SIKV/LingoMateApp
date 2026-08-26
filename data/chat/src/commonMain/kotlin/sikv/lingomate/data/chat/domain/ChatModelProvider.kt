package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatModelProvider", exact = true)
enum class ChatModelProvider(
    val apiKeyRequired: Boolean
) {
    ON_DEVICE(apiKeyRequired = false),
    OPEN_AI(apiKeyRequired = true),
}
