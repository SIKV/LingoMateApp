package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatRecap", exact = true)
data class ChatRecap(
    val summary: String,
    // From 1 to 10.
    val score: Int,
)
