package sikv.lingomate.data.chat.domain

import kotlin.native.ObjCName

@ObjCName("PracticeType", exact = true)
enum class PracticeType {
    CONVERSATION, // Chat with an AI about real-life situations.
    TRANSLATION, // Translate sentences into your learning language.
}
