package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatRecap", exact = true)
data class ChatRecap(
    val summary: String,
)
