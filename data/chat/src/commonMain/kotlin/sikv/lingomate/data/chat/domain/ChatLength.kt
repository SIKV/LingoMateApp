package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("ChatLength", exact = true)
enum class ChatLength(val length: Int) {
    // _LENGTH postfix is needed to avoid naming conflicts in SwiftUI
    SHORT_LENGTH(5),
    MEDIUM_LENGTH(10),
    LONG_LENGTH(20)
}
