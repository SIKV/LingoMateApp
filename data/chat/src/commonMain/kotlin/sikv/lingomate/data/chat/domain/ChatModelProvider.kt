package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatModelProvider", exact = true)
enum class ChatModelProvider {
    ON_DEVICE,
    OPEN_AI,
}
