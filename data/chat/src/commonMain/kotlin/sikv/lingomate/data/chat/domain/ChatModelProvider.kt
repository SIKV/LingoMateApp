package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatModelProvider", exact = true)
enum class ChatModelProvider {
    ON_DEVICE,
    OPEN_AI,
}
