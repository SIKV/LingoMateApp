package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatModelTier", exact = true)
enum class ChatModelTier {
    FAST,
    SMART,
}
