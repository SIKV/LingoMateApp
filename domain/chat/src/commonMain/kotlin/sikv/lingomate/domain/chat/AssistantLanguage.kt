package sikv.lingomate.domain.chat

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("AssistantLanguage", exact = true)
enum class AssistantLanguage {
    ENGLISH,
}
