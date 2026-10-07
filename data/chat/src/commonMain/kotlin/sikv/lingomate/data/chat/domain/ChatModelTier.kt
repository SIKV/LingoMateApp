package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

/** How a model compares to the others on offer, so the user can tell which is cheaper or smarter. */
@ObjCName("ChatModelTier", exact = true)
enum class ChatModelTier {
    FAST,
    SMART,
}
