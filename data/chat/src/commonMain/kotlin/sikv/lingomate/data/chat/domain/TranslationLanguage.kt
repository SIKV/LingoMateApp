package sikv.lingomate.data.chat.domain

import kotlinx.serialization.Serializable
import kotlin.native.ObjCName

@Serializable
@ObjCName("TranslationLanguage", exact = true)
enum class TranslationLanguage {
    ENGLISH,
}
