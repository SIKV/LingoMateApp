package sikv.lingomate.domain.chat

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("ChatModelProvider", exact = true)
enum class ChatModelProvider {
    ON_DEVICE,
    OPEN_AI,
}
