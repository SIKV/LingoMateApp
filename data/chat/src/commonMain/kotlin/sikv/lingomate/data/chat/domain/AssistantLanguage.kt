package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("AssistantLanguage", exact = true)
enum class AssistantLanguage {
    ENGLISH,
}
