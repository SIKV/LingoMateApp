package sikv.lingomate.domain.chat

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("PracticeType", exact = true)
enum class PracticeType {
    CONVERSATION, // Chat with an AI about real-life situations.
    TRANSLATION, // Translate sentences into your learning language.
}
