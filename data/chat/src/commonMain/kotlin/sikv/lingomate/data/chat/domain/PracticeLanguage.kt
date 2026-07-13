package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("PracticeLanguage", exact = true)
enum class PracticeLanguage {
    ENGLISH,
    SPANISH,
}
